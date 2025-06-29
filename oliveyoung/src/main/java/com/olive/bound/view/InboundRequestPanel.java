package com.olive.bound.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
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
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.olive.bound.model.BoundProductModel;
import com.olive.bound.model.BoundRequestModel;
import com.olive.common.config.Config;
import com.olive.common.model.BoundProduct;
import com.olive.common.model.Branch;
import com.olive.common.model.Stock;
import com.olive.common.model.User;
import com.olive.common.repository.BoundDAO;
import com.olive.common.repository.BranchDAO;
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
	
	JTableHeader header;
	JTable table;
	JScrollPane scroll;
	
	// RIGHT 입고 요청 폼
	JPanel p_request;
	JLabel la_right;
	
	JTableHeader header_re;
	JTable table_re;
	JScrollPane scroll_re;
	
	// BOTTOM
	JPanel p_bottom;
	
	JPanel comboPanel;
	JComboBox<Branch> cb_branch;
	JLabel la_approver;
	JTextField tf_approver;
	Date selectedDate;
	JLabel la_date;
	JDateChooser dateChooser;
	JLabel la_memo;
	JTextField tf_memo;
	JButton bt_save;
	
	UserDAO userDAO;
	ProductDAO productDAO;
	BranchDAO branchDAO;
	BoundDAO insertDAO;
	
	BoundRequestModel model; // 왼쪽 테이블 클릭시 우측테이블로 데이터 전송을 위한 모델 생성
	BoundRequestModel boundModel;
	BoundProductModel boundProductModel;
	DefaultTableCellRenderer centerRenderer; // 테이블 정렬
	
	MainLayout mainLayout;
	User user; // 로그인한 계정 객체
	User manager; // 로그인한 계정 지점의 점주(role = 2)
	List<BoundProduct> productList;
	
	public InboundRequestPanel(MainLayout mainLayout) {
		super(mainLayout);
		setLayout(new BorderLayout());
		
		this.mainLayout = mainLayout;
		this.user = mainLayout.user;
		int userId = user.getUser_id();
		
		// 공통 색상 및 폰트
        Color bgColor = new Color(245, 248, 250);
        Color comboColor = new Color(100, 149, 237);
        Font defaultFont = new Font("SansSerif", Font.PLAIN, 13);

        // 상단 패널 ------------------------------------------------------------
        p_north = new JPanel(new BorderLayout());
        
        // 좌측 상단 패널
        p_list = new JPanel();
        la_left = new JLabel("상품 목록");
        la_left.setFont(new Font("SansSerif", Font.BOLD, 22));
        
        // 우측 패널
        p_request = new JPanel();
        la_right = new JLabel("입고 물품 목록");
        la_right.setFont(new Font("SansSerif", Font.BOLD, 22));        
        
        
        // 중앙 패널 ------------------------------------------------------------
        p_center = new JPanel(new BorderLayout());
		
		// 좌측 중앙 - 테이블
		table = new JTable(new BoundRequestModel("now")); // 입고할 상품 리스트 테이블에 출력		
		
		// 테이블 헤더 스타일
		table.setRowHeight(25);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        table.getTableHeader().setBackground(Config.LIGHT_GREEN); // 테이블 헤더 배경색 설정
        table.getTableHeader().setForeground(Color.DARK_GRAY);
        table.getTableHeader().setToolTipText("상품을 클릭하면 상품이 요청서에 추가됩니다.");
        
        // 테이블 셀 가운데 정렬
        centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(Config.WHITE);
        scroll.setPreferredSize(new Dimension(Config.CONTENT_W / 2 + 80, Config.CONTENT_H - 180));

        
        
        // ------------------------------------------------------------
        boundProductModel = new BoundProductModel();
		table_re = new JTable(boundProductModel); // 입고 요청서 테이블
		
		// 테이블 헤더 스타일
		table_re.setRowHeight(25);
		table_re.setFont(new Font("SansSerif", Font.PLAIN, 13));
		table_re.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
		table_re.getTableHeader().setBackground(Config.LIGHT_GREEN); // 테이블 헤더 배경색 설정
		table_re.getTableHeader().setForeground(Color.DARK_GRAY);
		table_re.getTableHeader().setToolTipText("'0'을 입력 시 등록한 요청 상품이 삭제됩니다.");
        
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

				
				
				
		// 하단 지점 선택 - 콤보박스 ------------------------------------------------------------
        // 기존 p_bottom 내부 교체
        p_bottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));

        comboPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        comboPanel.setOpaque(false);

        cb_branch = new JComboBox<>();
        cb_branch.setPreferredSize(new Dimension(200, 30));
        cb_branch.setFont(new Font("SansSerif", Font.PLAIN, 14));
        cb_branch.setBackground(Config.WHITE);
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
        cal.setTime(new Date());
        cal.add(Calendar.DATE, 1); // 내일
        Date tomorrow = cal.getTime();
        
        // 내일로 설정
        dateChooser.setMinSelectableDate(tomorrow);
        dateChooser.setDate(tomorrow); // 기본값도 내일로

        // 달력 버튼 스타일
        JButton calendarButton = dateChooser.getCalendarButton();
        calendarButton.setBackground(Config.LIGHT_GRAY);
        calendarButton.setFocusPainted(false);
        calendarButton.setOpaque(true);
        calendarButton.setPreferredSize(new Dimension(30, 20));
        
        calendarButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                calendarButton.setBackground(Config.GREEN);
            }
            public void mouseExited(MouseEvent evt) {
                calendarButton.setBackground(Config.LIGHT_GRAY);
            }
        });

        la_approver = new JLabel("결재자 :");
        tf_approver = new JTextField();
        tf_approver.setPreferredSize(new Dimension(120, 30));
        tf_approver.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tf_approver.setBackground(Config.LIGHT_GRAY);
        tf_approver.setForeground(Color.DARK_GRAY);
        tf_approver.setEditable(false); // 수정 불가능하게
        tf_approver.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
        
        la_memo = new JLabel("메모 :");
    	tf_memo = new JTextField();
    	tf_memo.setPreferredSize(new Dimension(200, 30));
    	tf_memo.setFont(new Font("SansSerif", Font.PLAIN, 14));
    	tf_memo.setBackground(Config.WHITE);
    	tf_memo.setForeground(Color.DARK_GRAY);
        

        bt_save = new JButton("저장");
        bt_save.setPreferredSize(new Dimension(80, 30));
        bt_save.setBackground(Config.LIGHT_GRAY);

        // 하단 패널 부착
        comboPanel.add(la_date);
        comboPanel.add(dateChooser);
        comboPanel.add(la_approver);
        comboPanel.add(tf_approver);
        comboPanel.add(la_memo);
        comboPanel.add(tf_memo);
        comboPanel.add(bt_save);

        p_bottom.add(comboPanel);
        
		
		// 스타일 ------------------------------------------------------------
        p_north.setPreferredSize(new Dimension(Config.CONTENT_W , 50));
        p_north.setBackground(Config.WHITE);
        
		p_center.setBackground(Config.WHITE);
		
		
		p_list.setBorder(new EmptyBorder(0, 20, 0, 0)); // 패딩
		p_list.setPreferredSize(new Dimension(Config.CONTENT_W / 2 + 100, Config.CONTENT_H - 180));
		p_list.setBackground(Config.WHITE);
		p_list.add(scroll);
		
		p_request.setPreferredSize(new Dimension(Config.CONTENT_W / 2 - 110, Config.CONTENT_H - 180));
		p_request.setBackground(Config.WHITE);
		

		p_bottom.setBorder(new EmptyBorder(20, 20, 20, 20)); // 패딩
		p_bottom.setPreferredSize(new Dimension(Config.CONTENT_W , 50));
		p_bottom.setBackground(Config.WHITE);
		
		
		
		// 조립 ------------------------------------------------------------
		la_left.setBorder(BorderFactory.createEmptyBorder(0, 300, 0, 0));   // 왼쪽 padding
		la_right.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 160));  // 오른쪽 padding

		p_north.setLayout(new BorderLayout());
		p_north.add(la_left, BorderLayout.WEST);
		p_north.add(la_right, BorderLayout.EAST);
		
		p_list.add(scroll);
		p_request.add(scroll_re);
     
        p_center.add(p_list, BorderLayout.WEST);
        p_center.add(p_request);
		
		p_bottom.add(comboPanel);
		
		add(p_north, BorderLayout.NORTH);
		add(p_center, BorderLayout.CENTER);
		add(p_bottom, BorderLayout.SOUTH);
		
		
		
		
		
		// 좌측 테이블 헤더 클릭 이벤트 추가 ------------------------------------------------------------
		TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
		table.setRowSorter(sorter);

		// 헤더 클릭 감지 및 정렬 상태 출력
		header = table.getTableHeader();
		header.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		        int columnIndex = header.columnAtPoint(e.getPoint());
		        String columnName = table.getColumnName(columnIndex);
		        System.out.println("헤더 클릭됨: " + columnName + " (인덱스: " + columnIndex + ")");
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
		header_re = table_re.getTableHeader();
		header_re.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		        int columnIndex = header_re.columnAtPoint(e.getPoint());
		        String columnName = table_re.getColumnName(columnIndex);
		        System.out.println("헤더 클릭됨: " + columnName + " (인덱스: " + columnIndex + ")");
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
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int viewRow = table.getSelectedRow();  // 화면상 클릭한 행
				if (viewRow >= 0) {
					int modelRow = table.convertRowIndexToModel(viewRow);  // 실제 모델 인덱스

		            // 모델에서 정확한 데이터 가져오기
		            model = (BoundRequestModel) table.getModel();
		            Stock selectedStock = model.list.get(modelRow); // ✅ 반드시 modelRow 사용

		            BoundProduct bp = new BoundProduct();
		            bp.setProductOption(selectedStock.getProductOption());
		            bp.setB_count(1); // 초기 수량

		            boundProductModel.addProduct(bp);
				}
			}
		});
		
		table_re.getModel().addTableModelListener(e -> {
		    if (e.getType() == TableModelEvent.UPDATE) {
		        int row = e.getFirstRow();
		        int col = e.getColumn();

		        if (col == 2) { // 요청수량 컬럼
		            BoundProductModel model = (BoundProductModel) table_re.getModel();

		            int newQuantity;
		            try {
		                newQuantity = Integer.parseInt(model.getValueAt(row, col).toString());
		            } catch (NumberFormatException ex) {
		                return;
		            }

		            if (newQuantity == 0) {
		                model.removeRow(row); // ✨ 요청 수량이 0이면 행 삭제
		                return;
		            }
		        }
		    }
		});
		
		// 저장 버튼 클릭 이벤트 ------------------------------------------------------------
		bt_save.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				saveBoundRequest(userId);
			}
			
			public void mouseEntered(MouseEvent e) {
				bt_save.setBackground(Config.GREEN);
			};

			public void mouseExited(MouseEvent e) {
				bt_save.setBackground(Config.LIGHT_GRAY);
			};
		});
		
		// 지점 콤보박스 이벤트 연결 ------------------------------------------------------------
		cb_branch.addItemListener(new ItemListener() {
		    @Override
		    public void itemStateChanged(ItemEvent e) {
		        if (e.getStateChange() == ItemEvent.SELECTED) {
		            Branch selectedBranch = (Branch) e.getItem();

		            // 요청서에 상품이 하나 이상 담긴 경우만 확인창 표시
		            if (boundProductModel.getRowCount() > 0) {
		                int result = JOptionPane.showConfirmDialog(
		                    null,
		                    "요청서에 담긴 상품이 삭제됩니다. 변경하시겠습니까?",
		                    "지점 변경 확인",
		                    JOptionPane.YES_NO_OPTION
		                );

		                if (result != JOptionPane.YES_OPTION) {
		                    // 콤보박스 선택 이전으로 되돌리기 (무한루프 방지 위해 removeListener → 재등록)
		                    cb_branch.removeItemListener(this);
		                    cb_branch.setSelectedItem(e.getItemSelectable().getSelectedObjects()[0]);
		                    cb_branch.addItemListener(this);
		                    return;
		                }
		            }

		            // ✅ 변경 처리
		            if (selectedBranch != null) {
		                boundModel = new BoundRequestModel(selectedBranch);
		                table.setModel(boundModel);
		                setTableWidth(table);

		                // 요청서 초기화
		                boundProductModel.clear();

		                // 결재자 재설정
		                manager = userDAO.getManagerByBranchId(selectedBranch.getBr_id());
		                if (manager != null) {
		                    tf_approver.setText(manager.getUser_name());
		                    tf_approver.setToolTipText(manager.getUser_id() + " / " + manager.getUser_name());
		                } else {
		                    tf_approver.setText("점장 없음");
		                    tf_approver.setToolTipText(null);
		                }
		            }
		        }
		    }
		});

		
		setPreferredSize(new Dimension(Config.CONTENT_W, Config.CONTENT_H-70));
		setBackground(Config.WHITE);
		
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
	    	boundModel = new BoundRequestModel(firstBranch);
	        table.setModel(boundModel);

	        setTableWidth(table);
	    }
    }

    private void saveBoundRequest(int userId) {
 
        selectedDate = dateChooser.getDate();

        if (selectedDate == null) {
            JOptionPane.showMessageDialog(this, "입고일을 선택하세요");
            return;
        }

        // 오늘 날짜와 비교
        Date today = new Date();

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
        if (manager == null) {
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
			insertDAO = new BoundDAO();
			insertDAO.insertInbound(userId, managerId, brId, requestDate, memo, productList);
			
			
			// ✅ 저장 후 UI 초기화
			boundProductModel.clear(); // 테이블 초기화용 clear() 메서드 필요
			tf_memo.setText("");
			
			refresh();
			
			mainLayout.setDataDirty(true); 
	        mainLayout.refreshIfDirty();
			
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
    
    @Override
    public void refresh() {
        // 콤보박스 선택값 초기화
        cb_branch.setSelectedIndex(0); // 첫 번째 지점 선택

        // 테이블 모델 새로고침
        Branch selectedBranch = (Branch) cb_branch.getSelectedItem();
        if (selectedBranch != null) {
        	boundModel = new BoundRequestModel(selectedBranch);
            table.setModel(boundModel);
            setTableWidth(table); // 컬럼 너비 재설정
        }

        // 테이블 다시 그리기
        table.revalidate();
        table.repaint();

    	table.updateUI();
    }
}