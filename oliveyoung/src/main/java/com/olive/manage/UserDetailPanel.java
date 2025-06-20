package com.olive.manage;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.olive.common.config.Config;
import com.olive.common.repository.UserDAO;

public class UserDetailPanel extends JFrame{
	
	UserDAO userDAO;
	
	JPanel p_content;
	JLabel lb_name;
	JLabel lb_user_no;
	JLabel lb_branch;
	JLabel lb_role;
	JLabel lb_tel;
	JLabel lb_email;
	JLabel lb_hiredate;
	JButton bt_change;
	
	public UserDetailPanel() {
		
		userDAO = new UserDAO();
		
		
		p_content = new JPanel();
		lb_name = new JLabel("이름: ");
		lb_user_no = new JLabel("사번: ");
		lb_branch = new JLabel("소속매장: ");
		lb_role = new JLabel("권한: ");
		lb_tel = new JLabel("연락처: ");
		lb_email = new JLabel("이메일: ");
		lb_hiredate = new JLabel();
		bt_change = new JButton("변경");
		
		
		add(p_content);
		p_content.add(lb_name);
		p_content.add(lb_user_no);
		p_content.add(lb_branch);
		p_content.add(lb_role);
		p_content.add(bt_change);
		p_content.add(lb_tel);
		p_content.add(lb_email);
		p_content.add(lb_hiredate);
		
		
		
		
		setSize(Config.CONTENT_W, Config.CONTENT_H);
		setVisible(true);
	}
	
	
	public static void main(String[] args) {
		new UserDetailPanel(); 
		
	}

}
