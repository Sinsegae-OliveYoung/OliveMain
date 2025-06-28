package com.olive.manage.user;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
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
import com.olive.common.repository.MemberDAO;
import com.olive.common.repository.UserDAO;
import com.olive.common.util.DBManager;
import com.olive.common.util.MailSender;
import com.olive.common.util.StringUtil;
import com.olive.common.util.style.ButtonUtil;
import com.olive.common.util.style.ComboBoxUtil;

public class UserUpdateDialog extends JDialog{
	UserDetailPanel userDetailPanel;
	
	int tf_size = 15;
	JTextField t_name = new JTextField(tf_size);
	JTextField t_email = new JTextField(tf_size);
	JTextField t_tel = new JTextField(tf_size);
	JLabel lb_no = new JLabel();
	JLabel lb_br = new JLabel();
	
	JComboBox<Role> cb_role;
	
	UserDAO userDAO = new UserDAO();
	MemberDAO memberDAO = new MemberDAO();
	DBManager dbManager = DBManager.getInstance();
	
	public UserUpdateDialog(UserDetailPanel userDetailPanel) {		
		this.userDetailPanel = userDetailPanel;
		User u = userDetailPanel.member.getUser();
		
		cb_role = ComboBoxUtil.createRoleComboBoxWithNoDummy(userDetailPanel.getMainLayout().user.getRole().getRole_id());
		
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		
		p.add(createRow("   이름", t_name = new JTextField(u.getUser_name(), tf_size)));
		
		lb_no.setOpaque(true);
		lb_no.setBackground(Config.LIGHT_GRAY);
		lb_no.setText(Integer.toString(u.getUser_no()));
		p.add(createRow(" 사원번호", lb_no));
		lb_no.setPreferredSize(new Dimension(170, 30));
		
		lb_br.setOpaque(true);
		lb_br.setBackground(Config.LIGHT_GRAY);
		lb_br.setText(userDetailPanel.member.getBranch().getBr_name());
		p.add(createRow(" 지점", lb_br));
		lb_br.setPreferredSize(new Dimension(170, 30));
		
		// 로그인한 사용자가 팀장이면 콤보박스 
		p.add(createRow("직급", cb_role));
		p.add(createRow("   이메일",  t_email = new JTextField(u.getEmail(), tf_size)));		
		p.add(createRow("   연락처",  t_tel = new JTextField(u.getTel(), tf_size)));		
		add(p);
		
		JPanel p_south = new JPanel();
		p_south.setBackground(Config.WHITE);
		add(p_south);
		
		JButton bt_update = ButtonUtil.greenButtonUtil("완료");
		p_south.add(bt_update);
		
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
		
		bt_update.addActionListener(e -> {
			// 입력값 유효성 체크 
			if(isFormValid()) {
				update();
			}
		});
		
		setTitle("사원 등록");
		setSize(400, 480);
		setLayout(new FlowLayout());
		getContentPane().setBackground(Config.WHITE);
		setLocationRelativeTo(userDetailPanel);
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
	
	public void update() {
		
		try {
		
			User user = userDetailPanel.member.getUser();
			user.setUser_name(t_name.getText());
			user.setEmail(t_email.getText());
			user.setTel(t_tel.getText());
			user.setRole((Role)cb_role.getSelectedItem());
		
			userDAO.update(user);
			
			// 점장 -> 스태프 
//			Branch branch = userDetailPanel.member.getBranch();
//			branch.setUser();
//			branch.setBr_name(getName());
			
			
			JOptionPane.showMessageDialog(this, "정보 수정이 완료되었습니다.");
			dispose();
			
		} catch(UserException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}
	
}
