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

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.olive.bound.BoundPage;
import com.olive.chat.Client;
import com.olive.common.config.Config;
import com.olive.common.model.User;
import com.olive.common.repository.BranchDAO;
import com.olive.common.util.ImageUtil;
import com.olive.common.view.MainPage;
import com.olive.common.view.Page;
import com.olive.login.LoginPage;
import com.olive.manage.ManageConfig;
import com.olive.manage.ManagePage;
import com.olive.product.ProductPage;
import com.olive.stock.StockPage;
import com.olive.store.StorePage;

public class MainLayout extends JFrame {

	JPanel p_navi;
	JPanel p_left;
	JPanel p_title;
	Image img;
	ImageUtil img_title = new ImageUtil();
	JButton bt_title;

	JPanel p_menu;
	JButton bt_pd;
	JButton bt_io;
	JButton bt_st;
	JButton bt_sh;
	JButton bt_ma;

	JPanel p_my;
	JButton bt_alert;
	ImageIcon img_alert_default;
	ImageIcon img_alert_hover;
	JLabel lb_alertCount;
	JLabel lb_me;
	JButton bt_lo;

	JPanel p_content;
	
	JPanel p_float;
	JButton bt_float;
	Image img_float_default;
	Image img_float_hover;
	Image curImg;

	public JLabel lb_chatCount;
	Page[] pages; // 페이지 담을 배열
	
	private int alertCount = 0;
  
	private boolean isDataDirty = false;

	ImageUtil imgUtil = new ImageUtil();

	public User user;
	BranchDAO branchDAO;
	Client client;

	private AutoOutboundManager autoOutboundManager;

	public MainLayout(User user) {
		this.user = user;


		setVisible(false);
		client = new Client(this); // 채팅 클라이언트 연결
		client.setVisible(false);
		client.connect();

		branchDAO = new BranchDAO();

		// create
		p_navi = new JPanel();

		p_left = new JPanel();

		p_title = new JPanel();
		img = img_title.getImage(Config.LOGO_PATH, 180, 20);
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
		bt_sh = new JButton("지점");
		bt_ma = new JButton("관리");

		p_my = new JPanel();

		img_alert_default = new ImageIcon(imgUtil.getImage(Config.ALERT_DEF, 30, 30));
		img_alert_hover = new ImageIcon(imgUtil.getImage(Config.ALERT_HOVER, 30, 30));
		bt_alert = new JButton(img_alert_default);

		lb_me = new JLabel(setProfile());
		bt_lo = new JButton("로그아웃");

		p_content = new JPanel();

		img_float_default = img_title.getImage(Config.FLOAT_DEF, 40, 40);
		img_float_hover = img_title.getImage(Config.FLOAT_HOVER, 40, 40);

		// style
		p_navi.setBackground(Config.GREEN);
		p_navi.setPreferredSize(new Dimension(Config.NAVI_W, Config.NAVI_H));
		p_navi.setLayout(new BorderLayout());
		p_navi.setBorder(BorderFactory.createEmptyBorder(9, 15, 0, 15));

		p_left.setBackground(Config.GREEN);

		p_title.setOpaque(false);
		p_title.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15));

		bt_title.setPreferredSize(new Dimension(180, 20));
		bt_title.setBackground(Config.GREEN);
		bt_title.setFocusPainted(false);
		bt_title.setBorder(null);

		p_menu.setOpaque(false);

		for (JButton button : new JButton[] { bt_pd, bt_io, bt_st, bt_sh, bt_ma }) {
			button.setFont(new Font("Noto Sans KR", Font.BOLD, 20));
			button.setHorizontalAlignment(JButton.LEFT);
			button.setBackground(Config.GREEN);
			button.setFocusPainted(false);
			button.setBorder(null);
		}

		p_my.setOpaque(false);
		p_my.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

		for (JButton button : new JButton[] { bt_alert, bt_lo }) {
			button.setBackground(Config.GREEN);
			button.setFocusPainted(false);
			button.setBorder(null);
		}

		bt_alert.setPreferredSize(new Dimension(30, 30));

		lb_me.setFont(new Font("Noto Sans KR", Font.BOLD, 15));
		lb_me.setHorizontalAlignment(JLabel.RIGHT);

		bt_lo.setFont(new Font("Noto Sans KR", Font.BOLD, 15));
		bt_lo.setHorizontalAlignment(JButton.RIGHT);

		p_content.setBackground(Config.GREEN);

		// assemble
		p_title.add(bt_title);
		p_left.add(p_title);

		for (JButton button : new JButton[] { bt_pd, bt_io, bt_st, bt_sh }) {
			p_menu.add(button);
			p_menu.add(Box.createHorizontalStrut(50));
		}
		p_menu.add(bt_ma);

		p_left.add(p_menu);
		p_navi.add(p_left, BorderLayout.WEST);

		lb_alertCount = new JLabel("0");
		lb_alertCount.setFont(new Font("Noto Sans KR", Font.BOLD, 13));
		lb_alertCount.setForeground(Color.RED);

		p_my.add(bt_alert);
		p_my.add(lb_alertCount);
		p_my.add(Box.createHorizontalStrut(10));
		p_my.add(lb_me);
		p_my.add(Box.createHorizontalStrut(10));
		p_my.add(bt_lo);
		p_navi.add(p_my, BorderLayout.EAST);

		add(p_navi, BorderLayout.NORTH);
		add(p_content, BorderLayout.CENTER);

		createPage();

		for (JButton btn : new JButton[] { bt_pd, bt_title, bt_io, bt_st, bt_sh, bt_ma, bt_lo }) {
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
					else if (source == bt_st) {
						
						
						
						
						if (user.getRole().getRole_id() == 1) {
							JOptionPane.showMessageDialog(MainLayout.this, "팀장은 지점 관리에서 볼 수 있습니다");
						} else {
							showPage(Config.STOCK_PAGE);
						}
					} else if (source == bt_sh) {
		                  showPage(Config.STORE_PAGE);
		                  if (user.getRole().getRole_id()==1)
		                     ((StorePage)pages[4]).showPanel(0);
		                  else 
		                     ((StorePage)pages[4]).showPanel(1);
		               }
					else if (source == bt_ma)
						if(user.getRole().getRole_id() == 3) {
							JOptionPane.showMessageDialog(MainLayout.this, "권한이 없습니다");
						} else {
							//관리 버튼 누르면 항상 사용자 목록 페이지가 보이도록 설정 
							showPage(Config.MANAGE_PAGE);
							((ManagePage)(pages[Config.MANAGE_PAGE])).userListPanel.clearFilter();
							((ManagePage)(pages[Config.MANAGE_PAGE])).userListPanel.refreshAll();
							((ManagePage)(pages[Config.MANAGE_PAGE])).showPanel(ManageConfig.USER_LIST_KEY);
							((ManagePage)(pages[Config.MANAGE_PAGE])).currentKey = ManageConfig.USER_LIST_KEY;
							((ManagePage)(pages[Config.MANAGE_PAGE])).p_content.revalidate();
							((ManagePage)(pages[Config.MANAGE_PAGE])).p_content.repaint();
						}
						
					else if (source == bt_lo) {
						if ((JOptionPane.showConfirmDialog(MainLayout.this, "로그아웃 하시겠습니까?", "중요",
								JOptionPane.OK_CANCEL_OPTION)) == JOptionPane.OK_OPTION) {
							if (client != null) {
								System.out.println(" 로그아웃, 클라이언트 끊기");
								client.clientThread.send("disconnect", null);
								client.dispose();
							}
							autoOutboundManager.stop();
							dispose();
							new LoginPage();
						}
					}
				}
			});
		}

		bt_alert.addActionListener(e -> {
			autoOutboundManager.showAutoOutboundLogDialog();
			autoOutboundManager.resetAlertCount();
		});

		bt_alert.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				bt_alert.setIcon(img_alert_hover);
			}

			public void mouseExited(MouseEvent e) {
				bt_alert.setIcon(img_alert_default);
			}
		});

		if (user.getRole().getRole_id() != 1)
			createFloatButton();

		showPage(Config.MAIN_PAGE);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (client != null) {
					System.out.println(" 윈도우 종료, 클라이언트 끊기");
					client.clientThread.send("disconnect", null);
					client.dispose();
				}
				autoOutboundManager.stop();
				System.out.println("메인 창 종료 → 자동 출고 스레드 종료 요청됨");
				System.exit(0);
			}
		});

		getContentPane().setBackground(Config.WHITE);
		setSize(Config.LAYOUT_W, Config.LAYOUT_H);
		setLocationRelativeTo(null);

		// 자동 출고 매니저 시작
		autoOutboundManager = new AutoOutboundManager(this, user, lb_alertCount);
		autoOutboundManager.start();
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

	public void createFloatButton() {
		getLayeredPane().setLayout(null);
		
		curImg = img_float_hover;
		
		p_float = new JPanel();
		p_float.setLayout(null); // 내부 컴포넌트 위치 수동 지정
		p_float.setBounds(Config.LAYOUT_W - 80, Config.LAYOUT_H - 100, 70, 50); // 위치+크기 지정
		p_float.setOpaque(false);
		getLayeredPane().add(p_float, JLayeredPane.POPUP_LAYER);
		
		bt_float = new JButton() {
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(curImg, 0, 0, 40, 40, bt_float);
			}
		};

		bt_float.setSize(40, 40);
		//bt_float.setBounds(0, 0, 40, 40);
		bt_float.setLocation(Config.LAYOUT_W - 80, Config.LAYOUT_H - 100);
		bt_float.setContentAreaFilled(false); // 배경 제거
		bt_float.setBorderPainted(false);    // 테두리 제거
		bt_float.setFocusPainted(false);
		p_float.add(bt_float);
		
		lb_chatCount = new JLabel("0");
		lb_chatCount.setVisible(false);
		lb_chatCount.setBounds(40, 0, 20, 20);
		lb_chatCount.setForeground(Color.RED);
		p_float.add(lb_chatCount);
		
		bt_float.addActionListener(e -> {
			lb_chatCount.setText("0");
		    lb_chatCount.setVisible(false);
		    client.setVisible(true);
		    
		});
		

// 		bt_float.setSize(40, 40);
// 		bt_float.setContentAreaFilled(false);
// 		bt_float.setBorderPainted(false);
// 		bt_float.setFocusPainted(false);
// 		bt_float.setLocation(Config.LAYOUT_W - 80, Config.LAYOUT_H - 100);


		bt_float.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				curImg = img_float_default;
				bt_float.repaint();
			}

			public void mouseExited(MouseEvent e) {
				curImg = img_float_hover;
				bt_float.repaint();
			}
		});

		bt_float.addActionListener(e -> {
			client.setVisible(true);
		});


		getLayeredPane().add(bt_float, javax.swing.JLayeredPane.POPUP_LAYER);

	}

	public void setDataDirty(boolean dataDirty) {
		this.isDataDirty = dataDirty;
	}

	public boolean isDataDirty() {
		return isDataDirty;
	}

	public void refreshIfDirty() {
		if (isDataDirty) {
			for (Page page : pages) {
				page.refreshAll();
			}
			isDataDirty = false;
		}
	}

	public void showPage(int target) {
		refreshIfDirty();
		for (int i = 0; i < pages.length; i++) {
			pages[i].setVisible((i == target) ? true : false);
		}
	}

	public String setProfile() {
		String profile = null;

		profile = user.getUser_name() + " " + user.getRole().getRole_name() + "님 *´︶`*";

		if (user.getRole().getRole_id() != 1) {
			profile = branchDAO.getBranchList(user.getUser_id()) + " " + profile;
		}

		return profile;
	}
}
