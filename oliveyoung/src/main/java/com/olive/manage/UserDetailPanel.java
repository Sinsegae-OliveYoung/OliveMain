package com.olive.manage;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.olive.common.config.Config;
import com.olive.common.model.Member;
import com.olive.common.repository.UserDAO;
import com.olive.common.view.Panel;
import com.olive.mainlayout.MainLayout;

public class UserDetailPanel extends Panel{
	
	UserDAO userDAO;
	Member member;
	JPanel p_content;
	JLabel lb_name;
	JLabel lb_user_no;
	JLabel lb_branch;
	JLabel lb_role;
	JLabel lb_tel;
	JLabel lb_email;
	JLabel lb_hiredate;
	JButton bt_change;
	
	public UserDetailPanel(MainLayout mainLayout, Member member) {
		super(mainLayout);
		this.member = member;
		
		p_content = new JPanel();
		lb_name = new JLabel("이름: " + member.getUser().getUser_name());
		lb_user_no = new JLabel("사번: " + member.getUser().getUser_no());
		lb_branch = new JLabel("소속매장: " + member.getBranch().getBr_name());
		lb_role = new JLabel("권한: " + member.getUser().getRole().getRole_name());
		lb_tel = new JLabel("연락처: " + member.getUser().getTel());
		lb_email = new JLabel("이메일: "+ member.getUser().getEmail());
		lb_hiredate = new JLabel("입사일: " + member.getUser().getHiredate());
		bt_change = new JButton("변경");
		
		// 자신보다 높은 직급에 대해서는 변경이 뜨지 않도록(팀장이 1, 스태프가 3)  
		if(mainLayout.user.getRole().getRole_id() > member.getUser().getRole().getRole_id()) {
			bt_change.setVisible(false);
		} else {
			bt_change.setVisible(true);
		}
		
		
		p_content.add(lb_name);
		p_content.add(lb_user_no);
		p_content.add(lb_branch);
		p_content.add(lb_role);
		p_content.add(bt_change);
		p_content.add(lb_tel);
		p_content.add(lb_email);
		p_content.add(lb_hiredate);
		add(p_content);
		
		bt_change.addActionListener(e -> {
			// 권한 변경 다이얼로그 띄우기
			new RoleChangeDialog(this);		
			
		});
		
		setSize(Config.CONTENT_W, Config.CONTENT_H);
		setVisible(true);
	}
	

}
