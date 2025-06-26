package com.olive.mainlayout;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import com.olive.bound.BoundPage;
import com.olive.chat.Client;
import com.olive.common.config.Config;
import com.olive.common.model.Stock;
import com.olive.common.model.User;
import com.olive.common.repository.BranchDAO;
import com.olive.common.repository.StockDAO;
import com.olive.common.repository.UserDAO;
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
	JButton bt_sh; // 지점 store(shop)
	JButton bt_ma; // 관리 manage

	JPanel p_my;
	JButton bt_alert;
	JLabel lb_alertCount; // 알람 숫자 label
	JLabel lb_me; // ooo지점 oo님
	JButton bt_lo; // logout

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
    private boolean running = true;
    
    private List<String> autoOutboundLog = Collections.synchronizedList(new ArrayList<>());
    
	ImageUtil imgUtil = new ImageUtil();

	public User user;
	BranchDAO branchDAO;
	Client client;

	
	public MainLayout(User user) {
		this.user = user;
		
		client = new Client(this);  //채팅 클라이언트 연결
		client.setVisible(false);
		
		img_float_default = img_title.getImage("images/chat.png", 40, 40);
		img_float_hover = img_title.getImage("images/chat_hover.png", 40, 40);
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
		bt_sh = new JButton("지점");
		bt_ma = new JButton("관리");

		p_my = new JPanel();
		
		Image img = imgUtil.getImage(Config.ALERT_IMAGE, 27, 27);
		bt_alert = new JButton(new ImageIcon(img));
		
		bt_alert.setPreferredSize(new Dimension(30, 30)); // 이미지보다 살짝 여유
		bt_alert.setBorderPainted(false);           // 테두리 제거 
		bt_alert.setContentAreaFilled(false);       // 배경 제거 
		bt_alert.setMargin(new Insets(0, 0, 0, 0)); // 마진 제거
		bt_alert.setBackground(Config.GREEN);    // 배경색 설정 , 적용안됨
		bt_alert.setOpaque(false);
		
		bt_alert.addActionListener(e -> showAutoOutboundLogDialog());
		lb_me = new JLabel(setProfile());
		bt_lo = new JButton("로그아웃");

		p_content = new JPanel();
		
		
		img_float_default = img_title.getImage("images/chat.png", 40, 40);
		img_float_hover = img_title.getImage("images/chat_hover.png", 40, 40);
		
		bt_alert.addActionListener(e -> {
		    alertCount = 0;
		    lb_alertCount.setText("0");
		});

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

		p_menu.setBorder(BorderFactory.createEmptyBorder(14, 0, 0, 30));
		p_menu.setOpaque(false);

		for (JButton button : new JButton[] {bt_pd, bt_io, bt_st, bt_sh, bt_ma}) {
			button.setFont(new Font("Noto Sans KR", Font.BOLD, 20));
			button.setHorizontalAlignment(JButton.LEFT);
			button.setBackground(Config.GREEN);
			button.setFocusPainted(false);
			button.setBorder(null);
		}

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

		for (JButton button : new JButton[] {bt_pd, bt_io, bt_st, bt_sh}) {
			p_menu.add(button);
			p_menu.add(Box.createHorizontalStrut(50));
		}
		p_menu.add(bt_ma);
		p_navi.add(p_menu);
		
		// 숫자 라벨 (초기값 0)
		lb_alertCount = new JLabel("0");
		lb_alertCount.setFont(new Font("Noto Sans KR", Font.BOLD, 13));
		lb_alertCount.setForeground(Color.RED);

		// 버튼과 숫자를 나란히 배치할 패널
		JPanel alertPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
		alertPanel.setOpaque(false);
		alertPanel.add(bt_alert);
		alertPanel.add(lb_alertCount);

		// 기존 패널에 붙이기
		p_my.add(alertPanel);
		p_my.add(lb_me);
		p_my.add(Box.createHorizontalStrut(10));
		p_my.add(bt_lo);
		p_navi.add(p_my, BorderLayout.EAST);

		add(p_navi, BorderLayout.NORTH);
		add(p_content, BorderLayout.CENTER);

		createPage();
		
		// listener
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
						if(user.getRole().getRole_id() == 1) {
							JOptionPane.showMessageDialog(MainLayout.this, "팀장은 지점 관리에서 볼 수 있습니다");
						} else {
							showPage(Config.STOCK_PAGE);
						}
					}
					else if (source == bt_sh)
						showPage(Config.STORE_PAGE);
					else if (source == bt_ma)
						showPage(Config.MANAGE_PAGE);
					else if (source == bt_lo) {
						if ((JOptionPane.showConfirmDialog(MainLayout.this, "로그아웃 하시겠습니까?", "중요", JOptionPane.OK_CANCEL_OPTION)) == JOptionPane.OK_OPTION) {
							if(client != null) {
								System.out.println("클라이언트 종료");
			    				client.clientThread.send("disconnect", null);  // loginpage의 main 스레드가 clientThread의 send를 호출하여 실행 
			    				client.dispose();
							}
							dispose();
							new LoginPage();
						}
					}
				}
			});
		}
		
		createFloatButton();
		

		showPage(Config.MAIN_PAGE);

		//채팅 서버와 연결 끊기
		addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent e) {
				if(client != null) {
					System.out.println("클라이언트 종료");
    				client.clientThread.send("disconnect", null);  // loginpage의 main 스레드가 clientThread의 send를 호출하여 실행 
    				client.dispose();
				}
                System.exit(0);
			}	
		});
			
		
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
		bt_float.setBounds(0, 0, 40, 40);
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
                        	if(!running) break;
                            // 2. 수량 감소 처리
                            int newQty = stock.getSt_quantity() - 1;
                            stock.setSt_quantity(newQty);
                            stockDAO.updateQuantity(stock.getSt_id(), newQty, user);
                            
                            String logEntry = "- 재고 ID: " + stock.getSt_id() + ", 남은 수량: " + newQty;
                            autoOutboundLog.add(logEntry);
                            System.out.println("자동 출고 - " + logEntry);

                    	   	alertCount++;
                    	    lb_alertCount.setText(String.valueOf(alertCount));
                            // 패널 업데이트
                            setDataDirty(true); 
                            refreshIfDirty();
                        }
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
   private void showAutoOutboundLogDialog() {
	   
	    SwingUtilities.invokeLater(() -> {
	        JDialog dialog = new JDialog();
	        dialog.setTitle("자동 출고 기록");
	        dialog.setSize(400, 300);
	        dialog.setLocationRelativeTo(null);
	        dialog.setModal(true);

	        JPanel panel = new JPanel(new BorderLayout(10, 10));
	        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

	        JTextArea textArea = new JTextArea();
	        textArea.setEditable(false);
	        for (String log : autoOutboundLog) {
	            textArea.append(log + "\n");
	        }

	        JScrollPane scrollPane = new JScrollPane(textArea);
	        panel.add(scrollPane, BorderLayout.CENTER);

	        JButton btnClose = new JButton("닫기");
	        btnClose.addActionListener(ev -> dialog.dispose());
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
