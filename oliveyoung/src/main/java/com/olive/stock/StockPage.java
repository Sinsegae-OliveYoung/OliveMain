package com.olive.stock;

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
import com.olive.stock.alert.view.CountAlertPanel;
import com.olive.stock.alert.view.OldAlertPanel;
import com.olive.stock.history.view.StockFiltPanel;
import com.olive.stock.history.view.StockIBPanel;
import com.olive.stock.history.view.StockOBPanel;
import com.olive.stock.list.view.StockCatPanel;
import com.olive.stock.list.view.StockNowPanel;
import com.olive.stock.update.view.StockUpdatePanel;


public class StockPage extends Page {
	
	JPanel p_side;			// 사이드 바
	
	JLabel la_menu1;		// 상위 메뉴 A
	JButton bt_menu1;	// 상위 메뉴 A - 하위 메뉴1
	JButton bt_menu2;	// 상위 메뉴 A - 하위 메뉴2

	JLabel la_menu2;	
	JButton bt_menu3;	
	
	JLabel la_menu3;	
	JButton bt_menu4;	
	JButton bt_menu5;	
	JButton bt_menu6;	
	
	JLabel la_menu4;	
	JButton bt_menu7;	
	JButton bt_menu8;	
	
	JPanel p_content;
	
	StockPanel[] panels;			// 하위 메뉴 패널들을 담을 배열

	private boolean isDataDirty = false;

	public void setDataDirty(boolean isDataDirty) {
		this.isDataDirty = isDataDirty;
	}

	public StockPage(MainLayout mainLayout) {
		super(mainLayout);
		setLayout(new BorderLayout());
		
		// create
		p_side = new JPanel();
		
		la_menu1 = new JLabel("재고 조회");
		bt_menu1 = new JButton("현재 수량 확인");
		bt_menu2 = new JButton("카테고리별 확인");
		
		la_menu2 = new JLabel("재고 수정");
		bt_menu3 = new JButton("수동 수량 재고 조정");
		
		la_menu3 = new JLabel("재고 이력 관리");	
		bt_menu4 = new JButton("재고 입고 기록");	
		bt_menu5 = new JButton("재고 출고 기록");	
		bt_menu6 = new JButton("사용자/시간대 필터");	
		
		la_menu4 = new JLabel("안전 재고 알림");	
		bt_menu7 = new JButton("재고 수량 부족");	
		bt_menu8 = new JButton("오래된 재고 알림");	
		
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
		
		la_menu3.setFont(new Font("Noto Sans KR", Font.BOLD, 18));
		
		bt_menu4.setFont(new Font("Noto Sans KR", Font.BOLD, 14));
		bt_menu4.setBackground(Config.LIGHT_GRAY);
		bt_menu4.setFocusPainted(false);
		bt_menu4.setBorder(null);
		
		bt_menu5.setFont(new Font("Noto Sans KR", Font.BOLD, 14));
		bt_menu5.setBackground(Config.LIGHT_GRAY);
		bt_menu5.setFocusPainted(false);
		bt_menu5.setBorder(null);
		
		bt_menu6.setFont(new Font("Noto Sans KR", Font.BOLD, 14));
		bt_menu6.setBackground(Config.LIGHT_GRAY);
		bt_menu6.setFocusPainted(false);
		bt_menu6.setBorder(null);
		
		la_menu4.setFont(new Font("Noto Sans KR", Font.BOLD, 18));
		
		bt_menu7.setFont(new Font("Noto Sans KR", Font.BOLD, 14));
		bt_menu7.setBackground(Config.LIGHT_GRAY);
		bt_menu7.setFocusPainted(false);
		bt_menu7.setBorder(null);
		
		bt_menu8.setFont(new Font("Noto Sans KR", Font.BOLD, 14));
		bt_menu8.setBackground(Config.LIGHT_GRAY);
		bt_menu8.setFocusPainted(false);
		bt_menu8.setBorder(null);
				
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
		p_side.add(Box.createVerticalStrut(25));
		p_side.add(la_menu3);	
		p_side.add(Box.createVerticalStrut(15));
		p_side.add(bt_menu4);	
		p_side.add(Box.createVerticalStrut(10));
		p_side.add(bt_menu5);
		p_side.add(Box.createVerticalStrut(10));
		p_side.add(bt_menu6);	
		p_side.add(Box.createVerticalStrut(25));
		p_side.add(la_menu4);	
		p_side.add(Box.createVerticalStrut(15));
		p_side.add(bt_menu7);	
		p_side.add(Box.createVerticalStrut(10));
		p_side.add(bt_menu8);
		add(p_side, BorderLayout.WEST);
		
		add(p_content, BorderLayout.CENTER);
		

		// listener
		for (JButton btn : new JButton[] { bt_menu1, bt_menu2, bt_menu3, bt_menu4, bt_menu5, bt_menu6, bt_menu7, bt_menu8, } ) {
			btn.addMouseListener(new MouseAdapter() {
				public void mouseEntered(MouseEvent e) {btn.setForeground(Config.DARK_GREEN);}
				public void mouseExited(MouseEvent e) {btn.setForeground(Color.BLACK);}
				public void mouseClicked(MouseEvent e) {
				      JButton source = (JButton) e.getSource();
				      
				      if (source == bt_menu1)
				    	  showPanel(0);
				      if (source == bt_menu2)
				    	  showPanel(1);
				      if (source == bt_menu3)
				    	  showPanel(2);
				      if (source == bt_menu4)
				    	  showPanel(3);
				      if (source == bt_menu5)
				    	  showPanel(4);
				      if (source == bt_menu6)
				    	  showPanel(5);
				      if (source == bt_menu7)
				    	  showPanel(6);
				      if (source == bt_menu8)
				    	  showPanel(7);
				}
			});
		}
		
		createPanel();
		showPanel(-1);
		
		setPreferredSize(new Dimension(Config.LAYOUT_W, Config.CONTENT_H));
	}

	public void createPanel() {
		
		panels = new StockPanel[8];

		panels[0] = new StockNowPanel(mainLayout);
		panels[1] = new StockCatPanel(mainLayout);
		panels[2] = new StockUpdatePanel(mainLayout);
		panels[3] = new StockIBPanel(mainLayout);
		panels[4] = new StockOBPanel(mainLayout);
		panels[5] = new StockFiltPanel(mainLayout);
		panels[6] = new CountAlertPanel(mainLayout);
		panels[7] = new OldAlertPanel(mainLayout);
		
		for(int i=0; i<panels.length; i++) {
			p_content.add(panels[i]);
		}
	}
	
	public void showPanel(int target) {
		 if (isDataDirty) {
		        for (StockPanel panel : panels) {
		            panel.refresh(); // refresh()가 오버라이딩된 패널만 동작
		          
		        }
		        isDataDirty = false;
		    }
		
		for (int i = 0; i < panels.length; i++)
			panels[i].setVisible((i == target) ? true : false);
	}
	
}
