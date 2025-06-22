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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

import com.olive.bound.dialog.ProductAddDialog;
import com.olive.bound.model.BoundShowModel;
import com.olive.bound.model.InboundListModel;
import com.olive.bound.model.InboundModel;
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
    
    BoundProduct selected; // 선택된 요청서 객체

    private static InboundShowPanel instance; // ✅ 정적 필드 추가
    
    public InboundShowPanel(MainLayout mainLayout) {
        super(mainLayout);
        setLayout(new BorderLayout());
        
        instance = this; // ✅ 생성자에서 자기 자신 저장
        
        this.mainLayout = mainLayout;
		this.user = mainLayout.user;
		int userId = user.getUser_id();
        
		// ------------------------------------------------------------
        // 로그인 계정의 지점 리스트 가져오기
        branchDAO = new BranchDAO();
        userBranches = branchDAO.getBranchList(user.getUser_id());

        // 공통 색상 및 폰트
        Color bgColor = new Color(245, 248, 250);
        Color comboColor = new Color(100, 149, 237); // Cornflower Blue
        Font defaultFont = new Font("SansSerif", Font.PLAIN, 13);

        setBackground(Config.WHITE);

        // ------------------------------------------------------------
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


        // ------------------------------------------------------------
        // 중앙 패널
        p_center = new JPanel(new BorderLayout());
        
        // 리스트 테이블 생성
        bound = new Bound();
        
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
        
        
        
        
        
        
        // ------------------------------------------------------------
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
        cb_appuser.setEnabled(false); // 비활성화
        
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
        
        // 오늘 날짜 기준으로 내일 날짜 설정
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, 1); // 내일
        Date tomorrow = cal.getTime();
        
        // 내일로 설정
        dateChooser.setMinSelectableDate(tomorrow);
        
        // 입력 필드 스타일
        JTextField editor = (JTextField) dateChooser.getDateEditor().getUiComponent();
        editor.setToolTipText("오늘 날짜 이후만 선택이 가능합니다.");
        editor.setBackground(Config.WHITE);
        editor.setFont(new Font("SansSerif", Font.PLAIN, 13));

        // 달력 버튼 스타일
        JButton calendarButton = dateChooser.getCalendarButton();
        calendarButton.setBackground(Config.WHITE);
        calendarButton.setFocusPainted(false);
        calendarButton.setOpaque(true);
        calendarButton.setPreferredSize(new Dimension(30, 20));
        
        calendarButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                calendarButton.setBackground(Config.GREEN);
            }
            public void mouseExited(MouseEvent evt) {
                calendarButton.setBackground(Config.WHITE);
            }
        });

        // 메모
        t_memo = new JTextField();
        t_memo.setPreferredSize(new Dimension(Config.CONTENT_W / 2 - 80, 30));
        JLabel la_memo = new JLabel("메모:");
        la_memo.setPreferredSize(new Dimension(80, 30));
        p_detail.add(la_memo);
        p_detail.add(t_memo);
        
        // ------------------------------------------------------------
        // 상품 추가 버튼 패널 (오른쪽 정렬)
        JPanel addButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        addButtonPanel.setPreferredSize(new Dimension(Config.CONTENT_W / 2 + 10, 35));
        addButtonPanel.setBackground(Config.WHITE);

        JButton bt_add = new JButton("+");
        bt_add.setPreferredSize(new Dimension(42, 30));
        bt_add.setBackground(Config.LIGHT_GRAY);
        bt_add.setFont(new Font("SansSerif", Font.PLAIN, 13));
        bt_add.setEnabled(false); // 목록 선택 전에는 비활성화

        addButtonPanel.add(bt_add);
        p_detail.add(addButtonPanel);
        
        // ------------------------------------------------------------
        // 상품, 상품코드, 요청수량 table
        
        model_detail = new BoundShowModel();
        table_detail = new JTable(model_detail);
 		
 		// 테이블 헤더 스타일
 		table_detail.setRowHeight(25);
 		table_detail.setFont(new Font("SansSerif", Font.PLAIN, 13));
 		table_detail.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
 		table_detail.getTableHeader().setBackground(Config.LIGHT_GREEN); // 테이블 헤더 배경색 설정
 		table_detail.getTableHeader().setForeground(Color.DARK_GRAY);
 		
 		centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table_detail.getColumnCount(); i++) {
        	table_detail.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        scrollPane = new JScrollPane(table_detail);
        scrollPane.getViewport().setBackground(Config.WHITE);
        
        scrollPane.setPreferredSize(new Dimension(Config.CONTENT_W / 2 + 10, Config.CONTENT_H / 2- 30));
        p_detail.add(scrollPane); // 패널에 추가      
        
        p_center.add(p_detail);
        
        // 전체 레이아웃 구성
        add(topPanel, BorderLayout.NORTH);
//        add(Box.createHorizontalStrut(10));
        add(p_center, BorderLayout.CENTER);
        
        
        
        // ------------------------------------------------------------
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

        cb_branch.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    Branch selectedBranch = (Branch) cb_branch.getSelectedItem();
                    if (selectedBranch == null || selectedBranch.getBr_id() == 0) return;
                    
                    System.out.println(selectedBranch);

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
        
        // JTable 클릭 이벤트 처리
        table_list.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		int row = table_list.getSelectedRow();
        		if (row != -1) {
        			selected = model.getBoundAt(row);
        			showDetail(selected);
        			bt_add.setEnabled(true); // 상품 추가 버튼 활성화
        		}
        	}
        });
        
        
        bt_add.addActionListener(e -> {
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            ProductAddDialog dialog = new ProductAddDialog(parentFrame, selected);
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true); // 여기서 다이얼로그 실행됨

            List<BoundProduct> updatedList = dialog.getSelectedProducts();
            if (updatedList != null) {
                List<BoundProduct> filteredList = updatedList.stream()
                    .filter(bp -> bp.getB_count() > 0)
                    .toList();
                model_detail.setBoundProductList(filteredList);
            }
        });

        
        bt_add.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				bt_add.setBackground(Config.GREEN);
			};

			public void mouseExited(MouseEvent e) {
				bt_add.setBackground(Config.LIGHT_GRAY);
			};
		});
        
        bt_delete.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				deleteInbound(selected);
			}
			
			public void mouseEntered(MouseEvent e) {
				bt_delete.setBackground(Config.GREEN);
			};

			public void mouseExited(MouseEvent e) {
				bt_delete.setBackground(Config.LIGHT_GRAY);
			};
		});
        
        bt_save.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				saveInbound(selected);
			}
			
			public void mouseEntered(MouseEvent e) {
				bt_save.setBackground(Config.GREEN);
			};

			public void mouseExited(MouseEvent e) {
				bt_save.setBackground(Config.LIGHT_GRAY);
			};
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
            if (!userBranch.equals(requestBranch)) {
                cb_branch.addItem(userBranch);
            }
        }

        // 요청서 지점을 선택 상태로
        cb_branch.setSelectedItem(requestBranch);
        
        
        // 결재자
        cb_appuser.removeAllItems();
        User approver = bound.getApprover();
        if (approver != null) {
            cb_appuser.addItem(approver);
            cb_appuser.setSelectedItem(approver);
        }
        
    }
    
    private void deleteInbound(BoundProduct boundProduct) {
    	if (boundProduct == null) return;

        int confirm = javax.swing.JOptionPane.showConfirmDialog(
            null,
            "정말로 선택한 입고 요청서를 삭제하시겠습니까?",
            "삭제 확인",
            javax.swing.JOptionPane.YES_NO_OPTION
        );

        if (confirm == javax.swing.JOptionPane.YES_OPTION) {
            int boundId = boundProduct.getBound().getBound_id();
            inboundDAO.deleteInbound(boundId);

            javax.swing.JOptionPane.showMessageDialog(null, "입고 요청서가 삭제되었습니다.");

            // 목록 새로고침
            refreshStaticList();
        }
    }

    private void saveInbound(BoundProduct boundProduct) {
        if (selected == null) return;

        // 1. 현재 선택된 요청서 정보 추출
        Bound currentBound = selected.getBound();
        User newApprover = (User) cb_appuser.getSelectedItem();
        Date newRequestDate = dateChooser.getDate();
        String newMemo = t_memo.getText().trim();
        List<BoundProduct> newProductList = model_detail.getBoundProductList();
        
        Branch newBranch = (Branch) cb_branch.getSelectedItem();
        if (newBranch == null || newBranch.getBr_id() == 0) {
            JOptionPane.showMessageDialog(null, "지점을 선택해주세요.");
            return;
        }

        // 2. 기존 정보와 비교하여 변경 여부 판단
        boolean isModified = false;

        // 지점, 결재자, 날짜, 메모 비교
        if (!currentBound.getBranch().equals(newBranch) ||
            !currentBound.getApprover().equals(newApprover) ||
            !currentBound.getRequest_date().equals(newRequestDate) ||
            !currentBound.getComment().equals(newMemo)) {
            isModified = true;
        }

        // 상품 리스트 비교 (수량 또는 제품 ID 변경 여부)
        List<BoundProduct> oldProductList = inboundDAO.selectBoundProductListByBoundId(currentBound.getBound_id());
        if (oldProductList.size() != newProductList.size()) {
            isModified = true;
        } else {
            for (int i = 0; i < newProductList.size(); i++) {
                BoundProduct newBP = newProductList.get(i);
                boolean match = false;
                for (BoundProduct oldBP : oldProductList) {
                    if (newBP.getProductOption().getOption_id() == oldBP.getProductOption().getOption_id() &&
                        newBP.getB_count() == oldBP.getB_count()) {
                        match = true;
                        break;
                    }
                }
                if (!match) {
                    isModified = true;
                    break;
                }
            }
        }

        // 3. 변경사항 없으면 메시지
        if (!isModified) {
            JOptionPane.showMessageDialog(null, "변경된 사항이 없습니다.");
            return;
        }
        
        for (BoundProduct bp : newProductList) {
            if (bp.getProductOption() == null) {
                System.err.println("productOption이 null입니다. bp: " + bp);
                return;
            }
            System.out.println("option_id: " + bp.getProductOption().getOption_id());
        }

        // 4. 변경사항이 있으면 저장
        currentBound.setBranch(newBranch);
        currentBound.setApprover(newApprover);
        currentBound.setRequest_date(newRequestDate);
        currentBound.setComment(newMemo);

        // 요청서 업데이트
        inboundDAO.updateBound(currentBound);

        // 요청 상품들 업데이트
        inboundDAO.deleteBoundProductsByBoundId(currentBound.getBound_id());
        
        for (BoundProduct bp : newProductList) {
            bp.setBound(currentBound); // bound_id 설정
            inboundDAO.insertBoundProduct(bp);
        }

        JOptionPane.showMessageDialog(null, "요청서가 성공적으로 저장되었습니다.");

        // 테이블 새로고침
        refreshStaticList();
    }

    // 테이블 새로고침을 위함
    public static void refreshStaticList() {
        if (instance != null) {
            instance.refreshList(); // ✅ 내부 리프레시 메서드 호출
        }
    }

    public void refreshList() {
    	this.model = new InboundListModel(userBranches);
        table_list.setModel(model);
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