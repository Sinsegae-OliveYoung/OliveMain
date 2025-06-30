package com.olive.manage.user;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.olive.common.config.Config;
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
	
	JLabel lb_name;
	JLabel lb_no;
	JLabel lb_br;
	JPanel p_role;
	JLabel lb_role;
	JLabel lb_tel;
	JLabel lb_email;
	JLabel lb_hiredate;
	
	JButton bt_change;
	
	public UserDetailPanel(MainLayout mainLayout, String title, ManagePage managePage) {
		super(mainLayout, title, managePage);
		super.setButtonVisible(true);
	}
	
	public void setMember(Member member) {
		this.member = member;

		lb_name.setText(member.getUser().getUser_name());
		lb_no.setText(Integer.toString(member.getUser().getUser_no()));
		lb_br.setText(member.getBranch().getBr_name());
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
		wrapper.setBackground(Config.WHITE);
		
		JPanel p_content = new JPanel(new FlowLayout(FlowLayout.CENTER)); 
		p_content.setPreferredSize(new Dimension(700, 500));
		
		p_content.setBackground(Config.WHITE);
		p_content.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		p_content.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 20)); // top, left, bottom, right
		
		// 부모인 BasePanel의 생성자에서 createContent가 호출되므로, 자식 생성자에서 JLabel 인스턴스를 생성하면 
		// createContent에서 사용하는 lb_name 등은 아직 인스턴스 할당이 안되어 있어서 NPE가 발생한다. 
		p_content.add(createRow("이름", lb_name = new JLabel()));
		p_content.add(createRow("사원번호", lb_no = new JLabel()));
		p_content.add(createRow("소속매장", lb_br = new JLabel()));
		p_content.add(createRow("직급", lb_role = new JLabel()));
		p_content.add(createRow("연락처", lb_tel = new JLabel()));
		p_content.add(createRow("이메일", lb_email = new JLabel()));
		p_content.add(createRow("입사일", lb_hiredate = new JLabel()));
		wrapper.add(p_content);
		
		JPanel p = new JPanel();
		p.setBackground(Config.WHITE);
		p.setPreferredSize(new Dimension(700, 200));
		p.setBorder(BorderFactory.createEmptyBorder(0, 50, 50, 0)); // top, left, bottom, right
		wrapper.add(p);
		
		bt_change = ButtonUtil.greenButtonUtil("수정");
		bt_change.addActionListener(e -> {
			new UserUpdateDialog(this);
			
		});
		p.add(bt_change);
		
		return wrapper;
	}
	
	public JPanel createRow(String title, JComponent field) {
		
		JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		p.setBackground(Config.WHITE);
		p.setPreferredSize(new Dimension(460, 60));
		p.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));
				
		JLabel lb = new JLabel(title);
		lb.setFont(ManageConfig.PLAIN_FONT);
		lb.setPreferredSize(new Dimension(100, 60));
		p.add(lb);   
		
		field.setFont(ManageConfig.BOLD_FONT);
		field.setPreferredSize(new Dimension(300, 60));
		p.add(field);
		
		return p;		
	}
	
	public void refresh() {
		//초기에 mainlayout을 생성할 때, 관리 페이지가 마지막에 생성되기 때문에 다른 페이지에서 refreshDirty()를 호출하면 
		// member가 생성이 안돼있기 때문에 null 처리 
		if(this.member != null) {
			setMember(member); 
		}
	}

}
