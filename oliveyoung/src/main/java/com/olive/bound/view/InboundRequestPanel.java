package com.olive.bound.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

<<<<<<< HEAD
=======
import com.olive.bound.model.BoundProductModel;
import com.olive.bound.model.InboundModel;
>>>>>>> parent of 5c4965e (feat : finished to Outbound request & list)
import com.olive.common.config.Config;
import com.olive.common.model.Branch;
import com.olive.common.model.Stock;
import com.olive.common.model.User;
import com.olive.common.repository.BranchDAO;
<<<<<<< HEAD
=======
import com.olive.common.repository.InboundDAO;
>>>>>>> parent of 5c4965e (feat : finished to Outbound request & list)
import com.olive.common.repository.ProductDAO;
import com.olive.common.repository.UserDAO;
import com.olive.common.view.Panel;
import com.olive.mainlayout.MainLayout;
import com.toedter.calendar.JDateChooser;

public class InboundRequestPanel extends Panel{
	// NORTH
	JPanel p_north;
	
	// CENTER
	JPanel p_center;
	
	// LEFT 상품 테이블
	JPanel p_list;
	JLabel la_left;
	JTable table;
	JScrollPane scroll;
	
	// RIGHT 입고 요청 폼
	JPanel p_request;
	JLabel la_right;
	
	JTable table_re;
	JScrollPane scroll_re;
	
	// BOTTOM
	JPanel p_bottom;
	JComboBox<Branch> cb_branch;
	JDateChooser dateChooser;
	JLabel la_date;
	JButton bt_save;
	
	
	ProductDAO productDAO;
	BranchDAO branchDAO;
<<<<<<< HEAD
=======
	InboundDAO insertDAO;
	
	InboundModel model; // 왼쪽 테이블 클릭시 우측테이블로 데이터 전송을 위한 모델 생성
>>>>>>> parent of 5c4965e (feat : finished to Outbound request & list)
	InboundModel inboundModel;
	BoundProductModel boundProductModel;
	DefaultTableCellRenderer centerRenderer;
	
	public InboundRequestPanel(MainLayout mainLayout, User user) {
		super(mainLayout);
		setLayout(new BorderLayout());
		
		// 공통 색상 및 폰트
        Color bgColor = new Color(245, 248, 250);
        Color comboColor = new Color(100, 149, 237);
        Font defaultFont = new Font("SansSerif", Font.PLAIN, 13);

        // 상단 패널
        p_north = new JPanel(new BorderLayout());
        
        // 좌측 상단 패널
        p_list = new JPanel();
        la_left = new JLabel("상품 목록");
        la_left.setFont(new Font("SansSerif", Font.BOLD, 22));
        
        // 우측 패널
        p_request = new JPanel();
        la_right = new JLabel("입고 물품 목록");
        la_right.setFont(new Font("SansSerif", Font.BOLD, 22));        
        
        
        // 중앙 패널
        p_center = new JPanel(new BorderLayout());
		
		// 좌측 중앙 - 테이블
		table = new JTable(new InboundModel("now")); // 입고할 상품 리스트 테이블에 출력		
<<<<<<< HEAD
		
		// 테이블 헤더 클릭 이벤트 추가
		JTableHeader header = table.getTableHeader();
		header.addMouseListener(new java.awt.event.MouseAdapter() {
		    @Override
		    public void mouseClicked(java.awt.event.MouseEvent e) {
		        int columnIndex = header.columnAtPoint(e.getPoint());
		        String columnName = table.getColumnName(columnIndex);
		        System.out.println("헤더 클릭됨: " + columnName + " (인덱스: " + columnIndex + ")");
		        
		        // 예: 제품명 컬럼 클릭시만 처리
		        if ("제품명".equals(columnName)) {
		            javax.swing.JOptionPane.showMessageDialog(null, "제품명 컬럼 클릭됨");
		        }
		    }
		});
=======
>>>>>>> parent of 5c4965e (feat : finished to Outbound request & list)
		
		// 테이블 헤더 스타일
		table.setRowHeight(25);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        table.getTableHeader().setBackground(Config.LIGHT_GREEN); // 테이블 헤더 배경색 설정
        table.getTableHeader().setForeground(Color.DARK_GRAY);
        
        // 테이블 셀 가운데 정렬
        centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        scroll = new JScrollPane(table);
        
        
        
        
        
		
        boundProductModel = new BoundProductModel();
		table_re = new JTable(boundProductModel); // 입고 요청서 테이블
		
		// 테이블 헤더 클릭 이벤트 추가
		JTableHeader header_re = table_re.getTableHeader();
		header_re.addMouseListener(new java.awt.event.MouseAdapter() {
		    @Override
		    public void mouseClicked(java.awt.event.MouseEvent e) {
		        int columnIndex = header_re.columnAtPoint(e.getPoint());
		        String columnName = table_re.getColumnName(columnIndex);
		        System.out.println("헤더 클릭됨: " + columnName + " (인덱스: " + columnIndex + ")");
		        
		        // 예: 제품명 컬럼 클릭시만 처리
		        if ("제품명".equals(columnName)) {
		            javax.swing.JOptionPane.showMessageDialog(null, "제품명 컬럼 클릭됨");
		        }
		    }
		});
		
		// 테이블 헤더 스타일
		table_re.setRowHeight(25);
		table_re.setFont(new Font("SansSerif", Font.PLAIN, 13));
		table_re.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
		table_re.getTableHeader().setBackground(Config.LIGHT_GREEN); // 테이블 헤더 배경색 설정
		table_re.getTableHeader().setForeground(Color.DARK_GRAY);
        
        // 테이블 셀 가운데 정렬
        centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table_re.getColumnCount(); i++) {
        	table_re.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        scroll_re = new JScrollPane(table_re);
		
        // 컬럼 클릭 이벤트 -> 우측 테이블에 추가
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    // 현재 테이블 모델을 가져온다 (InboundModel)
                    InboundModel model = (InboundModel) table.getModel();
                    Stock selectedStock = model.list.get(row);

                    // 선택된 상품을 입고요청 모델에 추가
                    boundProductModel.addStock(selectedStock);
                }
            }
        });
				
				
				
				
				
				
				
				
				
		// 하단 지점 선택 - 콤보박스
        // 기존 p_bottom 내부 교체
        p_bottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));

        JPanel comboPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        comboPanel.setOpaque(false);

        cb_branch = new JComboBox<>();
        cb_branch.setPreferredSize(new Dimension(200, 30));
        cb_branch.setFont(new Font("SansSerif", Font.PLAIN, 14));
        cb_branch.setBackground(Config.LIGHT_GREEN);
        cb_branch.setForeground(Color.DARK_GRAY);
        cb_branch.setFocusable(false);
        cb_branch.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        comboPanel.add(cb_branch);	

        // 신규 추가: 하단 버튼들
        la_date = new JLabel("입고일:");
        dateChooser = new JDateChooser();
        dateChooser.setPreferredSize(new Dimension(200, 30));
        dateChooser.setDate(new java.util.Date());

//        JLabel la_approver = new JLabel("결재자:");
//        JComboBox<User> cb_approver = new JComboBox<>();
//        loadApproverList(cb_approver);
//
        bt_save = new JButton("저장");
        bt_save.setPreferredSize(new Dimension(80, 30));
        bt_save.setBackground(Config.LIGHT_GRAY);
//
//        // 저장 버튼 클릭 이벤트
//        bt_save.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                saveInboundRequest(dateChooser, cb_approver);
//            }
//        });
        
        comboPanel.add(la_date);
        comboPanel.add(dateChooser);
        comboPanel.add(bt_save);

        p_bottom.add(comboPanel);
//        p_bottom.add(la_date);
//        p_bottom.add(dateChooser);
//        p_bottom.add(la_approver);
//        p_bottom.add(cb_approver);
//        p_bottom.add(bt_save);
        
        
        
        
        
		
		// 스타일
        
		//new Dimension(Config.CONTENT_W, Config.CONTENT_H) // 1100, 740 -> 550, 740
//		p_center.setPreferredSize(new Dimension(1100, 300));
        p_north.setPreferredSize(new Dimension(Config.CONTENT_W , 50));
        p_north.setBackground(Config.WHITE);
        
//        la_left.setPreferredSize(new Dimension(400, 50));
//        la_right.setPreferredSize(new Dimension(400, 50));
        
		p_center.setBackground(Config.WHITE);
		
		Dimension d = new Dimension(Config.CONTENT_W / 2 - 10, 620);
		p_list.setPreferredSize(d);
		p_list.setBackground(Config.WHITE);
		
		scroll.setPreferredSize(new Dimension(540, 550));
		scroll.getViewport().setBackground(Config.WHITE);
		
		p_request.setPreferredSize(d);
		p_request.setBackground(Config.WHITE);
		
		scroll_re.setPreferredSize(new Dimension(540, 550));
		scroll_re.getViewport().setBackground(Config.WHITE);

		
		p_bottom.setPreferredSize(new Dimension(Config.CONTENT_W , 50));
		p_bottom.setBackground(Config.WHITE);
		
		// 조립
		
		la_left.setBorder(BorderFactory.createEmptyBorder(0, 200, 0, 0));   // 왼쪽 padding
		la_right.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 200));  // 오른쪽 padding

		p_north.setLayout(new BorderLayout());
		p_north.add(la_left, BorderLayout.WEST);
		p_north.add(la_right, BorderLayout.EAST);
		
//		p_list.add(la_left);
		p_list.add(scroll);
		
//		p_request.add(la_right);
		p_request.add(scroll_re);
		
//		p_center.add(p_list);
//		p_center.add(p_request);
		// SplitPane 생성
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scroll, scroll_re);
        splitPane.setDividerLocation(500); // 초기 분할 위치 (px)
        splitPane.setResizeWeight(0.5); // 크기 조절 시 왼쪽:오른쪽 비율
        splitPane.setContinuousLayout(true);
        splitPane.setOneTouchExpandable(true); // 화살표로 접었다 펼 수 있게

        // 중앙 패널에 SplitPane 추가       
        p_center.add(splitPane, BorderLayout.CENTER);
		
		p_bottom.add(comboPanel, BorderLayout.WEST);
		
		add(p_north, BorderLayout.NORTH);
		add(p_center, BorderLayout.CENTER);
		add(p_bottom, BorderLayout.SOUTH);
		
<<<<<<< HEAD
		// 콤보박스 이벤트 연결
=======
		
		
		
		
		// 좌측 테이블 헤더 클릭 이벤트 추가 ------------------------------------------------------------
		TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
		table.setRowSorter(sorter);

		// 헤더 클릭 감지 및 정렬 상태 출력
		JTableHeader header = table.getTableHeader();
		header.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		        int columnIndex = header.columnAtPoint(e.getPoint());
		        String columnName = table.getColumnName(columnIndex);
		        System.out.println("헤더 클릭됨: " + columnName + " (인덱스: " + columnIndex + ")");

		        SortOrder order = getSortOrder(sorter, columnIndex);
		        if (order == SortOrder.ASCENDING) {
		            System.out.println("정렬 방향: 오름차순");
		        } else if (order == SortOrder.DESCENDING) {
		            System.out.println("정렬 방향: 내림차순");
		        } else {
		            System.out.println("정렬 방향 없음");
		        }
		    }

		    private SortOrder getSortOrder(TableRowSorter<?> sorter, int columnIndex) {
		        List<? extends RowSorter.SortKey> sortKeys = sorter.getSortKeys();
		        for (RowSorter.SortKey key : sortKeys) {
		            if (key.getColumn() == columnIndex) {
		                return key.getSortOrder();
		            }
		        }
		        return SortOrder.UNSORTED;
		    }
		});
		
		// 테이블 생성 이후
		TableRowSorter<TableModel> sorter_re = new TableRowSorter<>(table_re.getModel());
		table_re.setRowSorter(sorter_re);

		// 헤더 클릭 이벤트로 정렬 상태 출력
		JTableHeader header_re = table_re.getTableHeader();
		header_re.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		        int columnIndex = header_re.columnAtPoint(e.getPoint());
		        String columnName = table_re.getColumnName(columnIndex);
		        System.out.println("헤더 클릭됨: " + columnName + " (인덱스: " + columnIndex + ")");

		        SortOrder order = getSortOrder(sorter_re, columnIndex);
		        if (order == SortOrder.ASCENDING) {
		            System.out.println("정렬 방향: 오름차순");
		        } else if (order == SortOrder.DESCENDING) {
		            System.out.println("정렬 방향: 내림차순");
		        } else {
		            System.out.println("정렬 방향 없음");
		        }
		    }

		    private SortOrder getSortOrder(TableRowSorter<?> sorter, int columnIndex) {
		        List<? extends RowSorter.SortKey> sortKeys = sorter.getSortKeys();
		        for (RowSorter.SortKey key : sortKeys) {
		            if (key.getColumn() == columnIndex) {
		                return key.getSortOrder();
		            }
		        }
		        return SortOrder.UNSORTED;
		    }
		});
		
		// 컬럼 클릭 이벤트 -> 우측 테이블에 추가 ------------------------------------------------------------
		table.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent e) {
				int viewRow = table.getSelectedRow();  // 화면상 클릭한 행
				if (viewRow >= 0) {
					int modelRow = table.convertRowIndexToModel(viewRow);  // 실제 모델 인덱스

		            // 모델에서 정확한 데이터 가져오기
		            model = (InboundModel) table.getModel();
		            Stock selectedStock = model.list.get(modelRow); // ✅ 반드시 modelRow 사용

		            BoundProduct bp = new BoundProduct();
		            bp.setProductOption(selectedStock.getProductOption());
		            bp.setB_count(1); // 초기 수량

		            boundProductModel.addProduct(bp);
				}
			}
		});
		
		// 저장 버튼 클릭 이벤트 ------------------------------------------------------------
		bt_save.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				saveInboundRequest(userId);
			}
			
			public void mouseEntered(MouseEvent e) {
				bt_save.setBackground(Config.GREEN);
			};

			public void mouseExited(MouseEvent e) {
				bt_save.setBackground(Config.LIGHT_GRAY);
			};
		});
		
		// 콤보박스 이벤트 연결 ------------------------------------------------------------
>>>>>>> parent of 5c4965e (feat : finished to Outbound request & list)
		cb_branch.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                	Branch branch= (Branch) cb_branch.getSelectedItem();
                    if (branch.getBr_id() != 0) {
<<<<<<< HEAD
                        InboundModel inboundModel = new InboundModel(branch);
=======
                        inboundModel = new InboundModel(branch);
>>>>>>> parent of 5c4965e (feat : finished to Outbound request & list)
                        table.setModel(inboundModel);
                    } else {
                        table.setModel(new InboundModel("now"));
                    }

                    // 선택 변경 후 렌더러 다시 설정
                    for (int i = 0; i < table.getColumnCount(); i++) {
                        table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
                    }
                    table.updateUI();
                }
            	
            	System.out.println("리스트");
            }
        });
		
		setPreferredSize(new Dimension(Config.CONTENT_W, Config.CONTENT_H-70));
		setBackground(Config.WHITE);
		
		loadCategories();
	}
	
	 // 카테고리 목록 불러오기
    private void loadCategories() {
    	System.out.println("load");
    	branchDAO = new BranchDAO();
        List<Branch> branchList = branchDAO.selectAll();
        System.out.println(branchList.get(1));

<<<<<<< HEAD
        Branch dummy = new Branch();
        dummy.setBr_id(0);
        dummy.setBr_name("카테고리를 선택하세요");
        cb_branch.addItem(dummy);
        
        for (Branch branch : branchList) {
        	cb_branch.addItem(branch);
        }
=======
	    if (branchList.isEmpty()) {
	        JOptionPane.showMessageDialog(this, "소속된 지점이 없습니다.");
	        return;
	    }

	    for (Branch branch : branchList) {
	        cb_branch.addItem(branch);
	    }

	    // ✅ 첫 번째 지점을 기본 선택값으로 설정
	    cb_branch.setSelectedIndex(0);

	    // ✅ 초기에 테이블도 해당 지점으로 세팅
	    Branch firstBranch = (Branch) cb_branch.getSelectedItem();
	    if (firstBranch != null) {
	        inboundModel = new InboundModel(firstBranch);
	        table.setModel(inboundModel);

	        setTableWidth(table);
	    }
>>>>>>> parent of 5c4965e (feat : finished to Outbound request & list)
    }
    
    private void loadApproverList(JComboBox<User> cb_approver) {
        UserDAO userDAO = new UserDAO();
        List<User> userList = userDAO.selectAll();
        cb_approver.addItem(null); // 선택 안했을 때 default
        for (User user : userList) {
            cb_approver.addItem(user);
        }
    }
    
    private void saveInboundRequest(JDateChooser dateChooser, JComboBox<User> cb_approver) {
        User approver = (User) cb_approver.getSelectedItem();
        java.util.Date utilDate = dateChooser.getDate();

<<<<<<< HEAD
        if (utilDate == null) {
=======
    private void saveInboundRequest(int userId) {
 
        selectedDate = dateChooser.getDate();

        if (selectedDate == null) {
>>>>>>> parent of 5c4965e (feat : finished to Outbound request & list)
            JOptionPane.showMessageDialog(this, "입고일을 선택하세요");
            return;
        }
        if (approver == null) {
            JOptionPane.showMessageDialog(this, "결재자를 선택하세요");
            return;
        }
        if (boundProductModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "입고 요청할 상품이 없습니다");
            return;
        }

<<<<<<< HEAD
//        List<InboundProduct> products = new ArrayList<>();
//        for (int i = 0; i < requestModel.getRowCount(); i++) {
//            Stock stock = requestModel.getStockAt(i);
//            int optionId = stock.getProductOption().getOption_id();
//            int count = (int) requestModel.getValueAt(i, 2);
//            products.add(new InboundProduct(optionId, count));
//        }
//
//        InboundDAO inboundDAO = new InboundDAO();
//        inboundDAO.insertInbound(
//                approver.getUser_id(),
//                new Date(utilDate.getTime()),
//                "",
//                products
//        );
//
//        JOptionPane.showMessageDialog(this, "입고 요청이 저장되었습니다.");
//        requestModel.clear();
=======
        // 점주 계정 id
        int managerId = manager.getUser_id();
        // 지점 id
        int brId = ((Branch) cb_branch.getSelectedItem()).getBr_id();
        // 요청일 날짜 가져오기
        java.sql.Date requestDate = new java.sql.Date(selectedDate.getTime());
        // 메모 가져오기
        String memo = tf_memo.getText().trim();
        // 상품 리스트 가져오기
        productList = boundProductModel.getProductList();
        

		String requesterName = user.getUser_name();
		String approverName = manager.getUser_name();
		int totalCount = productList.stream().mapToInt(BoundProduct::getB_count).sum();
		int totalPrice = productList.stream().mapToInt(bp -> bp.getProductOption().getPrice() * bp.getB_count()).sum();
		String requestDateStr = new SimpleDateFormat("yyyy-MM-dd").format(selectedDate);

		// 팝업 보여주기
		boolean confirmed = showConfirmationDialog(requesterName, approverName, totalCount, totalPrice, requestDateStr);

		if (!confirmed) {
			JOptionPane.showMessageDialog(this, "입고 요청이 취소되었습니다.");
		    return; // 저장 중단
		} else {
			// 저장하기
			insertDAO = new InboundDAO();
			insertDAO.insertInbound(userId, managerId, brId, requestDate, memo, productList);
			
			
			// ✅ 저장 후 UI 초기화
			boundProductModel.clear(); // 테이블 초기화용 clear() 메서드 필요
			tf_memo.setText("");
			
			// ✅ 정적 메서드 호출로 새로고침
			InboundShowPanel.refreshStaticList();
			
			JOptionPane.showMessageDialog(this, "입고 요청이 저장되었습니다.");
		}
        
        
        System.out.println(
        		"로그인 id : "  	+ userId + 
        		", 점주 id : " 	+ managerId + 
        		", 지점 id : " 	+ brId +
        		", 요청일 : " 	+ requestDate +
        		", 메모 : " 		+ memo
        );
        
        System.out.println("첫번째 제품 id : " + productList.getFirst().getProductOption().getOption_id()
        		+ ", 개수 : " + productList.getFirst().getB_count());
        
        

    }
    
    private void setTableWidth(JTable table) {
    	table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getColumnModel().getColumn(0).setPreferredWidth(88);
        table.getColumnModel().getColumn(1).setPreferredWidth(90);
        table.getColumnModel().getColumn(2).setPreferredWidth(88);
        table.getColumnModel().getColumn(3).setPreferredWidth(161);
        table.getColumnModel().getColumn(4).setPreferredWidth(65);
        table.getColumnModel().getColumn(5).setPreferredWidth(67);
        table.getColumnModel().getColumn(6).setPreferredWidth(68);

        // 선택 변경 후 렌더러 다시 설정
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        table.updateUI();
    }
    
    private boolean showConfirmationDialog(String requesterName, String approverName, int totalCount, int totalPrice, String requestDate) {
        String message = String.format(
            "<html><body>" +
            "<b>입고 요청 정보를 확인해주세요.</b><br><br>" +
            "요청자 :		%s<br><br>" +
            "결재자 :		%s<br><br>" +
            "총 상품 수량 :	%d개<br><br>" +
            "총 상품 금액 :	%,d원<br><br>" +
            "입고 요청일 : 	%s<br><br><br>" +
            "정말 요청하시겠습니까?" +
            "</body></html>",
            requesterName, approverName, totalCount, totalPrice, requestDate
        );

        int result = JOptionPane.showConfirmDialog(this, message, "입고 요청 확인", JOptionPane.YES_NO_OPTION);
        return result == JOptionPane.YES_OPTION;
    }
    
    // 테이블 새로고침을 위함
    public static void refreshStaticList() {
        if (instance != null) {
            instance.refreshList(); // ✅ 내부 리프레시 메서드 호출
        }
    }

    public void refreshList() {
        // 콤보박스 선택값 초기화
        cb_branch.setSelectedIndex(0); // 첫 번째 지점 선택

        // 테이블 모델 새로고침
        Branch selectedBranch = (Branch) cb_branch.getSelectedItem();
        if (selectedBranch != null) {
            inboundModel = new InboundModel(selectedBranch);
            table.setModel(inboundModel);
            setTableWidth(table); // 컬럼 너비 재설정
        }

        // 우측 요청 상품 테이블 초기화
        boundProductModel.clear();
        table_re.setModel(boundProductModel);

        // 메모 입력 필드 초기화
        tf_memo.setText("");

        // 입고일: 내일로 재설정
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, 1); // 내일
        dateChooser.setDate(cal.getTime());

        // 결재자 이름 재설정
        manager = userDAO.getManagerByBranchId(selectedBranch.getBr_id());
        if (manager != null) {
            tf_approver.setText(manager.getUser_name());
            tf_approver.setToolTipText(manager.getUser_id() + " / " + manager.getUser_name());
        } else {
            tf_approver.setText("점장 없음");
            tf_approver.setToolTipText(null);
        }

        // 테이블 다시 그리기
        table.revalidate();
        table.repaint();
        table_re.revalidate();
        table_re.repaint();
>>>>>>> parent of 5c4965e (feat : finished to Outbound request & list)
    }
}