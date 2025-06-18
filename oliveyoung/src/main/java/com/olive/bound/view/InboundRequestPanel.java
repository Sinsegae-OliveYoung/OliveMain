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

import com.olive.bound.BoundPage;
import com.olive.common.config.Config;
import com.olive.common.model.Branch;
import com.olive.common.model.Stock;
import com.olive.common.model.User;
import com.olive.common.repository.BranchDAO;
import com.olive.common.repository.ProductDAO;
import com.olive.common.repository.UserDAO;
import com.olive.common.view.Panel;
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
	InboundModel inboundModel;
	InboundRequestModel requestModel;
	DefaultTableCellRenderer centerRenderer;
	
	public InboundRequestPanel(BoundPage boundPage) {
		super(boundPage);
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
        
        
        
        
        
		
		requestModel = new InboundRequestModel();
		table_re = new JTable(requestModel); // 입고 요청서 테이블
		
		// 테이블 헤더 클릭 이벤트 추가
		JTableHeader header_re = table.getTableHeader();
		header.addMouseListener(new java.awt.event.MouseAdapter() {
		    @Override
		    public void mouseClicked(java.awt.event.MouseEvent e) {
		        int columnIndex = header.columnAtPoint(e.getPoint());
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
                    requestModel.addStock(selectedStock);
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
        dateChooser.setPreferredSize(new Dimension(120, 30));
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
		
		// 콤보박스 이벤트 연결
		cb_branch.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                	Branch branch= (Branch) cb_branch.getSelectedItem();
                    if (branch.getBr_id() != 0) {
                        InboundModel inboundModel = new InboundModel(branch);
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

        Branch dummy = new Branch();
        dummy.setBr_id(0);
        dummy.setBr_name("카테고리를 선택하세요");
        cb_branch.addItem(dummy);
        
        for (Branch branch : branchList) {
        	cb_branch.addItem(branch);
        }
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

        if (utilDate == null) {
            JOptionPane.showMessageDialog(this, "입고일을 선택하세요");
            return;
        }
        if (approver == null) {
            JOptionPane.showMessageDialog(this, "결재자를 선택하세요");
            return;
        }
        if (requestModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "입고 요청할 상품이 없습니다");
            return;
        }

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
    }
}