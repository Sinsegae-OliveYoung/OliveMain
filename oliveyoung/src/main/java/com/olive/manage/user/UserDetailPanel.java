package com.olive.manage.user;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.olive.common.model.Member;
import com.olive.common.repository.UserDAO;
import com.olive.common.util.style.ButtonUtil;
import com.olive.mainlayout.MainLayout;
import com.olive.manage.BasePanel;
import com.olive.manage.ManageConfig;
import com.olive.manage.ManagePage;

public class UserDetailPanel extends BasePanel{
	
	UserDAO userDAO;
	Member member;
	JPanel p_content;
	
	JPanel[] p_item;
	
	JLabel lb_name;
	JLabel lb_user_no;
	JLabel lb_branch;
	
	JPanel p_role;
	JLabel lb_role;
	JButton bt_change;
	
	JLabel lb_tel;
	JLabel lb_email;
	JLabel lb_hiredate;
	
	public UserDetailPanel(MainLayout mainLayout, String title, ManagePage managePage) {
		super(mainLayout, title, managePage);
		super.setButtonVisible(true);
	}
	
	public void setMember(Member member) {
		this.member = member;

		lb_name.setText(member.getUser().getUser_name());
		lb_user_no.setText(Integer.toString(member.getUser().getUser_no()));
		lb_branch.setText(member.getBranch().getBr_name());
		lb_role.setText(member.getUser().getRole().getRole_name());
		lb_tel.setText(member.getUser().getTel());
		lb_email.setText(member.getUser().getEmail());
		lb_hiredate.setText(member.getUser().getHiredate().toString());

		// 권한에 따라 버튼 표시 제어
		if (mainLayout.user.getRole().getRole_id() > member.getUser().getRole().getRole_id()) {
			bt_change.setVisible(false);
		} else {
			bt_change.setVisible(true);
		}
	}
	

	@Override
	public JPanel createContent() {
		
		JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 30)); // 중앙 배치
		wrapper.setPreferredSize(new Dimension(700, 500)); 
		wrapper.setBackground(Color.white);
		
		p_content = new JPanel(new FlowLayout(FlowLayout.CENTER)); 
		p_content.setPreferredSize(new Dimension(500, 450));
		
		p_content.setBackground(Color.white);
		p_content.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		p_content.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 20)); // top, left, bottom, right
		wrapper.add(p_content);
		
		p_item = new JPanel[7];
		
		String[] column = {"이름", "사번", "소속매장", "직급", "연락처", "이메일", "입사일"};
		JLabel[] lbs = {lb_name, lb_user_no, lb_branch, lb_role, lb_tel, lb_email, lb_hiredate};
		
		for(int i = 0; i < p_item.length; i++) {
			lbs[i] = new JLabel();
			lbs[i].setFont(ManageConfig.BOLD_FONT);
			lbs[i].setPreferredSize(new Dimension(300, 60));
			
			p_item[i] = new JPanel(new FlowLayout(FlowLayout.LEFT));
			p_item[i].setPreferredSize(new Dimension(460, 60));
			p_item[i].setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));
			p_item[i].setBackground(Color.white);
			
			
			JLabel lb = new JLabel(column[i]);
			lb.setFont(ManageConfig.PLAIN_FONT);
			lb.setPreferredSize(new Dimension(100, 60));
			p_item[i].add(lb);
			if(i == 3) {
				lb_role = new JLabel("");
				lb_role = lbs[3];
				lb_role.setPreferredSize(new Dimension(100, 60));
				bt_change = ButtonUtil.createDefaultButton("변경");
				p_item[3].add(lb_role);
				p_item[3].add(bt_change);
				
				p_content.add(p_item[3]);
			}
			else {
				//권한은 p_role을 추가해야함
				p_item[i].add(lbs[i]);
				p_content.add(p_item[i]);
			}
		}

		lb_name = lbs[0];
		lb_user_no = lbs[1];
		lb_branch = lbs[2];
		lb_tel = lbs[4];
		lb_email = lbs[5];
		lb_hiredate = lbs[6];
		


		bt_change.addActionListener(e -> {
			// 권한 변경 다이얼로그 띄우기
			new RoleChangeDialog(this);		
		});
		
		return wrapper;
	}
	

}
