package com.olive.manage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.olive.common.config.Config;
import com.olive.common.view.Page;
import com.olive.common.view.Panel;
import com.olive.mainlayout.MainLayout;

//슬라이드 기능 (접었다 폈다)
public class ManagePage extends Page{
	
	JPanel p_side;
	JLabel la_user;
	JButton bt_user_list;
	JLabel la_approval;
	JButton bt_approval_list;
	JPanel p_content;
	Panel[] panels;

	public ManagePage(MainLayout mainLayout) {
		super(mainLayout);
		setLayout(new BorderLayout());
		
		// create
		p_side = new JPanel();
		la_user = new JLabel("사용자 관리");
		bt_user_list = new JButton("  사용자 목록");
		la_approval = new JLabel("결재 관리");
		bt_approval_list = new JButton("  결재 목록");
		
		p_content = new JPanel();
		
		//style
		Font topFont = new Font("Noto Sans KR", Font.BOLD, 18);
		Font subFont = new Font("Noto Sans KR", Font.BOLD, 14);
		
		p_side.setBackground(Config.LIGHT_GRAY);
		p_side.setLayout(new BoxLayout(p_side, BoxLayout.Y_AXIS));
		p_side.setBorder(BorderFactory.createEmptyBorder(0,27,0,0));
		p_side.setPreferredSize(new Dimension(Config.SIDE_W, Config.SIDE_H));
		
		la_user.setFont(topFont);
		bt_user_list.setFont(subFont);
		bt_user_list.setBackground(Config.LIGHT_GRAY);
		bt_user_list.setFocusPainted(false);
		bt_user_list.setBorder(null);
		
		la_approval.setFont(topFont);
		bt_approval_list.setFont(subFont);
		bt_approval_list.setBackground(Config.LIGHT_GRAY);
		bt_approval_list.setFocusPainted(false);
		bt_approval_list.setBorder(null);

		p_content.setBackground(Config.WHITE);
		p_content.setPreferredSize(new Dimension(Config.CONTENT_W, Config.CONTENT_H));
		
		p_side.add(Box.createVerticalStrut(25));
		p_side.add(la_user);	
		p_side.add(Box.createVerticalStrut(15));
		p_side.add(bt_user_list);	
		
		p_side.add(Box.createVerticalStrut(25));
		p_side.add(la_approval);	
		p_side.add(Box.createVerticalStrut(15));
		p_side.add(bt_approval_list);
		
		add(p_side, BorderLayout.WEST);
		add(p_content, BorderLayout.CENTER);
	
		// listener
		for (JButton btn : new JButton[] { bt_user_list, bt_approval_list} ) {
			btn.addMouseListener(new MouseAdapter() {
				public void mouseEntered(MouseEvent e) {btn.setForeground(Config.DARK_GREEN);}
				public void mouseExited(MouseEvent e) {btn.setForeground(Color.BLACK);}
				public void mouseClicked(MouseEvent e) {
				      JButton source = (JButton) e.getSource();
				      
				     /* if (source == bt_menu1)
				    	  showPanel(0);
				      else if (source == bt_menu2)
				    	  showPanel(1);
				      else if (source == bt_menu3)
				    	  showPanel(2);
				      else if (source == bt_menu4)
				    	  showPanel(3);*/
				      
				      /*--------------
				       *  테스트용
				       * -------------*/
				      if (source == bt_user_list)
				    	  showPanel(0);
				}
			});
		}
	
		createPanel();
		showPanel(-1);
	}
	
	public void createPanel() {
		
		//panels = new UserListPanel[1];
		
		//panels[0] = new UserListPanel(mainLayout);
		//panels[1] = new Menu2Panel(this);
		//panels[2] = new Menu3Panel(this);
		//panels[3] = new Menu4Panel(this);
		
		//for (int i = 0; i < panels.length; i++)
		//p_content.add(panels[0]);
	}
	
	public void showPanel(int target) {
		for (int i = 0; i < panels.length; i++) {
			panels[i].setVisible((i == target) ? true : false);
		}
		
	}
	
}
