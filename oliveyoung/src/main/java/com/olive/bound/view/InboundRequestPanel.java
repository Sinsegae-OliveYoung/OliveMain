package com.olive.bound.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

import com.olive.common.config.Config;
import com.olive.common.model.BoundProduct;
import com.olive.common.model.Branch;
import com.olive.common.model.Stock;
import com.olive.common.model.User;
import com.olive.common.repository.BranchDAO;
import com.olive.common.repository.InboundDAO;
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
	JTableHeader header_re;
	JTable table;
	JScrollPane scroll;
	
	// RIGHT 입고 요청 폼
	JPanel p_request;
	JLabel la_right;
	
	JTable table_re;
	JScrollPane scroll_re;
	InboundModel model;
	
	// BOTTOM
	JPanel p_bottom;
	JPanel comboPanel;
	JComboBox<Branch> cb_branch;
	JLabel la_approver;
	JTextField tf_approver;
	JLabel la_date;
	JDateChooser dateChooser;
	JLabel la_memo;
	JTextField tf_memo;
	JButton bt_save;
	
	UserDAO userDAO;
	ProductDAO productDAO;
	BranchDAO branchDAO;
	InboundModel inboundModel;
	BoundProductModel boundProductModel;
	DefaultTableCellRenderer centerRenderer;
	
	MainLayout mainLayout;
	User user;
	User currentApprover;  // 클래스 필드에 추가
	
	public InboundRequestPanel(MainLayout mainLayout, User user) {
		super(mainLayout);
		setLayout(new BorderLayout());
		
		this.mainLayout = mainLayout;
		user = mainLayout.user;
		
		System.out.println(user.getUser_id());
		
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
        scroll.getViewport().setBackground(Config.WHITE);
        scroll.setPreferredSize(new Dimension(Config.CONTENT_W / 2 + 80, Config.CONTENT_H - 180));
        
        
        
        
        
        
		
        boundProductModel = new BoundProductModel();
		table_re = new JTable(boundProductModel); // 입고 요청서 테이블
		
		// 테이블 헤더 클릭 이벤트 추가
		header_re = table_re.getTableHeader();
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
        
        table_re.getColumnModel().getColumn(0).setPreferredWidth(120);
        table_re.getColumnModel().getColumn(2).setPreferredWidth(50);

        scroll_re = new JScrollPane(table_re);
        scroll_re.getViewport().setBackground(Config.WHITE);
        scroll_re.setPreferredSize(new Dimension(Config.CONTENT_W / 2 - 150, Config.CONTENT_H - 180));
        
		
        // 컬럼 클릭 이벤트 -> 우측 테이블에 추가
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    // 현재 테이블 모델을 가져온다 (InboundModel)
                    model = (InboundModel) table.getModel();
                    Stock selectedStock = model.list.get(row);

                    // 선택된 상품을 입고요청 모델에 추가
                    boundProductModel.addStock(selectedStock);
                }
            }
        });
				
				
				
				
				
				
				
				
				
		// 하단 지점 선택 - 콤보박스
        // 기존 p_bottom 내부 교체
        p_bottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));

        comboPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        comboPanel.setOpaque(false);

        cb_branch = new JComboBox<>();
        cb_branch.setPreferredSize(new Dimension(200, 30));
        cb_branch.setFont(new Font("SansSerif", Font.PLAIN, 14));
        cb_branch.setBackground(Config.LIGHT_GRAY);
        cb_branch.setForeground(Color.DARK_GRAY);
        cb_branch.setFocusable(false);
        cb_branch.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        comboPanel.add(cb_branch);	

        // 신규 추가: 하단 버튼들
        la_date = new JLabel("입고일 :");
        
        dateChooser = new JDateChooser();
        dateChooser.setPreferredSize(new Dimension(200, 30));
        
     // 오늘 날짜 기준으로 내일 날짜 설정
        Calendar cal = Calendar.getInstance();
        cal.setTime(new java.util.Date());
        cal.add(Calendar.DATE, 1); // 내일
        java.util.Date tomorrow = cal.getTime();

        // 내일로 설정
        dateChooser.setMinSelectableDate(tomorrow);
        dateChooser.setDate(tomorrow); // 기본값도 내일로

        JTextField editor = ((JTextField) dateChooser.getDateEditor().getUiComponent());
        editor.setBackground(Config.LIGHT_GRAY);
        
        dateChooser.setDate(new java.util.Date());
        
        la_memo = new JLabel("메모 :");
    	tf_memo = new JTextField();
    	tf_memo.setPreferredSize(new Dimension(200, 30));
    	tf_memo.setFont(new Font("SansSerif", Font.PLAIN, 14));
    	tf_memo.setBackground(Config.LIGHT_GRAY);
    	tf_memo.setForeground(Color.DARK_GRAY);
        
        la_approver = new JLabel("결재자 :");
        tf_approver = new JTextField();
        tf_approver.setPreferredSize(new Dimension(120, 30));
        tf_approver.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tf_approver.setBackground(Config.LIGHT_GRAY);
        tf_approver.setForeground(Color.DARK_GRAY);
        tf_approver.setEditable(false); // 수정 불가능하게
        
//        loadApproverList(cb_approver);

        bt_save = new JButton("저장");
        bt_save.setPreferredSize(new Dimension(80, 30));
        bt_save.setBackground(Config.PINK);

        // 저장 버튼 클릭 이벤트
        bt_save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	saveInboundRequest();
//                saveInboundRequest(dateChooser, cb_approver);
            }
        });
        
        comboPanel.add(la_date);
        comboPanel.add(dateChooser);
        comboPanel.add(la_approver);
        comboPanel.add(tf_approver);
        comboPanel.add(la_memo);
        comboPanel.add(tf_memo);
        comboPanel.add(bt_save);

        p_bottom.add(comboPanel);
        
		
		// 스타일
        
		//new Dimension(Config.CONTENT_W, Config.CONTENT_H) // 1100, 740 -> 550, 740
//		p_center.setPreferredSize(new Dimension(1100, 300));
        p_north.setPreferredSize(new Dimension(Config.CONTENT_W , 50));
        p_north.setBackground(Config.WHITE);
        
		p_center.setBackground(Config.WHITE);
		
		Dimension d = new Dimension(Config.CONTENT_W / 2 - 10, 620);
		
		
		p_list.setBorder(new EmptyBorder(0, 20, 0, 0)); // 패딩
		p_list.setPreferredSize(new Dimension(Config.CONTENT_W / 2 + 100, Config.CONTENT_H - 180));
		p_list.setBackground(Config.WHITE);
		p_list.add(scroll);
		
		p_request.setPreferredSize(new Dimension(Config.CONTENT_W / 2 - 110, Config.CONTENT_H - 180));
		p_request.setBackground(Config.WHITE);
		

		p_bottom.setBorder(new EmptyBorder(20, 20, 20, 20)); // 패딩
		p_bottom.setPreferredSize(new Dimension(Config.CONTENT_W , 50));
		p_bottom.setBackground(Config.WHITE);
		
		// 조립
		
		la_left.setBorder(BorderFactory.createEmptyBorder(0, 300, 0, 0));   // 왼쪽 padding
		la_right.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 160));  // 오른쪽 padding

		p_north.setLayout(new BorderLayout());
		p_north.add(la_left, BorderLayout.WEST);
		p_north.add(la_right, BorderLayout.EAST);
		
		p_list.add(scroll);
		p_request.add(scroll_re);

        // 중앙 패널에 SplitPane 추가       
        p_center.add(p_list, BorderLayout.WEST);
        p_center.add(p_request);
		
		p_bottom.add(comboPanel);
		
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
                    setTableWidth(table); // 테이블 너비 재설정
                    
                    System.out.println("브랜치 " + branch.getBr_id());
                    
                    // ✅ 선택된 지점의 점장 이름 불러오기
                    User manager = userDAO.getManagerByBranchId(branch.getBr_id());
                    if (manager != null) {
                        tf_approver.setText(manager.getUser_name());
                        tf_approver.setToolTipText(manager.getUser_id() + " / " + manager.getUser_name());
                        
                        // 전역변수에 결재자(점장) 객체 넣기
                        currentApprover = manager;
                        
                    } else {
                        tf_approver.setText("점장 없음");
                    }
                }            	
            }
        });
		
		setPreferredSize(new Dimension(Config.CONTENT_W, Config.CONTENT_H-70));
		setBackground(Config.WHITE);
		
		int userId = mainLayout.user.getUser_id();
		this.userDAO = new UserDAO(); // ✅ NPE 방지: 반드시 먼저 생성!
		loadCategories(userId);
	}
	
	 // 카테고리 목록 불러오기
    private void loadCategories(int userId) {
    	 branchDAO = new BranchDAO();
    	    List<Branch> branchList = branchDAO.getBranchList(userId);

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
    	        InboundModel inboundModel = new InboundModel(firstBranch);
    	        table.setModel(inboundModel);

    	        setTableWidth(table);
    	    }
    }
    
    private void loadApproverList(JComboBox<User> cb_approver) {
        userDAO = new UserDAO();
        List<User> userList = userDAO.selectAll();
        cb_approver.addItem(null); // 선택 안했을 때 default
        for (User user : userList) {
            cb_approver.addItem(user);
        }
    }
    
    private void saveInboundRequest() {
    //(JDateChooser dateChooser, JComboBox<User> cb_approver) {
        java.util.Date selectedDate = dateChooser.getDate();
        
        

        if (selectedDate == null) {
            JOptionPane.showMessageDialog(this, "입고일을 선택하세요");
            return;
        }

        // 오늘 날짜와 비교
        java.util.Date today = new java.util.Date();

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(today);
        cal1.set(Calendar.HOUR_OF_DAY, 0);
        cal1.set(Calendar.MINUTE, 0);
        cal1.set(Calendar.SECOND, 0);
        cal1.set(Calendar.MILLISECOND, 0);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(selectedDate);
        cal2.set(Calendar.HOUR_OF_DAY, 0);
        cal2.set(Calendar.MINUTE, 0);
        cal2.set(Calendar.SECOND, 0);
        cal2.set(Calendar.MILLISECOND, 0);

        if (!cal2.after(cal1)) { // 오늘 포함 이전이면 거부
            JOptionPane.showMessageDialog(this, "입고일은 '내일 이후'만 선택 가능합니다.");
            return;
        }

        if (currentApprover == null) {
            JOptionPane.showMessageDialog(this, "결재자가 지정되지 않았습니다.");
            return;
        }

        if (boundProductModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "입고 요청할 상품이 없습니다.");
            return;
        }
        
        if(tf_memo == null) {
        	JOptionPane.showMessageDialog(this, "메모가 입력되지 않았습니다.");
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
        
        
        // 👉 데이터 준비
//        java.sql.Date sqlDate = new java.sql.Date(selectedDate.getTime());
//        String comment = tf_memo.getText().trim();
//        List<BoundProduct> productList = boundProductModel.getProductList(); // ✅ BoundProduct 리스트를 반환하는 메서드 필요
//
//        // 👉 DAO 호출
//        InboundDAO inboundDAO = new InboundDAO();
//        inboundDAO.insertInbound(currentApprover.getUser_id(), sqlDate, comment, productList);
        int userId = currentApprover.getUser_id();
        int brId = ((Branch) cb_branch.getSelectedItem()).getBr_id();
        Date sqlDate = new Date(dateChooser.getDate().getTime());
        String comment = tf_memo.getText().trim();
        List<BoundProduct> productList = boundProductModel.getProductList();

        InboundDAO dao = new InboundDAO();
        dao.insertInbound(userId, brId, sqlDate, comment, productList);

        // ✅ 저장 후 UI 초기화
        JOptionPane.showMessageDialog(this, "입고 요청이 저장되었습니다.");

        boundProductModel.clear(); // 테이블 초기화용 clear() 메서드 필요
        tf_memo.setText("");
        
        
        JOptionPane.showMessageDialog(this, "입고 요청이 저장되었습니다.");
//        requestModel.clear();
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
}