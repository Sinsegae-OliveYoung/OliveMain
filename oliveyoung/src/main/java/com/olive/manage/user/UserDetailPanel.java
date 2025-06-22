package com.olive.manage.user;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.olive.common.config.Config;
import com.olive.common.model.Member;
import com.olive.common.repository.UserDAO;
import com.olive.mainlayout.MainLayout;
import com.olive.manage.BasePanel;

public class UserDetailPanel extends BasePanel{
	
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
	
	public UserDetailPanel(MainLayout mainLayout, String title) {
		super(mainLayout, title);
		this.member = member;
		super.setButtonVisible(true);
		
	}
	
	public void setMember(Member member) {
		this.member = member;

		lb_name.setText("이름: " + member.getUser().getUser_name());
		lb_user_no.setText("사번: " + member.getUser().getUser_no());
		lb_branch.setText("소속매장: " + member.getBranch().getBr_name());
		lb_role.setText("권한: " + member.getUser().getRole().getRole_name());
		lb_tel.setText("연락처: " + member.getUser().getTel());
		lb_email.setText("이메일: " + member.getUser().getEmail());
		lb_hiredate.setText("입사일: " + member.getUser().getHiredate());

		// 권한에 따라 버튼 표시 제어
		if (mainLayout.user.getRole().getRole_id() > member.getUser().getRole().getRole_id()) {
			bt_change.setVisible(false);
		} else {
			bt_change.setVisible(true);
		}
	}
	

	@Override
	public JPanel createContent() { 
		p_content = new JPanel();
		
		lb_name = new JLabel("이름");
		lb_user_no = new JLabel("사번");
		lb_branch = new JLabel("소속매장");
		lb_role = new JLabel("직급");
		lb_tel = new JLabel("연락처");
		lb_email = new JLabel("이메일");
		lb_hiredate = new JLabel("입사일");
		bt_change = new JButton("변경");
	
		p_content.add(lb_name);
		p_content.add(lb_user_no);
		p_content.add(lb_branch);
		p_content.add(lb_role);
		p_content.add(bt_change);
		p_content.add(lb_tel);
		p_content.add(lb_email);
		p_content.add(lb_hiredate);

		bt_change.addActionListener(e -> {
			// 권한 변경 다이얼로그 띄우기
			new RoleChangeDialog(this);		
			
		});
		
		return p_content;
	}
	

}
