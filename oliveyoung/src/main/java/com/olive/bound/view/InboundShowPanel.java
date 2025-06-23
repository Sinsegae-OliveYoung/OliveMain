package com.olive.bound.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

<<<<<<< HEAD
=======
import com.olive.bound.dialog.ProductAddDialog;
import com.olive.bound.model.BoundShowModel;
import com.olive.bound.model.InboundListModel;
import com.olive.bound.model.InboundModel;
>>>>>>> parent of 5c4965e (feat : finished to Outbound request & list)
import com.olive.common.config.Config;
import com.olive.common.model.Bound;
import com.olive.common.model.BoundProduct;
import com.olive.common.model.Branch;
<<<<<<< HEAD
import com.olive.common.repository.InboundDAO;
=======
import com.olive.common.model.User;
import com.olive.common.repository.BranchDAO;
import com.olive.common.repository.InboundDAO;
import com.olive.common.repository.UserDAO;
>>>>>>> parent of 5c4965e (feat : finished to Outbound request & list)
import com.olive.common.view.Panel;
import com.olive.mainlayout.MainLayout;
import com.toedter.calendar.JDateChooser;

public class InboundShowPanel extends Panel{
	// 상단
	JPanel topPanel;
	JPanel rightButtonPanel;
	JLabel titleLabel;
	JComboBox<Branch> cb_branch;
	JComboBox<Branch> cb_appuser;
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
	
<<<<<<< HEAD
=======
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
>>>>>>> parent of 5c4965e (feat : finished to Outbound request & list)

	Bound bound;
	InboundListModel model;
	BoundShowModel model_detail;
    InboundDAO inboundDAO = new InboundDAO();

    public InboundShowPanel(MainLayout mainLayout) {
        super(mainLayout);
        setLayout(new BorderLayout());

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
//        model = new InboundListModel(bound);  // # 입고 요청서 model 연결
        
<<<<<<< HEAD
        model = new InboundListModel("now");  // # 입고 요청서 model 연결
=======
        model = new InboundListModel(userBranches);
>>>>>>> parent of 5c4965e (feat : finished to Outbound request & list)
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
        la_branch = new JLabel("지점 : "); 
        la_branch.setPreferredSize(new Dimension(80, 30));
        p_detail.add(la_branch);
        p_detail.add(cb_branch);

        // 결재자 선택
        cb_appuser = new JComboBox<>();
        cb_appuser.setPreferredSize(new Dimension(Config.CONTENT_W / 2 - 80, 30));
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
        
                
        
        
<<<<<<< HEAD
     // JTable 클릭 이벤트 처리
        table_list.addMouseListener(new MouseAdapter() {
=======
        // ------------------------------------------------------------
        // 테이블 헤더 클릭 이벤트 추가
//        JTableHeader header_re = table_detail.getTableHeader();
//        header_re.addMouseListener(new java.awt.event.MouseAdapter() {
//        	@Override
//        	public void mouseClicked(java.awt.event.MouseEvent e) {
//        		int columnIndex = header_re.columnAtPoint(e.getPoint());
//        		String columnName = table_detail.getColumnName(columnIndex);
//        		System.out.println("헤더 클릭됨: " + columnName + " (인덱스: " + columnIndex + ")");
//        		
//        		// 예: 제품명 컬럼 클릭시만 처리
//        		if ("상품명".equals(columnName)) {
//        			javax.swing.JOptionPane.showMessageDialog(null, "상품명 컬럼 클릭됨");
//        		}
//        	}
//        });
     // 1. 정렬 기능 설정
        TableRowSorter<TableModel> sorter_list = new TableRowSorter<>(table_list.getModel());
        table_list.setRowSorter(sorter_list);

        // 2. 헤더 클릭 이벤트로 정렬 상태 출력
        JTableHeader header_list = table_list.getTableHeader();
        header_list.addMouseListener(new MouseAdapter() {
>>>>>>> parent of 5c4965e (feat : finished to Outbound request & list)
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table_list.getSelectedRow();
                if (row != -1) {
                    BoundProduct selected = model.getBoundAt(row);
                    showDetail(selected);
                }
            }
        });
<<<<<<< HEAD
=======
        
        // JTable 클릭 이벤트 처리
        table_list.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
//        		int row = table_list.getSelectedRow();
        		int viewRow = table_list.getSelectedRow();
        		if (viewRow != -1) {
        			int modelRow = table_list.convertRowIndexToModel(viewRow); // ✅ 핵심
        			selected = model.getBoundAt(modelRow);
        			showDetail(selected);
        			bt_add.setEnabled(true); // 상품 추가 버튼 활성화
        			
        			
        			originalProductList = inboundDAO
        					.selectBoundProductListByBoundId(selected.getBound().getBound_id())
        					.stream()
        					.map(bp -> {
        						BoundProduct copy = new BoundProduct();
        						copy.setB_count(bp.getB_count());
        						copy.setProductOption(bp.getProductOption()); // option_id 기반 비교용
        						return copy;
        					})
        					.collect(Collectors.toList());
        		}
        		
        	}
        });

        bt_add.addActionListener(e -> {
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            ProductAddDialog dialog = new ProductAddDialog(parentFrame, selected);
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true); // 다이얼로그 실행

            // ✅ 추가 버튼을 눌러서 확정한 경우에만 반영
            if (dialog.isConfirmed()) {
                List<BoundProduct> updatedList = dialog.getSelectedProducts();
                if (updatedList != null && !updatedList.isEmpty()) {
                    List<BoundProduct> filteredList = updatedList.stream()
                        .filter(bp -> bp.getB_count() > 0)
                        .toList();
                    model_detail.setBoundProductList(filteredList);
                }
            }
            // ❌ bt_close로 닫았거나 아무것도 선택 안 했으면 기존 데이터 유지
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
>>>>>>> parent of 5c4965e (feat : finished to Outbound request & list)

    }
    
    
    private void showDetail(BoundProduct boundProduct) {
        Bound bound = boundProduct.getBound();

        cb_branch.setSelectedItem(bound.getBranch());
        cb_appuser.setSelectedItem(bound.getUser());
        dateChooser.setDate(bound.getRequest_date());
        t_memo.setText(bound.getComment());
        
        System.out.println(bound.getComment());

<<<<<<< HEAD
        // DAO에서 해당 bound_id의 상세 상품 리스트 조회
        List<BoundProduct> boundProductList = inboundDAO.selectBoundProductListByBoundId(bound.getBound_id());
        model_detail.setBoundProductList(boundProductList);
=======
        // 상품 리스트 불러오기
        boundProductList = inboundDAO.selectBoundProductListByBoundId(bound.getBound_id());
        model_detail.setBoundProductList(boundProductList);

        // 요청서에 연결된 지점
        Branch requestBranch = bound.getBranch();

        // userBranches 중 이름이 같은 지점을 찾아 대체 (정상 br_id 포함된 객체로)
        for (Branch b : userBranches) {
            if (b.getBr_name().equals(requestBranch.getBr_name())) {
                requestBranch = b;
                break;
            }
        }

        cb_branch.removeAllItems();
        cb_branch.addItem(requestBranch);

        for (Branch userBranch : userBranches) {
            if (userBranch.getBr_id() != requestBranch.getBr_id()) {
                cb_branch.addItem(userBranch);
            }
        }

        cb_branch.setSelectedItem(requestBranch);

        
        Branch selectedBranch = (Branch) cb_branch.getSelectedItem();       
        
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
        
        // 기존의 요청서
        Bound currentBound = selected.getBound();
        
        // 수정된 요청서
        User newApprover = (User) cb_appuser.getSelectedItem();
        Date newRequestDate = dateChooser.getDate();
        String newMemo = t_memo.getText().trim();
        List<BoundProduct> newProductList = model_detail.getBoundProductList();
        
        Branch newBranch = (Branch) cb_branch.getSelectedItem();
        if (newBranch == null) {
            JOptionPane.showMessageDialog(null, "지점을 선택해주세요.");
            return;
        }

        boolean isBoundModified = false;
        boolean isProductModified = false;

        // Bound 정보 비교
        if (!currentBound.getBranch().equals(newBranch) ||
            !currentBound.getApprover().equals(newApprover) ||
            !currentBound.getRequest_date().equals(newRequestDate) ||
            !currentBound.getComment().equals(newMemo)) {
            isBoundModified = true;
        }

        // 상품 리스트 비교
        if (originalProductList.size() != newProductList.size()) {
            isProductModified = true;
        } else {
            for (BoundProduct newBP : newProductList) {
                boolean found = false;
                for (BoundProduct originalBP : originalProductList) {
                    if (newBP.getProductOption().getOption_id() == originalBP.getProductOption().getOption_id()
                        && newBP.getB_count() == originalBP.getB_count()) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    isProductModified = true;
                    break;
                }
            }
        }

        // 아무것도 변경 안 됐으면
        if (!isBoundModified && !isProductModified) {
            JOptionPane.showMessageDialog(null, "변경된 사항이 없습니다.");
            return;
        }
        
        for (BoundProduct bp : newProductList) {
            if (bp.getProductOption() == null) {
                System.err.println("productOption이 null입니다. bp: " + bp);
                return;
            }
        }
        // --- 3. 사용자 확인 ---
        int totalCount = newProductList.stream().mapToInt(BoundProduct::getB_count).sum();
        int totalPrice = newProductList.stream()
            .mapToInt(bp -> bp.getB_count() * bp.getProductOption().getPrice())
            .sum();

        String requesterName = user.getUser_name();
        String approverName = newApprover.getUser_name();
        String requestDateStr = new SimpleDateFormat("yyyy-MM-dd").format(newRequestDate);

        boolean confirmed = showConfirmationDialog(requesterName, approverName, totalCount, totalPrice, requestDateStr);
        if (!confirmed) return;

        // 4. 변경사항이 있으면 저장
        if (isBoundModified) {
            currentBound.setBranch(newBranch);
            currentBound.setApprover(newApprover);
            currentBound.setRequest_date(newRequestDate);
            currentBound.setComment(newMemo);
            inboundDAO.updateBound(currentBound);
        }

        if (isProductModified) {
            inboundDAO.deleteBoundProductsByBoundId(currentBound.getBound_id());
            for (BoundProduct bp : newProductList) {
                bp.setBound(currentBound);
                inboundDAO.insertBoundProduct(bp);
            }
        }

        JOptionPane.showMessageDialog(null, "요청서가 성공적으로 저장되었습니다.");

        // 테이블 새로고침
        refreshStaticList();
    }
    
    
    // 요청서 저장 확인 폼
    private boolean showConfirmationDialog(String requesterName, String approverName, int totalCount, int totalPrice, String requestDate) {
        String message = String.format(
            "<html><body>" +
            "<b>입고 요청 정보를 확인해주세요.</b><br><br>" +
            "요청자 :&nbsp;&nbsp;&nbsp;%s<br><br>" +
            "결재자 :&nbsp;&nbsp;&nbsp;%s<br><br>" +
            "총 상품 수량 :&nbsp;&nbsp;&nbsp;%d개<br><br>" +
            "총 상품 금액 :&nbsp;&nbsp;&nbsp;%,d원<br><br>" +
            "입고 요청일 :&nbsp;&nbsp;&nbsp;%s<br><br><br>" +
            "<b>정말 요청하시겠습니까?</b>" +
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
    	this.model = new InboundListModel(userBranches);
        table_list.setModel(model);
        table_list.revalidate();
        table_list.repaint();

        // 상세내용 초기화
        model_detail.setBoundProductList(List.of());
        t_memo.setText("");
        dateChooser.setDate(null);
        cb_branch.setSelectedIndex(-1);
        cb_appuser.setSelectedIndex(-1);
>>>>>>> parent of 5c4965e (feat : finished to Outbound request & list)
    }
}