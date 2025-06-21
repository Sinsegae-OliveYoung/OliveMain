package com.olive.bound.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

import com.olive.common.config.Config;
import com.olive.common.model.Bound;
import com.olive.common.model.BoundProduct;
import com.olive.common.model.Branch;
import com.olive.common.model.User;
import com.olive.common.repository.BranchDAO;
import com.olive.common.repository.InboundDAO;
import com.olive.common.repository.UserDAO;
import com.olive.common.view.Panel;
import com.olive.mainlayout.MainLayout;
import com.toedter.calendar.JDateChooser;

public class InboundShowPanel extends Panel{
	// 상단
	JPanel topPanel;
	JPanel rightButtonPanel;
	JLabel titleLabel;
	JComboBox<Branch> cb_branch;
	JComboBox<User> cb_appuser;
	JButton bt_delete;
	JButton bt_save;
	
	// 중앙
	JPanel p_center;
	
	// 좌측 요청서 목록
	JPanel p_left;
	JTable table_list;
	JScrollPane scroll_list;
	
	// 우측 요청서 상세 조회
	JPanel p_detail;
	JDateChooser dateChooser;
	JLabel la_branch;
    JLabel la_appuser;
    JLabel la_date;
    JTextField t_memo;
    JTable table_detail;
    JScrollPane scrollPane;
	
    User user;
	Bound bound;
	InboundModel inboundModel;
	InboundListModel model;
	BoundShowModel model_detail;
	
	UserDAO userDAO;
    InboundDAO inboundDAO = new InboundDAO();
    BranchDAO branchDAO;
    List<Branch> branchList; // 지점 목록
    List<BoundProduct> boundProductList; // 상품 목록
    List<Branch> userBranches; // 사용자 소유 지점 목록

    private static InboundShowPanel instance; // ✅ 정적 필드 추가
    
    public InboundShowPanel(MainLayout mainLayout) {
        super(mainLayout);
        setLayout(new BorderLayout());
        
        instance = this; // ✅ 생성자에서 자기 자신 저장
        this.user = mainLayout.user;
        
        // 로그인 계정의 지점 리스트 가져오기
        branchDAO = new BranchDAO();
        userBranches = branchDAO.getBranchList(user.getUser_id());

        // 공통 색상 및 폰트
        Color bgColor = new Color(245, 248, 250);
        Color comboColor = new Color(100, 149, 237); // Cornflower Blue
        Font defaultFont = new Font("SansSerif", Font.PLAIN, 13);

        setBackground(Config.WHITE);

        // 상단 패널
        topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Config.WHITE);
        topPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
        
        rightButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        rightButtonPanel.setBackground(Config.WHITE);

        // 제목 라벨
        titleLabel = new JLabel("입고요청서 List");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        titleLabel.setForeground(new Color(40, 40, 40));
        titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
        
        // 상단 삭제 버튼
        bt_delete = new JButton("삭제");
        bt_delete.setPreferredSize(new Dimension(80, 30));
        bt_delete.setBackground(Config.LIGHT_GRAY);
        
        // 상단 저장 버튼
        bt_save = new JButton("저장");
        bt_save.setPreferredSize(new Dimension(80, 30));
        bt_save.setBackground(Config.LIGHT_GRAY);
        
        // 상단 패널에 요소 부착
        rightButtonPanel.add(bt_save);
        rightButtonPanel.add(bt_delete);
        rightButtonPanel.setBorder(new EmptyBorder(0, 0, 0, 40));

        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(rightButtonPanel, BorderLayout.EAST);

        
        // 중앙 패널
        p_center = new JPanel(new BorderLayout());
        
        // 리스트 테이블 생성
        bound = new Bound();
        
//        model = new InboundListModel("now");  // # 입고 요청서 model 연결
//        table_list = new JTable(model);
        
        model = new InboundListModel(userBranches);
        table_list = new JTable(model);

        // 리스트 테이블 헤더 스타일
        table_list.setRowHeight(25);
        table_list.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table_list.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        table_list.getTableHeader().setBackground(Config.LIGHT_GREEN); // 테이블 헤더 배경색 설정
        table_list.getTableHeader().setForeground(Color.DARK_GRAY);

        // 리스트 테이블 셀 가운데 정렬
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table_list.getColumnCount(); i++) {
        	table_list.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        scroll_list = new JScrollPane(table_list);
        scroll_list.getViewport().setBackground(Config.WHITE);
        scroll_list.setPreferredSize(new Dimension(Config.CONTENT_W / 2 - 120, Config.CONTENT_H - 160));
        
        p_left = new JPanel();
        p_left.setBorder(new EmptyBorder(0, 20, 0, 0)); // 패딩
        p_left.setBackground(Config.WHITE);
        p_left.setPreferredSize(new Dimension(Config.CONTENT_W / 2 - 100, Config.CONTENT_H - 160));
        p_left.add(scroll_list);
        
        p_center.add(p_left, BorderLayout.WEST);
        
        
        
        
        
        
        
        // 오른쪽 패널
        p_detail = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 15));
        p_detail.setBackground(Config.WHITE); 
        p_detail.setBorder(new EmptyBorder(0, 20, 20, 20)); // 패딩

        // 지점 선택
        cb_branch = new JComboBox<>();
        cb_branch.setPreferredSize(new Dimension(Config.CONTENT_W / 2 - 80, 30));
        cb_branch.setBackground(Config.WHITE);
        
        la_branch = new JLabel("지점 : "); 
        la_branch.setPreferredSize(new Dimension(80, 30));
        p_detail.add(la_branch);
        p_detail.add(cb_branch);

        // 결재자 선택
        cb_appuser = new JComboBox<User>();
        cb_appuser.setPreferredSize(new Dimension(Config.CONTENT_W / 2 - 80, 30));
        cb_appuser.setBackground(Config.WHITE);
//        cb_appuser.setEnabled(false); // 비활성화
        
        cb_appuser.setToolTipText("지점 변경 시 자동 설정됩니다.");
        la_appuser = new JLabel("결재자 : ");
        la_appuser.setPreferredSize(new Dimension(80, 30));
        p_detail.add(la_appuser);
        p_detail.add(cb_appuser);

        // 요청일
        dateChooser = new JDateChooser();
        dateChooser.setPreferredSize(new Dimension(Config.CONTENT_W / 2 - 80, 30));
        la_date = new JLabel("요청일 : ");
        la_date.setPreferredSize(new Dimension(80, 30));
        p_detail.add(la_date);
        p_detail.add(dateChooser);

        // 메모
        t_memo = new JTextField();
        t_memo.setPreferredSize(new Dimension(Config.CONTENT_W / 2 - 80, 30));
        JLabel la_memo = new JLabel("메모:");
        la_memo.setPreferredSize(new Dimension(80, 30));
        p_detail.add(la_memo);
        p_detail.add(t_memo);
        
        
        
        // 상품, 상품코드, 요청수량 table
        
        model_detail = new BoundShowModel();
        table_detail = new JTable(model_detail);
        
        // 테이블 헤더 클릭 이벤트 추가
 		JTableHeader header_re = table_detail.getTableHeader();
 		header_re.addMouseListener(new java.awt.event.MouseAdapter() {
 		    @Override
 		    public void mouseClicked(java.awt.event.MouseEvent e) {
 		        int columnIndex = header_re.columnAtPoint(e.getPoint());
 		        String columnName = table_detail.getColumnName(columnIndex);
 		        System.out.println("헤더 클릭됨: " + columnName + " (인덱스: " + columnIndex + ")");
 		        
 		        // 예: 제품명 컬럼 클릭시만 처리
 		        if ("상품명".equals(columnName)) {
 		            javax.swing.JOptionPane.showMessageDialog(null, "상품명 컬럼 클릭됨");
 		        }
 		    }
 		});
 		
 		// 테이블 헤더 스타일
 		table_detail.setRowHeight(25);
 		table_detail.setFont(new Font("SansSerif", Font.PLAIN, 13));
 		table_detail.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
 		table_detail.getTableHeader().setBackground(Config.LIGHT_GREEN); // 테이블 헤더 배경색 설정
 		table_detail.getTableHeader().setForeground(Color.DARK_GRAY);
        
        scrollPane = new JScrollPane(table_detail);
        scrollPane.getViewport().setBackground(Config.WHITE);
        
        scrollPane.setPreferredSize(new Dimension(Config.CONTENT_W / 2 + 10, Config.CONTENT_H / 2 + 20));
        p_detail.add(scrollPane); // 패널에 추가      
        
        
        
        p_center.add(p_detail);
        
        // 전체 레이아웃 구성
        add(topPanel, BorderLayout.NORTH);
//        add(Box.createHorizontalStrut(10));
        add(p_center, BorderLayout.CENTER);
        
                
     // JTable 클릭 이벤트 처리
        table_list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table_list.getSelectedRow();
                if (row != -1) {
                    BoundProduct selected = model.getBoundAt(row);
                    showDetail(selected);
                }
            }
        });
        
        
        cb_branch.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    Branch selectedBranch = (Branch) cb_branch.getSelectedItem();
                    if (selectedBranch == null || selectedBranch.getBr_id() == 0) return;

                    // ✅ 해당 지점의 점장 불러오기
                    UserDAO userDAO = new UserDAO();
                    User manager = userDAO.getManagerByBranchId(selectedBranch.getBr_id());

                    // ✅ cb_appuser 초기화 및 업데이트
                    cb_appuser.removeAllItems();
                    if (manager != null) {
                        cb_appuser.addItem(manager);
                        cb_appuser.setSelectedItem(manager);
                    } else {
                        User dummy = new User();
                        dummy.setUser_name("점장 없음");
                        cb_appuser.addItem(dummy);
                        cb_appuser.setSelectedItem(dummy);
                    }
                }
            }
        });

    }
    
    
    private void showDetail(BoundProduct boundProduct) {
    	bound = boundProduct.getBound();

        cb_appuser.setSelectedItem(bound.getApprover());
        dateChooser.setDate(bound.getRequest_date());
        t_memo.setText(bound.getComment());

        // 상품 리스트 불러오기
        boundProductList = inboundDAO.selectBoundProductListByBoundId(bound.getBound_id());
        model_detail.setBoundProductList(boundProductList);

        // 요청서에 연결된 지점
        Branch requestBranch = bound.getBranch();

        // 콤보박스 초기화
        cb_branch.removeAllItems();

        // 요청서 지점 먼저 추가
        if (requestBranch != null) {
            cb_branch.addItem(requestBranch);
        }

        // 로그인 계정 지점 리스트 중 중복되지 않은 지점만 추가
        for (Branch userBranch : userBranches) {
            if (requestBranch == null || !userBranch.equals(requestBranch)) {
                cb_branch.addItem(userBranch);
            }
        }

        // 요청서 지점을 선택 상태로
        cb_branch.setSelectedItem(requestBranch);
        
        
        // 결재자
        cb_appuser.removeAllItems();
        User approver = bound.getApprover();
        System.out.println(approver.getUser_name());
        if (approver != null) {
            cb_appuser.addItem(approver);
            cb_appuser.setSelectedItem(approver);
        }
        
    }
    
    
    // 테이블 새로고침을 위함
    public static void refreshStaticList() {
        if (instance != null) {
            instance.refreshList(); // ✅ 내부 리프레시 메서드 호출
        }
    }

    public void refreshList() {
    	this.model = new InboundListModel(userBranches);
        table_list = new JTable(model);
//        this.model = new InboundListModel("now");
//        table_list.setModel(model);
        table_list.revalidate();
        table_list.repaint();

        // 상세내용 초기화
        model_detail.setBoundProductList(List.of());
        t_memo.setText("");
        dateChooser.setDate(null);
        cb_branch.setSelectedIndex(-1);
        cb_appuser.setSelectedIndex(-1);
    }
}