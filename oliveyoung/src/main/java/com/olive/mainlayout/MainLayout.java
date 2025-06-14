package com.olive.mainlayout;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.olive.common.config.Config;
import com.olive.common.view.Page;
import com.olive.stock.StockPage;
import com.olive.store.StorePage;


/*----------------------------------------------
 * 
 *  이것은 빨아쓰는 액자임니다.
 *  액자 속 당신의 사진은 따로 파일 만들어야 해요!
 *  - 각 페이지명은 createPage() 메서드 보고 참고해주세요
 *  - JFrame 상속 받아야 함
 *  - setPreferredSize(new Dimension(Config.CONTENT_W, Config.CONTENT_H));
 * 
 *---------------------------------------------- */


public class MainLayout extends JFrame {

	JPanel p_navi;

	JPanel p_title;
	ImageIcon img;
	JButton bt_title;

	JPanel p_menu;
	JButton bt_io; // 입출고 in,out
	JButton bt_st; // 재고 stock
	JButton bt_cl; // 일정 calendar
	JButton bt_sh; // 지점 store(shop)
	JButton bt_co; // 관리 config

	JPanel p_my;
	JLabel lb_me; // ooo지점 oo님
	JButton bt_lo; // logout

	JPanel p_content;
	
	Page[] pages;	// 페이지 담을 배열
	
	public MainLayout() {
		// create
		p_navi = new JPanel();

		p_title = new JPanel();
		// Config 가서 이미지 경로 설정해주셔야 합니다
		img = new ImageIcon(new ImageIcon(Config.LOGO_PATH).getImage().getScaledInstance(180, 20, Image.SCALE_SMOOTH));
		bt_title = new JButton(img);

		p_menu = new JPanel();
		bt_io = new JButton("입출고");
		bt_st = new JButton("재고");
		bt_cl = new JButton("일정");
		bt_sh = new JButton("지점");
		bt_co = new JButton("관리");

		p_my = new JPanel();
		lb_me = new JLabel("ooo지점 oo님");
		bt_lo = new JButton("로그아웃");

		p_content = new JPanel();
		
		// style
		p_navi.setBackground(Config.GREEN);
		p_navi.setPreferredSize(new Dimension(Config.NAVI_W, Config.NAVI_H));
		p_navi.setLayout(new BorderLayout());

		p_title.setOpaque(false); // 배경 투명
		p_title.setBorder(BorderFactory.createEmptyBorder(15, 5, 0, 0));
		
		bt_title.setBackground(Config.GREEN);
		bt_title.setFocusPainted(false);
		bt_title.setBorder(null);

		p_menu.setBorder(BorderFactory.createEmptyBorder(13, 0, 0, 0));
		p_menu.setOpaque(false);
		
		bt_io.setFont(new Font("Noto Sans KR", Font.BOLD, 20));
		bt_io.setHorizontalAlignment(JButton.LEFT);
		bt_io.setBackground(Config.GREEN);
		bt_io.setFocusPainted(false);
		bt_io.setBorder(null);

		bt_st.setFont(new Font("Noto Sans KR", Font.BOLD, 20));
		bt_st.setHorizontalAlignment(JButton.LEFT);
		bt_st.setBackground(Config.GREEN);
		bt_st.setFocusPainted(false);
		bt_st.setBorder(null);

		bt_cl.setFont(new Font("Noto Sans KR", Font.BOLD, 20));
		bt_cl.setHorizontalAlignment(JButton.LEFT);
		bt_cl.setBackground(Config.GREEN);
		bt_cl.setFocusPainted(false);
		bt_cl.setBorder(null);

		bt_sh.setFont(new Font("Noto Sans KR", Font.BOLD, 20));
		bt_sh.setHorizontalAlignment(JButton.LEFT);
		bt_sh.setBackground(Config.GREEN);
		bt_sh.setFocusPainted(false);
		bt_sh.setBorder(null);

		bt_co.setFont(new Font("Noto Sans KR", Font.BOLD, 20));
		bt_co.setHorizontalAlignment(JButton.LEFT);
		bt_co.setBackground(Config.GREEN);
		bt_co.setFocusPainted(false);
		bt_co.setBorder(null);

		p_my.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 10));
		p_my.setOpaque(false);

		lb_me.setFont(new Font("Noto Sans KR", Font.BOLD, 15));
		lb_me.setHorizontalAlignment(JLabel.RIGHT);

		bt_lo.setFont(new Font("Noto Sans KR", Font.BOLD, 15));
		bt_lo.setHorizontalAlignment(JButton.RIGHT);
		bt_lo.setBackground(Config.GREEN);
		bt_lo.setFocusPainted(false);
		bt_lo.setBorder(null);
		
		// assemble
		p_title.add(bt_title);
		p_navi.add(p_title, BorderLayout.WEST);
		
		p_menu.add(bt_io);
		p_menu.add(Box.createHorizontalStrut(50));
		p_menu.add(bt_st);
		p_menu.add(Box.createHorizontalStrut(50));
		p_menu.add(bt_cl);
		p_menu.add(Box.createHorizontalStrut(50));
		p_menu.add(bt_sh);
		p_menu.add(Box.createHorizontalStrut(50));
		p_menu.add(bt_co);
		p_navi.add(p_menu);

		p_my.add(lb_me);
		p_my.add(Box.createHorizontalStrut(10));
		p_my.add(bt_lo);
		p_navi.add(p_my, BorderLayout.EAST);

		add(p_navi, BorderLayout.NORTH);
		add(p_content, BorderLayout.CENTER);

		// listener
		for (JButton btn : new JButton[] { bt_title, bt_io, bt_st, bt_cl, bt_sh, bt_co, bt_lo } ) {
			btn.addMouseListener(new MouseAdapter() {
				public void mouseEntered(MouseEvent e) {btn.setForeground(Color.WHITE);}
				public void mouseExited(MouseEvent e) {btn.setForeground(Color.BLACK);}
				public void mouseClicked(MouseEvent e) {
				      JButton source = (JButton) e.getSource();
				      
				      /*--------------
				       *  병합용
				       * -------------*/
				     /* if (source == bt_title)
				    	  showPage(Config.MAIN_PAGE);
				      else if (source == bt_io)
				    	  showPage(Config.BOUND_PAGE);
				      else if (source == bt_st)
				    	  showPage(Config.STOCK_PAGE);
				      else if (source == bt_cl)
				    	  showPage(Config.SCHEDULE_PAGE);
				      else if (source == bt_sh)
				    	  showPage(Config.STORE_PAGE);
				      else if (source == bt_co)
				    	  showPage(Config.CONFIG_PAGE);
				      else if (source == bt_lo)
				    	  showPage(new LoginPage());*/
				      
				      /*--------------
				       *  개인 테스트용
				       * -------------*/
				      if (source == bt_st)
				    	  showPage(0);
				}
			
			});
		}
		createPage();
		showPage(-1);
		
		getContentPane().setBackground(Config.WHITE);
		setSize(Config.LAYOUT_W, Config.LAYOUT_H);
		setVisible(true);
	}
	
	public void createPage() {
		
		/*-------------------------------------------------
		 * 병합용 
		 *------------------------------------------------- */
		/*pages = new Page[6];
		* 
		* pages[Config.MAIN_PAGE] = new MainPage(this);
		* pages[Config.BOUND_PAGE] = new BoundPage(this);
		* pages[Config.STOCK_PAGE] = new StockPage(this);
		* pages[Config.SCHEDULE_PAGE] = new SchedulePage(this);
		* 
		* pages[Config.CONFIG_PAGE] = new ConfigPage(this);
		* 
		* for (int i = 0; i < pages.length; i++)
		* 	p_content.add(pages[i]);
		*/
		

		/*-------------------------------------------------
		 * 개인 테스트용  --> 이거 사용해서 테스트 하심 돼요
		 *------------------------------------------------- */
		pages = new Page[1];
		
		pages[0] = new StockPage(this);
		p_content.add(pages[0]);
	}
	
	public void showPage(int target) {
		// 로그인 검증 추가해야 함
			for (int i = 0; i < pages.length; i++)
				pages[i].setVisible((i == target) ? true : false);
	}
	
	public static void main(String[] args) {
		new MainLayout();
	}
}
