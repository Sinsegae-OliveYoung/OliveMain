package com.olive.mainlayout;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.olive.bound.BoundPage;
import com.olive.common.config.Config;
import com.olive.common.model.Role;
import com.olive.common.model.Stock;
import com.olive.common.model.User;
import com.olive.common.repository.BranchDAO;
import com.olive.common.repository.StockDAO;
import com.olive.common.util.ImageUtil;
import com.olive.common.view.MainPage;
import com.olive.common.view.Page;
import com.olive.login.LoginPage;
import com.olive.manage.ManagePage;
import com.olive.product.ProductPage;
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
	Image img;
	ImageUtil img_title = new ImageUtil();
	JButton bt_title;

	JPanel p_menu;
	JButton bt_pd; // 상품 product
	JButton bt_io; // 입출고 in,out
	JButton bt_st; // 재고 stock
	JButton bt_cl; // 일정 calendar
	JButton bt_sh; // 지점 store(shop)
	JButton bt_ma; // 관리 manage

	JPanel p_my;
	JLabel lb_me; // ooo지점 oo님
	JButton bt_lo; // logout

	JPanel p_content;

	Page[] pages; // 페이지 담을 배열
	
	private boolean isDataDirty = false;
    private boolean running = true;

	public User user;
	BranchDAO branchDAO;

	public MainLayout(User user) {
		this.user = user;
		
		branchDAO = new BranchDAO();

		// create
		p_navi = new JPanel();

		p_title = new JPanel();
		img = img_title.getImage("images/logo2.png", 180, 20);
		bt_title = new JButton() {
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(img, 0, 0, 180, 20, bt_title);
			}
		};

		p_menu = new JPanel();
		bt_pd = new JButton("상품");
		bt_io = new JButton("입출고");
		bt_st = new JButton("재고");
		bt_cl = new JButton("일정");
		bt_sh = new JButton("지점");
		bt_ma = new JButton("관리");

		p_my = new JPanel();

		lb_me = new JLabel(setProfile());
		bt_lo = new JButton("로그아웃");

		p_content = new JPanel();

		// style
		p_navi.setBackground(Config.GREEN);
		p_navi.setPreferredSize(new Dimension(Config.NAVI_W, Config.NAVI_H));
		p_navi.setLayout(new BorderLayout());

		p_title.setOpaque(false); // 배경 투명
		p_title.setBorder(BorderFactory.createEmptyBorder(17, 15, 0, 0));

		bt_title.setPreferredSize(new Dimension(180, 20));
		bt_title.setBackground(Config.GREEN);
		bt_title.setFocusPainted(false);
		bt_title.setBorder(null);

		p_menu.setBorder(BorderFactory.createEmptyBorder(14, 0, 0, 200));
		p_menu.setOpaque(false);

		bt_pd.setFont(new Font("Noto Sans KR", Font.BOLD, 20));
		bt_pd.setHorizontalAlignment(JButton.LEFT);
		bt_pd.setBackground(Config.GREEN);
		bt_pd.setFocusPainted(false);
		bt_pd.setBorder(null);

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

		bt_ma.setFont(new Font("Noto Sans KR", Font.BOLD, 20));
		bt_ma.setHorizontalAlignment(JButton.LEFT);
		bt_ma.setBackground(Config.GREEN);
		bt_ma.setFocusPainted(false);
		bt_ma.setBorder(null);

		p_my.setOpaque(false);
		p_my.setBorder(BorderFactory.createEmptyBorder(17, 0, 0, 10));

		lb_me.setFont(new Font("Noto Sans KR", Font.BOLD, 15));
		lb_me.setHorizontalAlignment(JLabel.RIGHT);

		bt_lo.setFont(new Font("Noto Sans KR", Font.BOLD, 15));
		bt_lo.setHorizontalAlignment(JButton.RIGHT);
		bt_lo.setBackground(Config.GREEN);
		bt_lo.setFocusPainted(false);
		bt_lo.setBorder(null);

		p_content.setBackground(Config.GREEN);

		// assemble
		p_title.add(bt_title);
		p_navi.add(p_title, BorderLayout.WEST);

		p_menu.add(bt_pd);
		p_menu.add(Box.createHorizontalStrut(50));
		p_menu.add(bt_io);
		p_menu.add(Box.createHorizontalStrut(50));
		p_menu.add(bt_st);
		p_menu.add(Box.createHorizontalStrut(50));
		p_menu.add(bt_cl);
		p_menu.add(Box.createHorizontalStrut(50));
		p_menu.add(bt_sh);
		p_menu.add(Box.createHorizontalStrut(50));
		p_menu.add(bt_ma);
		p_navi.add(p_menu);

		p_my.add(lb_me);
		p_my.add(Box.createHorizontalStrut(10));
		p_my.add(bt_lo);
		p_navi.add(p_my, BorderLayout.EAST);

		add(p_navi, BorderLayout.NORTH);
		add(p_content, BorderLayout.CENTER);

		createPage();

		// listener
		for (JButton btn : new JButton[] { bt_pd, bt_title, bt_io, bt_st, bt_cl, bt_sh, bt_ma, bt_lo }) {
			btn.addMouseListener(new MouseAdapter() {
				public void mouseEntered(MouseEvent e) {
					btn.setForeground(Color.WHITE);
				}

				public void mouseExited(MouseEvent e) {
					btn.setForeground(Color.BLACK);
				}

				public void mouseClicked(MouseEvent e) {
					JButton source = (JButton) e.getSource();


					if (source == bt_title)
						showPage(Config.MAIN_PAGE);
					else if (source == bt_pd)
						showPage(Config.PRODUCT_PAGE);
					else if (source == bt_io)
						showPage(Config.BOUND_PAGE);
					else if (source == bt_st)
						showPage(Config.STOCK_PAGE);
					else if (source == bt_sh)
						showPage(Config.STORE_PAGE);
					else if (source == bt_ma)
						showPage(Config.MANAGE_PAGE);
					else if (source == bt_lo) {
						if ((JOptionPane.showConfirmDialog(MainLayout.this, "로그아웃 하시겠습니까?", "중요",
								JOptionPane.OK_CANCEL_OPTION)) == JOptionPane.OK_OPTION) {
							setVisible(false);

							dispose();
							new LoginPage();
						}
					}
				}
			});
		}

		showPage(Config.MAIN_PAGE);

		getContentPane().setBackground(Config.WHITE);
		setSize(Config.LAYOUT_W, Config.LAYOUT_H);
		setLocationRelativeTo(null);
		setVisible(true);
		
		// 윈도우 닫으면 쓰레드 종료
		addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowClosing(WindowEvent e) {
		        running = false; // 스레드 종료 플래그 설정
		        System.out.println("메인 창 종료 → 자동 출고 스레드 종료 요청됨");
		    }
		});
		
		// 자동 출고 쓰레드 초기화 작업 <- 페이지 생성 후에 run
		startAutoOutboundThread();	
	}

	public void createPage() {
		pages = new Page[6];

		pages[0] = new MainPage(this);
		pages[1] = new ProductPage(this);
		pages[2] = new BoundPage(this);
		pages[3] = new StockPage(this);
		pages[4] = new StorePage(this);
		pages[5] = new ManagePage(this);

		for (int i = 0; i < pages.length; i++) {
			p_content.add(pages[i]);
		}
	}

	/* 
	 * 데이터 수정 시 모든 패널 업데이트 코드
	 */

	public void setDataDirty(boolean dataDirty) {
		this.isDataDirty = dataDirty;
	}

	public boolean isDataDirty() {
		return isDataDirty;
	}

	/** 데이터 변경 시 필요한 페이지들을 새로고침 */
	public void refreshIfDirty() {
		if (isDataDirty) {
			for (Page page : pages) {
				page.refreshAll();
			}
			isDataDirty = false;
		}
	}

	public void showPage(int target) {
		refreshIfDirty(); // ← 새로고침 시점은 페이지 전환 직전
		for (int i = 0; i < pages.length; i++)
			pages[i].setVisible((i == target) ? true : false);
	}
	
	// 자동 출고 쓰레드 메서드
	
   private void startAutoOutboundThread() {
        Thread autoOutboundThread = new Thread(() -> {
            StockDAO stockDAO = new StockDAO();

            // 현재 Thread.sleep이 while문 안에 존재하여 프로그램 종료 후에도 for문이 반복 실행중 (자동 출고중)
            while (running) { 
                try {
                    // 1. 재고 수량이 1 이상인 재고 리스트 조회
                    List<Stock> stockList = stockDAO.selectAllStockWithQuantity(user);

                    for (Stock stock : stockList) {
                        if (stock.getSt_quantity() > 0) {
                            // 2. 수량 감소 처리
                            int newQty = stock.getSt_quantity() - 1;
                            stock.setSt_quantity(newQty);
                            if(!running) break;
                            stockDAO.updateQuantity(stock.getSt_id(), newQty, user);

                            showAutoOutboundDialog(stock, newQty);
                            System.out.println("자동 출고: " + stock.getSt_id() + " → 수량: " + newQty);
                        }
                        // 패널 업데이트
                        setDataDirty(true); 
                        refreshIfDirty();
                        // 3. 60초 대기
                        Thread.sleep(60 * 1000);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        autoOutboundThread.setDaemon(true); // 창 종료 시 스레드도 종료되도록
        autoOutboundThread.start();
    }
   
	   private void showAutoOutboundDialog(Stock stock, int newQty) {
		    SwingUtilities.invokeLater(() -> {
		        JDialog dialog = new JDialog();
		        dialog.setTitle("자동 출고 알림");
		        dialog.setSize(300, 180);
		        dialog.setLocationRelativeTo(null);
		        dialog.setModal(false);
	
		        JPanel panel = new JPanel();
		        panel.setLayout(new BorderLayout(10, 10));
		        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
	
		        JLabel message = new JLabel("<html>자동 출고가 발생했습니다.<br>재고 ID: <b>" + stock.getSt_id() +
		                "</b><br>남은 수량: <b>" + newQty + "</b></html>", SwingConstants.CENTER);
		        message.setFont(new Font("SansSerif", Font.PLAIN, 14));
		        panel.add(message, BorderLayout.CENTER);
	
		        JButton btnClose = new JButton("확인");
		        btnClose.addActionListener(e -> dialog.dispose());
		        panel.add(btnClose, BorderLayout.SOUTH);
	
		        dialog.add(panel);
		        dialog.setVisible(true);
		    });
		}


	public String setProfile() {
		String profile = null;
		
		profile = user.getUser_name()  
				+ " "
				+ user.getRole().getRole_name()
				+ "님 *´︶`*";
		
		if (user.getRole().getRole_id() != 1) {
			profile = branchDAO.getBranchList(user.getUser_id()) + " " + profile;
		}

		return profile;
	}
}
