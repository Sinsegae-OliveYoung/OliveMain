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
import com.olive.common.view.Page;
import com.olive.common.view.Panel;
import com.olive.mainlayout.MainLayout;
import com.olive.store.report.view.ReportProductMenu;
import com.olive.store.report.view.ReportStoreMenu;
import com.olive.store.report.view.ReportTotalMenu;
import com.olive.store.storeconfig.view.StoreConfigMenu;
import com.olive.store.stores.view.StoreBongMenu;
import com.olive.store.stores.view.StoreIncMenu;
import com.olive.store.stores.view.StoreSamMenu;
import com.olive.store.stores.view.StoreSinMenu;


public class StorePage extends Page {
	
	JPanel p_side;			// 사이드 바
	
	JButton mn_store_config;		// 지점 관리
	JButton mn_store_sin;			// 신세계점
	JButton mn_store_inc;			// 아이앤씨점
	JButton mn_store_sam;		// 삼성점
	JButton mn_store_bong;		// 봉은사점

	JLabel mn_report;				// 보고서
	JButton mn_report_total;		// 기간별 총 매출
	JButton mn_report_product;	// 상품별 매출
	JButton mn_report_store;		// 지점별 매출
	
	JPanel p_content;
	
	Panel[] panels;			// 하위 메뉴 패널들을 담을 배열
	
	public StorePage(MainLayout mainLayout) {
		super(mainLayout);
		setLayout(new BorderLayout());
		
		// create
		p_side = new JPanel();
		
		mn_store_config = new JButton("지점 관리");
		mn_store_sin = new JButton("  신세계점");
		mn_store_inc = new JButton("  아이앤씨점");
		mn_store_sam = new JButton("  삼성점");
		mn_store_bong = new JButton("  봉은사점");
		
		mn_report = new JLabel("보고서");
		mn_report_total = new JButton("  기간별 총 매출");
		mn_report_product	 = new JButton("  상품별 매출");
		mn_report_store	 = new JButton("  지점별 매출");
		
		p_content = new JPanel();
		
		// style
		p_side.setBackground(Config.LIGHT_GRAY);
		p_side.setLayout(new BoxLayout(p_side, BoxLayout.Y_AXIS));
		p_side.setBorder(BorderFactory.createEmptyBorder(0,27,0,0));
		p_side.setPreferredSize(new Dimension(Config.SIDE_W, Config.SIDE_H));
		
		mn_store_config.setFont(new Font("Noto Sans KR", Font.BOLD, 18));
		mn_store_config.setBackground(Config.LIGHT_GRAY);
		mn_store_config.setFocusPainted(false);
		mn_store_config.setBorder(null);
		
		mn_store_sin.setFont(new Font("Noto Sans KR", Font.BOLD, 14));
		mn_store_sin.setBackground(Config.LIGHT_GRAY);
		mn_store_sin.setFocusPainted(false);
		mn_store_sin.setBorder(null);

		mn_store_inc.setFont(new Font("Noto Sans KR", Font.BOLD, 14));
		mn_store_inc.setBackground(Config.LIGHT_GRAY);
		mn_store_inc.setFocusPainted(false);
		mn_store_inc.setBorder(null);
		
		mn_store_sam.setFont(new Font("Noto Sans KR", Font.BOLD, 14));
		mn_store_sam.setBackground(Config.LIGHT_GRAY);
		mn_store_sam.setFocusPainted(false);
		mn_store_sam.setBorder(null);
		
		mn_store_bong.setFont(new Font("Noto Sans KR", Font.BOLD, 14));
		mn_store_bong.setBackground(Config.LIGHT_GRAY);
		mn_store_bong.setFocusPainted(false);
		mn_store_bong.setBorder(null);

		mn_report.setFont(new Font("Noto Sans KR", Font.BOLD, 18));
		
		mn_report_total.setFont(new Font("Noto Sans KR", Font.BOLD, 14));
		mn_report_total.setBackground(Config.LIGHT_GRAY);
		mn_report_total.setFocusPainted(false);
		mn_report_total.setBorder(null);
		
		mn_report_product.setFont(new Font("Noto Sans KR", Font.BOLD, 14));
		mn_report_product.setBackground(Config.LIGHT_GRAY);
		mn_report_product.setFocusPainted(false);
		mn_report_product.setBorder(null);
		
		mn_report_store.setFont(new Font("Noto Sans KR", Font.BOLD, 14));
		mn_report_store.setBackground(Config.LIGHT_GRAY);
		mn_report_store.setFocusPainted(false);
		mn_report_store.setBorder(null);
				
		p_content.setBackground(Config.WHITE);
		p_content.setPreferredSize(new Dimension(Config.CONTENT_W, Config.CONTENT_H));
		
		// assemble

		p_side.add(Box.createVerticalStrut(25));
		p_side.add(mn_store_config);	
		p_side.add(Box.createVerticalStrut(15));
		p_side.add(mn_store_sin);	
		p_side.add(Box.createVerticalStrut(10));
		p_side.add(mn_store_inc);
		p_side.add(Box.createVerticalStrut(10));
		p_side.add(mn_store_sam);	
		p_side.add(Box.createVerticalStrut(10));
		p_side.add(mn_store_bong);
		p_side.add(Box.createVerticalStrut(25));
		p_side.add(mn_report);	
		p_side.add(Box.createVerticalStrut(15));
		p_side.add(mn_report_total);	
		p_side.add(Box.createVerticalStrut(10));
		p_side.add(mn_report_product);	
		p_side.add(Box.createVerticalStrut(10));
		p_side.add(mn_report_store);	
		add(p_side, BorderLayout.WEST);
		
		add(p_content, BorderLayout.CENTER);
		

		// listener
		for (JButton btn : new JButton[] { mn_store_config, mn_store_sin, mn_store_inc, mn_store_sam, mn_store_bong, mn_report_total, mn_report_product, mn_report_store } ) {
			btn.addMouseListener(new MouseAdapter() {
				public void mouseEntered(MouseEvent e) {btn.setForeground(Config.DARK_GREEN);}
				public void mouseExited(MouseEvent e) {btn.setForeground(Color.BLACK);}
				public void mouseClicked(MouseEvent e) {
				      JButton source = (JButton) e.getSource();
				     if (source == mn_store_config)
				    	  showPanel(0);
				      else if (source == mn_store_sin)
				    	  showPanel(1);
				      else if (source == mn_store_inc)
				    	  showPanel(2);
				      else if (source == mn_store_sam)
				    	  showPanel(3);
				      else if (source == mn_store_bong)
				    	  showPanel(4);
				      else if (source == mn_report_total)
				    	  showPanel(5);
				      else if (source == mn_report_product)
				    	  showPanel(6);
				      else if (source == mn_report_store)
				    	  showPanel(7);
				}
			});
		}
		
		createPanel();
		
		setPreferredSize(new Dimension(Config.LAYOUT_W, Config.CONTENT_H));
	}

	public void createPanel() {
		
		panels = new Panel[8];

		panels[0] = new StoreConfigMenu(this);
		panels[1] = new StoreSinMenu(this);
		panels[2] = new StoreIncMenu(this);
		panels[3] = new StoreSamMenu(this);
		panels[4] = new StoreBongMenu(this);
		panels[5] = new ReportTotalMenu(this);
		panels[6] = new ReportProductMenu(this);
		panels[7] = new ReportStoreMenu(this);
		
		for (int i = 0; i < panels.length; i++)
			p_content.add(panels[i]);
	}
	
	public void showPanel(int target) {
			for (int i = 0; i < panels.length; i++)
				panels[i].setVisible((i == target) ? true : false);
	}
	
}
