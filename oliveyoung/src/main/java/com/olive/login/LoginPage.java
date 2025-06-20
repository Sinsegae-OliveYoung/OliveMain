package com.olive.login;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.Image;
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
import com.olive.common.model.User;
import com.olive.common.repository.UserDAO;
import com.olive.common.util.DBManager;
import com.olive.common.util.ImageUtil;
<<<<<<< HEAD
=======
import com.olive.login.security.model.Admin;
>>>>>>> develop
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
	UserDAO dao = new UserDAO();
	public User user;

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
		t_pwd.addActionListener(e -> nullCheck());
		bt_login.addActionListener(e -> nullCheck());

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

	// 로그인 정보가 맞는지 확인하는 메서드
	public void loginCheck() {
		// 모든 유저 정보에 입력받은 아이디와 패스워드 대입, 해당되는 유저 추출
		user = dao.checkLogin(Integer.parseInt(t_id.getText()), new String(t_pwd.getPassword()));
		
		// 해당되는 유저가 있다면
		if (user != null) {
			JOptionPane.showMessageDialog(this, "로그인 중...\n여기에 프로그레스바?");
			mainLayout = new MainLayout(user); // 메인 페이지로 유저 정보를 갖고 이동
			dispose(); // 현재 창 닫기
			// 해당되는 유저가 없다면
		} else {
			List<User> userList = dao.selectAll();
			Boolean exist = false; // 아이디 존재 여부를 결정 짓는 변수
			// 모든 유저 정보를 가져와서 비교
			for (User users : userList)
				// 해당하는 아이디가 있다면
				if (Integer.toString(users.getUser_no()).equals(t_id.getText()))
					exist = true; // 아이디 존재 여부 변수 true값으로 변경
			// 아이디가 존재하면
			if (exist)
				JOptionPane.showMessageDialog(this, "비밀번호를 확인해주세요");	// 비밀번호 여부만 묻기
			// 아이디가 존재하지 않으면
			else
				JOptionPane.showMessageDialog(this, "존재하지 않는 아이디입니다");	// 아이디 없다고 반환
		}
	}

	public static void main(String[] args) {
		new LoginPage();
	}
}
