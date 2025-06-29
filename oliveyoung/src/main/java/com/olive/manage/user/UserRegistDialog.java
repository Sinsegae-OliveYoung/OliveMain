package com.olive.manage.user;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.olive.common.config.Config;
import com.olive.common.exception.EmailException;
import com.olive.common.exception.UserException;
import com.olive.common.model.Branch;
import com.olive.common.model.Member;
import com.olive.common.model.Role;
import com.olive.common.model.User;
import com.olive.common.repository.BranchDAO;
import com.olive.common.repository.MemberDAO;
import com.olive.common.repository.UserDAO;
import com.olive.common.util.DBManager;
import com.olive.common.util.MailSender;
import com.olive.common.util.StringUtil;
import com.olive.common.util.style.ButtonUtil;
import com.olive.common.util.style.ComboBoxUtil;

public class UserRegistDialog extends JDialog{
	UserListPanel userListPanel;
	
	int tf_size = 15;
	JTextField t_name = new JTextField(tf_size);
	JTextField t_no = new JTextField(tf_size);
	JTextField t_email = new JTextField(tf_size);
	JTextField t_tel = new JTextField(tf_size);
	
	// 팀장이 사용자를 등록할때 용
	JComboBox<Role> cb_role;
	JComboBox<Branch> cb_br;
	
	//점장이 사용자를 등록할때는 지점/직급이 고정되어있으므로 라벨로 표시
	JLabel lb_br = new JLabel();
	JLabel lb_role = new JLabel();
	
	UserDAO userDAO = new UserDAO();
	MemberDAO memberDAO = new MemberDAO();
	BranchDAO branchDAO = new BranchDAO();
	DBManager dbManager = DBManager.getInstance();
	
	public UserRegistDialog(UserListPanel userListPanel) {		
		this.userListPanel = userListPanel;
		User u = userListPanel.getMainLayout().user;
		
		cb_role = ComboBoxUtil.createRoleComboBoxWithNoDummy(u.getRole().getRole_id());
		cb_br = ComboBoxUtil.createBranchComboBoxWithNoDummy(u.getUser_id());
		
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		
		p.add(createRow("   이름", t_name));
		p.add(createRow("지점", cb_br));		
		p.add(createRow("직급", cb_role));		
		p.add(createRow("   사원번호", t_no));  
		p.add(createRow("   이메일",  t_email));		
		p.add(createRow("   연락처",  t_tel));		
		add(p);

		// 로그인한 사람이 팀장이다. 
		// 직급 콤보박스 수정 (점장, 스태프)  
		if(((Role)cb_role.getSelectedItem()).getRole_id() == 2) {
			Branch br = new Branch();
			br.setBr_id(99);
			br.setBr_name("미지정");
			
			cb_br.insertItemAt(br, 0);
			cb_br.setSelectedIndex(0);
			cb_br.setEnabled(false);
		}
		
		cb_role.addItemListener(e -> {
		  if (e.getStateChange() == ItemEvent.SELECTED) {
		        Role selectedRole = (Role) e.getItem();

		        if (selectedRole.getRole_id() == 2) { // 점장
		            // 지점을 "임시지점"으로 설정하고 비활성화
		        	Branch br = new Branch();
					br.setBr_id(99);
					br.setBr_name("미지정");
		            cb_br.insertItemAt(br, 0);
		            cb_br.setSelectedIndex(0);
		            cb_br.setEnabled(false);

		        } else { // 스태프
		            // 지점 목록 다시 세팅 (미지정 없이)
		            cb_br.removeItemAt(0);
		            cb_br.setEnabled(true);
		        } 
		    }
		});
		
		// 팀장이 사원을 등록할때 점장을 선택하면, 지점이 임시지점으로 세팅 후 변경 불가능 
		// 스태프를 선택하면, 지점을 선택가능하게 함 (미지정 없는 지점)
		
		JPanel p_south = new JPanel();
		p_south.setBackground(Config.WHITE);
		add(p_south);
		
		JButton bt_regist = ButtonUtil.greenButtonUtil("등록");
		p_south.add(bt_regist);
		
		
		// 전화번호: 숫자만 입력 가능, 13자리 제한 (xxx-xxxx-xxxx 형식)
		t_tel.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				
				// 백스페이스도 KeyTyped 된 것이라 지우자마자 하이픈이 다시 생기는 것 방지
				if(c == KeyEvent.VK_BACK_SPACE) 
					return;
				
				int len = t_tel.getText().length();
				
				//숫자가 아니거나 길이 초과 시 입력 무시
				if(!Character.isDigit(c) || len >= 13) {
					e.consume();
				}
		        //하이픈 자동 삽입
		        if ((len == 3 || len == 8) && c != '-') {
		            t_tel.setText(t_tel.getText() + "-");
		        }
			}
		});
		
		// 사원번호 : 숫자만 입력 가능, 4자리 제한 
		t_no.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if(!Character.isDigit(c) || t_no.getText().length() >= 4){
					e.consume();
				}
			}
		});
		
		bt_regist.addActionListener(e -> {
			// 입력값 유효성 체크 
			if(isFormValid()) {
				insert();
			}
		});
		
		setTitle("사원 등록");
		setSize(400, 480);
		setLayout(new FlowLayout());
		getContentPane().setBackground(Config.WHITE);
		setLocationRelativeTo(userListPanel);
		setVisible(true);
	}
	
	private JPanel createRow(String str, JComponent field) {
		JPanel row = new JPanel(new FlowLayout());
		row.setBackground(Config.WHITE);

	    JLabel label = new JLabel(str, JLabel.LEFT);
	    label.setPreferredSize(new Dimension(150, 50));
	    row.add(label);
	    
	    field.setPreferredSize(new Dimension(150, 30));
	    row.add(field);
		
	    return row;
	}
	
	public boolean isFormValid() {
		boolean flag = true;
		
		if(t_name.getText().length() < 1) {
			JOptionPane.showMessageDialog(this, "사원명을 입력하세요");
			flag = false;
		}
		else if(t_no.getText().length() < 1) {
			JOptionPane.showMessageDialog(this, "사원번호 4자리를 입력하세요");
			flag = false;
		}
		else if(t_email.getText().length() < 1) {
			JOptionPane.showMessageDialog(this, "이메일을 입력하세요 (예: email_id@gmail.com)");
			flag = false;
		}
		else if(!t_email.getText().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
			JOptionPane.showMessageDialog(this, "유효한 이메일 형식이 아닙니다. (예: email_id@gmail.com)");
			flag = false;
		}
		else if(t_tel.getText().length() < 1 || t_tel.getText().length() != 13) {			
			JOptionPane.showMessageDialog(this, "유효한 전화번호를 입력하세요 (예: 010-1234-5678)");
			flag = false;
		}
		
		return flag; 
	}
	
	public static String generateTempPwd(){
		int length = 6;
	    String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	    StringBuilder sb = new StringBuilder();
	    SecureRandom random = new SecureRandom();

	    for (int i = 0; i < length; i++) {
	        sb.append(chars.charAt(random.nextInt(chars.length())));
	    }
	    
	    return sb.toString();
	}
	
	public void insert() {
		// 트랜잭션 : user가 등록 실패 시, memeber 등록도 안돼야 한다.
		Connection con = dbManager.getConnection();
		try {
			con.setAutoCommit(false);
		
		User user = new User();
		user.setUser_name(t_name.getText());
		user.setUser_no(Integer.parseInt(t_no.getText()));
		user.setEmail(t_email.getText());
		user.setTel(t_tel.getText());
		user.setRole((Role)cb_role.getSelectedItem());
		user.setHiredate(Date.valueOf(LocalDate.now()));  //등록 날짜
		
		// 임시 비밀번호 생성 후 DB에 insert 성공 시, 입력한 이메일로 임시 비밀번호 전송 
		String tmpPwd = generateTempPwd();
		String securedPwd = StringUtil.getSecuredPass(tmpPwd);
		user.setPwd(securedPwd); 
		
		userDAO.insert(user);
		
		// db에서 생성된 user pk 가져오기
		user.setUser_id(userDAO.selectRecentPk());
		
		Member member = new Member();
		member.setUser(user);
		member.setBranch((Branch)cb_br.getSelectedItem());
		memberDAO.insert(member);
		
		con.commit(); //에러 없으니 커밋
		
		try {
			// 이메일 전송
			MailSender mailSender = new MailSender();
			
			String content = "임시 비밀번호: " + tmpPwd + "\n 빠른 시일내에 변경해 주세요.";
			mailSender.send(user.getEmail(), "[올리브영] 임시 비밀번호 발급", content);
		} catch (EmailException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
		JOptionPane.showMessageDialog(this, "사원 등록이 완료되었습니다. 임시 비밀번호가 발급되었으니 메일을 확인해주세요.");
		// 나중에 함수로 묶기 
		userListPanel.getMainLayout().setDataDirty(true);
		userListPanel.getMainLayout().refreshIfDirty();
		} catch (UserException e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
		catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			dispose();
		}
	}
	
}
