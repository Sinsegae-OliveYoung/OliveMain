package com.olive.manage.user;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.olive.common.model.Role;
import com.olive.common.repository.UserDAO;
import com.olive.common.util.style.ButtonUtil;
import com.olive.common.util.style.ComboBoxUtil;

public class UserRegistDialog extends JDialog{
	UserDetailPanel userDetailPanel;
	UserDAO userDAO = new UserDAO();
	
	public UserRegistDialog(UserDetailPanel userDetailPanel) {		
		
		setLayout(new FlowLayout());
		setSize(400, 600);
		setLocationRelativeTo(null);   //setSize 이후에 위치 지정해야됨 
		
		//배경 패널 : layout null, 절대 위치 사용 
		JPanel panel = new JPanel();	
		//panel.setLayout(null);
		
		//라벨 
		JPanel p_label = new JPanel();
		p_label.setPreferredSize(new Dimension(150, 600));
		p_label.setBackground(Color.blue);
		panel.add(p_label);
		
		Dimension d = new Dimension(150, 40);
		JLabel lb_name = new JLabel("이름");
		lb_name.setPreferredSize(d);
		p_label.add(lb_name);
		
		JLabel lb_br = new JLabel("소속 매장");
		lb_br.setPreferredSize(d);
		p_label.add(lb_br);
		
		JLabel lb_role = new JLabel("직급");
		lb_role.setPreferredSize(d);
		p_label.add(lb_role);
		
		JLabel lb_tel = new JLabel("연락처");
		lb_tel.setPreferredSize(d);
		p_label.add(lb_tel);
		
		JLabel lb_email = new JLabel("이메일");
		lb_email.setPreferredSize(d);
		p_label.add(lb_email);
		
		
		
		
		JLabel label = new JLabel("권한 변경: ");
		label.setBounds(30, 30, 70, 30);
		panel.add(label);
		
		// 직급 콤보박스 
		JComboBox<Role> cb_role = ComboBoxUtil.createRoleComboBox();
		cb_role.setBounds(110, 30, 120, 30);
		panel.add(cb_role);
		
		//저장 버튼
		JButton bt_save = ButtonUtil.greenButtonUtil("저장");
		bt_save.setBounds(100, 90, 80, 30);
		panel.add(bt_save);
		
		setContentPane(panel);
		
		//저장 버튼 이벤트 연결
		bt_save.addActionListener(e -> {
			//db 업데이트 
			Role role = (Role)cb_role.getSelectedItem();
			userDetailPanel.member.getUser().setRole(role);
			userDAO.update(userDetailPanel.member.getUser());
			
			// 알림창 
			JOptionPane.showMessageDialog(bt_save, "변경이 완료되었습니다.");
			userDetailPanel.lb_role.setText(role.getRole_name());
			dispose();
		});
		System.out.println("RoleChangeDialog()");
		setVisible(true);
		
	}
	public static void main(String[] args) {
		new UserRegistDialog(null);
	}
	
}
