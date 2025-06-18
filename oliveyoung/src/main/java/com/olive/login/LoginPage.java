package com.olive.login;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.olive.common.config.Config;
import com.olive.common.model.Branch;
import com.olive.common.model.Member;
import com.olive.common.model.Role;
import com.olive.common.model.User;
import com.olive.common.util.DBManager;
import com.olive.common.util.ImageUtil;
import com.olive.common.util.StringUtil;
import com.olive.login.security.model.Admin;
import com.olive.login.security.repository.AdminDAO;
import com.olive.mainlayout.MainLayout;

public class LoginPage extends JFrame {
	JPanel p_bg; // 연두색 전체 패널
	JPanel p_login; // 가운데 로그인 패널

	JPanel p_title;
	Image img;
	ImageUtil img_title = new ImageUtil();

	JPanel p_id;
	JLabel lb_id;
	JTextField t_id;

	JPanel p_pwd;
	JLabel lb_pwd;
	JPasswordField t_pwd;

	JButton bt_login;

	DBManager dbManager = DBManager.getInstance();
	MainLayout mainLayout;
	public Admin admin = new Admin();

	public LoginPage() {

		// create
		p_bg = new JPanel();
		p_login = new JPanel();
		img = img_title.getImage("images/logo2.png", 300, 35);

		p_title = new JPanel() {
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(img, 0, 0, 300, 35, p_title);
			}
		};

		p_id = new JPanel();
		lb_id = new JLabel("아이디");
		t_id = new JTextField(10);

		p_pwd = new JPanel();
		lb_pwd = new JLabel("패스워드");
		t_pwd = new JPasswordField(16);

		bt_login = new JButton("로그인");

		// style
		p_bg.setBackground(Config.LIGHT_GREEN);
		p_bg.setLayout(new GridBagLayout()); // 가운데 정렬을 위한 레이아웃

		p_login.setBackground(Config.WHITE);
		p_login.setBorder(BorderFactory.createEmptyBorder(50, 50, 70, 50)); // 패딩 추가
		p_login.setLayout(new BoxLayout(p_login, BoxLayout.Y_AXIS)); // 수직 정렬
		p_login.setPreferredSize(new Dimension(400, 350));

		p_title.setBackground(Config.WHITE);

		lb_id.setHorizontalAlignment(JLabel.RIGHT); // 텍스트 오른쪽 정렬
		lb_id.setFont(new Font("Noto Sans KR", Font.BOLD, 17));
		lb_id.setBackground(Config.LIGHT_GRAY);

		p_id.setLayout(new BorderLayout(20, 0)); // 좌우 간격 5px
		p_id.setOpaque(false); // 배경 투명
		p_id.setMaximumSize(new Dimension(300, 40));
		p_id.setBorder(BorderFactory.createEmptyBorder(0, 51, 0, 40));

		lb_pwd.setHorizontalAlignment(JLabel.RIGHT);
		lb_pwd.setFont(new Font("Noto Sans KR", Font.BOLD, 17));
		lb_pwd.setBackground(new Color(245, 245, 245));

		p_pwd.setLayout(new BorderLayout(20, 0));
		p_pwd.setOpaque(false);
		p_pwd.setMaximumSize(new Dimension(300, 40));
		p_pwd.setBorder(BorderFactory.createEmptyBorder(0, 35, 0, 40));

		bt_login.setAlignmentX(Component.CENTER_ALIGNMENT);
		bt_login.setBackground(Config.LIGHT_GRAY);
		bt_login.setFont(new Font("Noto Sans KR", Font.BOLD, 15));
		bt_login.setPreferredSize(new Dimension(120, 30));

		// assemble
		p_login.add(Box.createVerticalStrut(25)); // 상하 간격 추가

		p_id.add(lb_id, BorderLayout.WEST);
		p_id.add(Box.createHorizontalStrut(50)); // 좌우 간격 추가
		p_id.add(t_id, BorderLayout.CENTER);
		p_login.add(p_title);
		p_login.add(p_id);
		p_login.add(Box.createVerticalStrut(10));

		p_pwd.add(lb_pwd, BorderLayout.WEST);
		p_pwd.add(t_pwd, BorderLayout.CENTER);
		p_login.add(p_pwd);
		p_login.add(Box.createVerticalStrut(30));

		p_login.add(bt_login);
		
		// 리스너 연결
		t_pwd.addActionListener(e->nullCheck());
		bt_login.addActionListener(e->nullCheck());
		
		p_bg.add(p_login);
		add(p_bg);

		setTitle("로그인");
		setSize(1300, 800);
		setLocationRelativeTo(null); // 창을 화면 가운데에 배치 (setDefault와 세트)
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	public void nullCheck() {
		if (t_id.getText().isEmpty())
			JOptionPane.showMessageDialog(LoginPage.this, "아이디를 입력해주세요");
		else if (t_pwd.getPassword().length < 1)
			JOptionPane.showMessageDialog(LoginPage.this, "패스워드를 입력해주세요");
		else
			loginCheck();
	}
	
	public void loginCheck() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String id = t_id.getText();
		String pwd = new String(t_pwd.getPassword());
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT"
				+ "    a.admin_id,"
				+ "    a.user_no,"
				+ "    a.pwd,"
				+ "    u.user_id,"
				+ "    u.user_no AS u_user_no,"
				+ "    u.user_name,"
				+ "    u.tel,"
				+ "    u.email,"
				+ "    u.hiredate,"
				+ "    u.role_id,"
				+ "    r.role_code,"
				+ "    r.role_name,"
				+ "    m.mem_id,"
				+ "    m.br_id AS m_br_id,"
				+ "    b.br_id,"
				+ "    b.br_name,"
				+ "    b.br_address,"
				+ "    b.br_tel"
				+ " FROM admin a"
				+ " INNER JOIN user u ON a.user_no = u.user_no"
				+ " INNER JOIN role r ON u.role_id = r.role_id"
				+ " INNER JOIN member m ON u.user_id = m.user_id"
				+ " INNER JOIN branch b ON m.br_id = b.br_id"
				+ " WHERE a.user_no=?"
				+ " AND a.pwd=?");

		try {
			con = dbManager.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, id);
			pstmt.setString(2, StringUtil.getSecuredPass(pwd));			
			rs = pstmt.executeQuery();

			List<Admin> list = new ArrayList();

			while (rs.next()) {
				// 1. User 생성 및 값 설정
				User user = new User();
				user.setUser_id(rs.getInt("user_id"));
				user.setUser_no(rs.getInt("user_no"));
				user.setUser_name(rs.getString("user_name"));
				user.setTel(rs.getString("tel"));
				user.setEmail(rs.getString("email"));
				user.setHiredate(rs.getDate("hiredate"));

				// 2. Branch 생성 및 값 설정
				Branch branch = new Branch();
				branch.setBr_id(rs.getInt("br_id"));
				branch.setBr_name(rs.getString("br_name"));
				branch.setBr_address(rs.getString("br_address"));
				branch.setBr_tel(rs.getString("br_tel"));

				// branch → user 참조
				branch.setUser(user);

				// 3. Member 생성 및 값 설정
				Member member = new Member();
				member.setMem_id(rs.getInt("mem_id"));
				member.setUser(user);     // member → user 참조
				member.setBranch(branch); // member → branch 참조

				// 4. Role 생성 및 값 설정
				Role role = new Role();
				role.setRole_id(rs.getInt("role_id"));
				role.setRole_code(rs.getString("role_code"));
				role.setRole_name(rs.getString("role_name"));
				role.setMember(member);   // role → member 참조

				// user → role 참조 (양방향)
				user.setRole(role);

				// 5. Admin 생성 및 값 설정
				Admin admin = new Admin();
				admin.setAdmin_id(rs.getInt("admin_id"));
				admin.setPwd(rs.getString("pwd"));
				admin.setUser(user);      // admin → user 참조
				
				list.add(admin);
			}
			
			if (list.size() > 0) {
				JOptionPane.showMessageDialog(this, "로그인 중...\n여기에 프로그레스바?");
				// 메인 페이지로 이동
				mainLayout = new MainLayout(list);
				dispose();
			} else {
				List<Admin> adminList = new AdminDAO().selectAll();
				Boolean exist = false;
				for (Admin admin : adminList)
					if (Integer.toString(admin.getUser().getUser_no()).equals(t_id.getText())) 	// user 정보에 있는 아이디를 입력했을 때
						exist = true;
				if (exist)
					JOptionPane.showMessageDialog(this, "비밀번호를 확인해주세요");
				else
					JOptionPane.showMessageDialog(this, "존재하지 않는 아이디입니다");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
	}

	public static void main(String[] args) {
		new LoginPage();
	}
}
