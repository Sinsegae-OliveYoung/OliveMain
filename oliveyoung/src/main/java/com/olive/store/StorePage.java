package com.olive.store;

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
import com.olive.common.view.Menu1Panel;
import com.olive.common.view.Page;
import com.olive.common.view.Panel;
import com.olive.mainlayout.MainLayout;


public class StorePage extends Page {
	
	JPanel p_side;			// 사이드 바
	
	JLabel la_menu1;		// 상위 메뉴 A
	JButton bt_menu1;	// 상위 메뉴 A - 하위 메뉴1
	JButton bt_menu2;	// 상위 메뉴 A - 하위 메뉴2

	JLabel la_menu2;		// 상위 메뉴 B
	JButton bt_menu3;	// 상위 메뉴 B - 하위 메뉴1
	JButton bt_menu4;	// 상위 메뉴 B - 하위 메뉴2
	
	JPanel p_content;
	
	Panel[] panels;			// 하위 메뉴 패널들을 담을 배열
	
	public StorePage(MainLayout mainLayout) {
		super(mainLayout);
		setLayout(new BorderLayout());
		
		// create
		p_side = new JPanel();
		
		la_menu1 = new JLabel("상위 메뉴 A");
		bt_menu1 = new JButton("  하위 메뉴1");
		bt_menu2 = new JButton("  하위 메뉴2");
		
		la_menu2 = new JLabel("상위 메뉴 B");
		bt_menu3 = new JButton("  하위 메뉴1");
		bt_menu4	 = new JButton("  하위 메뉴2");
		
		p_content = new JPanel();
		
		// style
		p_side.setBackground(Config.LIGHT_GRAY);
		p_side.setLayout(new BoxLayout(p_side, BoxLayout.Y_AXIS));
		p_side.setBorder(BorderFactory.createEmptyBorder(0,27,0,0));
		p_side.setPreferredSize(new Dimension(Config.SIDE_W, Config.SIDE_H));
		
		la_menu1.setFont(new Font("Noto Sans KR", Font.BOLD, 18));
		
		bt_menu1.setFont(new Font("Noto Sans KR", Font.BOLD, 14));
		bt_menu1.setBackground(Config.LIGHT_GRAY);
		bt_menu1.setFocusPainted(false);
		bt_menu1.setBorder(null);
		
		bt_menu2.setFont(new Font("Noto Sans KR", Font.BOLD, 14));
		bt_menu2.setBackground(Config.LIGHT_GRAY);
		bt_menu2.setFocusPainted(false);
		bt_menu2.setBorder(null);

		la_menu2.setFont(new Font("Noto Sans KR", Font.BOLD, 18));
		
		bt_menu3.setFont(new Font("Noto Sans KR", Font.BOLD, 14));
		bt_menu3.setBackground(Config.LIGHT_GRAY);
		bt_menu3.setFocusPainted(false);
		bt_menu3.setBorder(null);
		
		bt_menu4.setFont(new Font("Noto Sans KR", Font.BOLD, 14));
		bt_menu4.setBackground(Config.LIGHT_GRAY);
		bt_menu4.setFocusPainted(false);
		bt_menu4.setBorder(null);
				
		p_content.setBackground(Config.WHITE);
		p_content.setPreferredSize(new Dimension(Config.CONTENT_W, Config.CONTENT_H));
		
		// assemble

		p_side.add(Box.createVerticalStrut(25));
		p_side.add(la_menu1);	
		p_side.add(Box.createVerticalStrut(15));
		p_side.add(bt_menu1);	
		p_side.add(Box.createVerticalStrut(10));
		p_side.add(bt_menu2);
		p_side.add(Box.createVerticalStrut(25));
		p_side.add(la_menu2);	
		p_side.add(Box.createVerticalStrut(15));
		p_side.add(bt_menu3);	
		p_side.add(Box.createVerticalStrut(10));
		p_side.add(bt_menu4);	
		add(p_side, BorderLayout.WEST);
		
		add(p_content, BorderLayout.CENTER);
		

		// listener
		for (JButton btn : new JButton[] { bt_menu1, bt_menu2, bt_menu3, bt_menu4 } ) {
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
				      if (source == bt_menu1)
				    	  showPanel(0);
				}
			});
		}
		
		createPanel();
		showPanel(-1);
		
		setPreferredSize(new Dimension(Config.LAYOUT_W, Config.CONTENT_H));
	}

	public void createPanel() {
		
		panels = new Panel[1];

		panels[0] = new Menu1Panel(this);
		//panels[1] = new Menu2Panel(this);
		//panels[2] = new Menu3Panel(this);
		//panels[3] = new Menu4Panel(this);
		
		//for (int i = 0; i < panels.length; i++)
			p_content.add(panels[0]);
	}
	
	public void showPanel(int target) {
			for (int i = 0; i < panels.length; i++)
				panels[i].setVisible((i == target) ? true : false);
	}
	
}
