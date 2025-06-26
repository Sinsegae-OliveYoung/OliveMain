package com.olive.product.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.olive.common.config.Config;
import com.olive.common.model.Brand;
import com.olive.common.model.Category;
import com.olive.common.model.CategoryDetail;
import com.olive.common.model.User;
import com.olive.common.repository.BrandDAO;
import com.olive.common.repository.CategoryDAO;
import com.olive.common.repository.CategoryDetailDAO;
import com.olive.common.util.style.ButtonUtil;
import com.olive.common.util.style.ComboBoxUtil;
import com.olive.common.view.Panel;
import com.olive.mainlayout.MainLayout;
import com.olive.product.model.BrandModel;
import com.olive.product.model.CategoryModel;


public class BrandPanel extends Panel {
	// NORTH
	JPanel p_north;
	
	// CENTER
	JPanel p_center;
	
	// LEFT 카테고리 테이블
	JPanel p_cate;
	JLabel la_cate;
	
	JTableHeader header;
	JTable table;
	JScrollPane scroll;
	
	// RIGHT 브랜드 테이블
	JPanel p_brand;
	JLabel la_brand;
	
	JTableHeader header_br;
	JTable table_br;
	JScrollPane scroll_br;
	
	// BOTTOM
	JPanel p_bottom;
	
	// 카테고리 하단
	JPanel comboPanel_1;
	JLabel la_ca1;
	JLabel la_ca2;
	JLabel la_ca3;
	JLabel la_ca4;
	JComboBox<Category> cb_cate;
	JTextField tf_ca1;
	JTextField tf_ca2;
	JTextField tf_ca3;
	JButton bt_cate;
	
	// 브랜드 하단
	JPanel comboPanel_2;
	JLabel la_br1;
	JLabel la_br2;
	JTextField tf_br1;
	JTextField tf_br2;
	JButton bt_brand;
	JButton bt_brand_del;
	
	CategoryModel categoryModel;
	BrandModel brandModel;
	
	CategoryDAO categoryDAO = new CategoryDAO();
	CategoryDetailDAO categoryDetailDAO = new CategoryDetailDAO();
    BrandDAO brandDAO = new BrandDAO();

	DefaultTableCellRenderer centerRenderer; // 테이블 정렬
	
	MainLayout mainLayout;
	User user; // 로그인한 계정 객체

	public BrandPanel(MainLayout mainLayout) {
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
        p_north.setBackground(Config.WHITE);
        
        // 좌측 상단 패널
        p_cate = new JPanel();
        la_cate = new JLabel("카테고리 목록");
        la_cate.setFont(new Font("SansSerif", Font.BOLD, 22));
        
        // 우측 패널
        p_brand = new JPanel();
        la_brand = new JLabel("브랜드 목록");
        la_brand.setFont(new Font("SansSerif", Font.BOLD, 22));
        
        la_cate.setBorder(BorderFactory.createEmptyBorder(0, 300, 0, 0));   // 왼쪽 padding
        la_brand.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 160));  // 오른쪽 padding

        p_north.setPreferredSize(new Dimension(Config.CONTENT_W , 50));
        p_north.setBackground(Config.WHITE);
        
        
        p_north.setLayout(new BorderLayout());
		p_north.add(la_cate, BorderLayout.WEST);
		p_north.add(la_brand, BorderLayout.EAST);
        
        
        // 중앙 패널 ------------------------------------------------------------
        p_center = new JPanel(new BorderLayout());
        p_center.setBackground(Config.WHITE);
		
        // ------------------------------------------------------------
		// 좌측 중앙 - 테이블
        categoryModel = new CategoryModel();
        table = new JTable(categoryModel); // 카테고리 테이블

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
        scroll.setPreferredSize(new Dimension(Config.CONTENT_W / 2 + 80, Config.CONTENT_H - 300));

        
        
        
        
        p_cate.setBorder(new EmptyBorder(0, 20, 0, 0)); // 패딩
        p_cate.setPreferredSize(new Dimension(Config.CONTENT_W / 2 + 100, Config.CONTENT_H - 300));
        p_cate.setBackground(Config.WHITE);
        p_cate.add(scroll);
		
        comboPanel_1 = new JPanel();
        comboPanel_1.setLayout(new BoxLayout(comboPanel_1, BoxLayout.Y_AXIS));
        comboPanel_1.setOpaque(false);

        // ---------- 1행: 카테고리 ----------
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        row1.setOpaque(false);

        la_ca1 = new JLabel("카테고리 :");
        la_ca1.setPreferredSize(new Dimension(90, 30));
        la_ca1.setFont(new Font("SansSerif", Font.BOLD, 12));

        cb_cate = ComboBoxUtil.createCategoryComboBox();
        cb_cate.setPreferredSize(new Dimension(200, 30));
        cb_cate.setFont(new Font("SansSerif", Font.PLAIN, 14));
        cb_cate.setBackground(Config.WHITE);
        cb_cate.setForeground(Color.DARK_GRAY);
        cb_cate.setBorder(new LineBorder(Color.GRAY, 1, true));
        cb_cate.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cb_cate.setFocusable(false);
        
        la_ca3 = new JLabel("카테고리 코드 :");
        la_ca3.setPreferredSize(new Dimension(110, 30));
        la_ca3.setFont(new Font("SansSerif", Font.BOLD, 12));
        
        tf_ca2 = new JTextField();
        tf_ca2.setPreferredSize(new Dimension(200, 30));
        tf_ca2.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tf_ca2.setBackground(Config.LIGHT_GRAY);
        tf_ca2.setForeground(Color.DARK_GRAY);
        tf_ca2.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
        tf_ca2.setEnabled(false);

        row1.add(la_ca1);
        row1.add(cb_cate);
        row1.add(la_ca3);
        row1.add(tf_ca2);

        // ---------- 2행: 상세 카테고리 ----------
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        row2.setOpaque(false);

        la_ca2 = new JLabel("상세 카테고리 :");
        la_ca2.setPreferredSize(new Dimension(90, 30));
        la_ca2.setFont(new Font("SansSerif", Font.BOLD, 12));

        tf_ca1 = new JTextField();
        tf_ca1.setPreferredSize(new Dimension(200, 30));
        tf_ca1.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tf_ca1.setBackground(Config.LIGHT_GRAY);
        tf_ca1.setForeground(Color.DARK_GRAY);
        tf_ca1.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
        
        la_ca4 = new JLabel("상세 카테고리 코드 :");
        la_ca4.setPreferredSize(new Dimension(110, 30));
        la_ca4.setFont(new Font("SansSerif", Font.BOLD, 12));

        tf_ca3 = new JTextField();
        tf_ca3.setPreferredSize(new Dimension(200, 30));
        tf_ca3.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tf_ca3.setBackground(Config.LIGHT_GRAY);
        tf_ca3.setForeground(Color.DARK_GRAY);
        tf_ca3.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));

        row2.add(la_ca2);
        row2.add(tf_ca1);
        row2.add(la_ca4);
        row2.add(tf_ca3);

        // ---------- 3행: 버튼 ----------
        JPanel row3 = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        row3.setOpaque(false);

        bt_cate = ButtonUtil.greenButtonUtil("카테고리 저장"); 
        bt_cate.setPreferredSize(new Dimension(120, 30));

        row3.add(bt_cate);

        // ---------- 조립 ----------
        comboPanel_1.add(row1);
        comboPanel_1.add(row2);
        comboPanel_1.add(row3);
        
        p_cate.add(comboPanel_1);
  
        
        la_ca1.setFont(new Font("SansSerif", Font.BOLD, 12));
        la_ca2.setFont(new Font("SansSerif", Font.BOLD, 12));
        
        
        // ------------------------------------------------------------
        // 우측 브랜드 테이블
        brandModel = new BrandModel();
		table_br = new JTable(brandModel); // 브랜드 테이블
		
		// 테이블 헤더 스타일
		table_br.setRowHeight(25);
		table_br.setFont(new Font("SansSerif", Font.PLAIN, 13));
		table_br.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
		table_br.getTableHeader().setBackground(Config.LIGHT_GREEN); // 테이블 헤더 배경색 설정
		table_br.getTableHeader().setForeground(Color.DARK_GRAY);
		table_br.getTableHeader().setToolTipText("'0'을 입력 시 등록한 요청 상품이 삭제됩니다.");
        
        // 테이블 셀 가운데 정렬
        centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table_br.getColumnCount(); i++) {
        	table_br.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        table_br.getColumnModel().getColumn(0).setPreferredWidth(120);
        table_br.getColumnModel().getColumn(1).setPreferredWidth(50);

        scroll_br = new JScrollPane(table_br);
        scroll_br.getViewport().setBackground(Config.WHITE);
        scroll_br.setPreferredSize(new Dimension(Config.CONTENT_W / 2 - 150, Config.CONTENT_H - 300));
        
        p_brand.setPreferredSize(new Dimension(Config.CONTENT_W / 2 - 110, Config.CONTENT_H - 300));
        p_brand.setBackground(Config.WHITE);
        
        p_brand.add(scroll_br);
        
        // 중앙 부착
        p_center.add(p_cate, BorderLayout.WEST);
        p_center.add(p_brand);
        
        
        comboPanel_2 = new JPanel();
        comboPanel_2.setLayout(new BoxLayout(comboPanel_2, BoxLayout.Y_AXIS));
        comboPanel_2.setOpaque(false);

        // ---------- 1행: 브랜드 ----------
        JPanel b_row1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        b_row1.setOpaque(false);

        la_br1 = new JLabel("브랜드명 :");
        la_br1.setFont(new Font("SansSerif", Font.BOLD, 12));
        la_br1.setPreferredSize(new Dimension(90, 30));

        tf_br1 = new JTextField();
        tf_br1.setPreferredSize(new Dimension(200, 30));
        tf_br1.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tf_br1.setBackground(Config.LIGHT_GRAY);
        tf_br1.setForeground(Color.DARK_GRAY);
        tf_br1.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));

        b_row1.add(la_br1);
        b_row1.add(tf_br1);

        // ---------- 2행: 브랜드 코드 ----------
        JPanel b_row2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        b_row2.setOpaque(false);

        la_br2 = new JLabel("브랜드 코드 :");
        la_br2.setFont(new Font("SansSerif", Font.BOLD, 12));
        la_br2.setPreferredSize(new Dimension(90, 30));

        tf_br2 = new JTextField();
        tf_br2.setPreferredSize(new Dimension(200, 30));
        tf_br2.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tf_br2.setBackground(Config.LIGHT_GRAY);
        tf_br2.setForeground(Color.DARK_GRAY);
        tf_br2.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));

        b_row2.add(la_br2);
        b_row2.add(tf_br2);

        // ---------- 3행: 버튼 ----------
        JPanel b_row3 = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0)); 
        b_row3.setOpaque(false);

        bt_brand_del = ButtonUtil.pinkButtonUtil("브랜드 삭제"); 
        bt_brand_del.setPreferredSize(new Dimension(120, 30));
        
        bt_brand = ButtonUtil.greenButtonUtil("브랜드 저장"); 
        bt_brand.setPreferredSize(new Dimension(120, 30));

        b_row3.add(bt_brand_del);
        b_row3.add(bt_brand);

        // ---------- 조립 ----------
        comboPanel_2.add(b_row1);
        comboPanel_2.add(b_row2);
        comboPanel_2.add(b_row3);
        
        p_brand.add(comboPanel_2);
  

        // ------------------------------------------------------------
        // 최종 패널에 부착
        add(p_north, BorderLayout.NORTH);
        add(p_center, BorderLayout.CENTER);


		
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
		
		// 테이블 생성 이후 ------------------------------------------------------------
		TableRowSorter<TableModel> sorter_br = new TableRowSorter<>(table_br.getModel());
		table_br.setRowSorter(sorter_br);

		// 헤더 클릭 이벤트로 정렬 상태 출력
		header_br = table_br.getTableHeader();
		header_br.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		        int columnIndex = header_br.columnAtPoint(e.getPoint());
		        String columnName = table_br.getColumnName(columnIndex);
		        System.out.println("헤더 클릭됨: " + columnName + " (인덱스: " + columnIndex + ")");

		        SortOrder order = getSortOrder(sorter_br, columnIndex);
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
		
		// 좌측 테이블 컬럼 클릭 이벤트 추가 ------------------------------------------------------------
		table.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		        int viewRow = table.getSelectedRow();
		        if (viewRow != -1) {
		            int modelRow = table.convertRowIndexToModel(viewRow); // 정렬 상태 보정

		            // 모델에서 CategoryDetail 객체 가져오기
		            CategoryDetail cd = categoryModel.getCategoryDetail(modelRow);

		            // 콤보박스 선택 설정
		            cb_cate.setSelectedItem(cd.getCategory());

		            // 텍스트 필드 설정
		            tf_ca2.setText(String.valueOf(cd.getCategory().getCt_code())); // 카테고리 코드
		            tf_ca1.setText(cd.getCt_dt_name());                            // 상세 카테고리명
		            tf_ca3.setText(String.valueOf(cd.getCt_dt_code()));            // 상세 카테고리 코드
		        }
		    }
		});
		
		// 우측 테이블 컬럼 클릭 이벤트 추가 ------------------------------------------------------------
		table_br.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		        int viewRow = table_br.getSelectedRow();
		        if (viewRow != -1) {
		            int modelRow = table_br.convertRowIndexToModel(viewRow); // 정렬 보정
		            Brand brand = brandModel.getRow(modelRow); // 모델에서 올바른 데이터 추출

		            // 텍스트필드에 값 설정
		            tf_br1.setText(brand.getBd_name());
		            tf_br2.setText(brand.getBd_code());
		        }
		    }
		});
		
		//카테고리 콤보박스 선택 연결 이벤트 ------------------------------------------------------------
		cb_cate.addActionListener(e -> {
		    Category selected = (Category) cb_cate.getSelectedItem();
		    if (selected != null) {
		        tf_ca2.setText(String.valueOf(selected.getCt_code()));
		    } else {
		        tf_ca2.setText("");
		    }
		});

		//카테고리 저장 버튼 클릭 이벤트 ------------------------------------------------------------
		bt_cate.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				saveCategory();
			}
		});
		
		
		// 브랜드 저장 버튼 클릭 이벤트 ------------------------------------------------------------
		bt_brand.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				saveBrand();
			}
		});
		
		bt_brand_del.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				delBrand();
			}
		});

		setPreferredSize(new Dimension(Config.CONTENT_W, Config.CONTENT_H-70));
		setBackground(Config.WHITE);
		
	}
	
	private void saveCategory() {
		   String ct_dt_name = tf_ca1.getText().trim();
		    String ct_dt_code = tf_ca3.getText().trim();

		    if (cb_cate.getSelectedItem() == null) {
		        JOptionPane.showMessageDialog(this, "카테고리를 선택하세요.", "입력 오류", JOptionPane.WARNING_MESSAGE);
		        return;
		    }
		    
		    Category selectedCategory = (Category) cb_cate.getSelectedItem();
		    int ct_id = selectedCategory.getCt_id();  // 콤보박스에서 선택된 Category의 ID
		    String ct_name = selectedCategory.getCt_name();


		    if (ct_dt_name.isEmpty() || ct_dt_code.isEmpty()) {
		        JOptionPane.showMessageDialog(this, "상세 카테고리명과 코드를 입력하세요.", "입력 오류", JOptionPane.WARNING_MESSAGE);
		        return;
		    }

		    // 중복 검사 (상세 카테고리만)
		    List<CategoryDetail> list = categoryModel.getCategoryDetailList();

		    boolean isDuplicate = list.stream().anyMatch(cd ->
		           cd.getCt_dt_name().equalsIgnoreCase(ct_dt_name)
		    );

		    if (isDuplicate) {
		        JOptionPane.showMessageDialog(this, "이미 존재하는 상세 카테고리명 또는 코드입니다.", "중복 오류", JOptionPane.WARNING_MESSAGE);
		        return;
		    }

		    // CategoryDetail 저장
		    CategoryDetail cd = new CategoryDetail();
		    cd.setCt_dt_name(ct_dt_name);
		    cd.setCt_dt_code(ct_dt_code);
		    cd.setCategory(selectedCategory); // ct_id 포함됨

		    int result = categoryDetailDAO.insert(cd);

		    if (result > 0) {
		        JOptionPane.showMessageDialog(this, "상세 카테고리가 성공적으로 저장되었습니다.");
		        refresh();
		        mainLayout.setDataDirty(true);
		        mainLayout.refreshIfDirty();
		    } else {
		        JOptionPane.showMessageDialog(this, "저장에 실패했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
		    }
	}

	private void saveBrand() {		
	    String bd_name = tf_br1.getText().trim();
	    String bd_code = tf_br2.getText().trim();

	    // 1. 유효성 검사
	    if (bd_name.isEmpty() || bd_code.isEmpty()) {
	        JOptionPane.showMessageDialog(this, "브랜드명과 브랜드 코드를 모두 입력하세요.", "입력 오류", JOptionPane.WARNING_MESSAGE);
	        return;
	    }

	    // 2. 중복 검사
	    List<Brand> existingBrands = brandModel.getBrandList(); // brandModel 내부 리스트 접근

	    boolean nameExists = existingBrands.stream()
	        .anyMatch(b -> b.getBd_name().equalsIgnoreCase(bd_name));
	    if (nameExists) {
	        JOptionPane.showMessageDialog(this, "이미 존재하는 브랜드명입니다.", "중복 오류", JOptionPane.WARNING_MESSAGE);
	        return;
	    }

	    boolean codeExists = existingBrands.stream()
	        .anyMatch(b -> b.getBd_code().equalsIgnoreCase(bd_code));
	    if (codeExists) {
	        JOptionPane.showMessageDialog(this, "이미 존재하는 브랜드 코드입니다.", "중복 오류", JOptionPane.WARNING_MESSAGE);
	        return;
	    }

	    // 3. DB 저장
	    Brand newBrand = new Brand();
	    newBrand.setBd_name(bd_name);
	    newBrand.setBd_code(bd_code);

	    int result = brandDAO.insert(newBrand);

	    if (result > 0) {
	        JOptionPane.showMessageDialog(this, "브랜드가 성공적으로 저장되었습니다.", "저장 완료", JOptionPane.INFORMATION_MESSAGE);
	        brandDAO.load(); // DB에서 다시 불러오기
	        table_br.updateUI();

	        tf_br1.setText("");
	        tf_br2.setText("");
	        
	        // 테이블 새로고침
	        refresh();
	        
	        mainLayout.setDataDirty(true); 
	        mainLayout.refreshIfDirty();
	    } else {
	        JOptionPane.showMessageDialog(this, "브랜드 저장에 실패했습니다.", "저장 실패", JOptionPane.ERROR_MESSAGE);
	    }
	}
	
	
	private void delBrand() {
	    String bd_name = tf_br1.getText().trim();
	    String bd_code = tf_br2.getText().trim();

	    // 1. 유효성 검사
	    if (bd_name.isEmpty() || bd_code.isEmpty()) {
	        JOptionPane.showMessageDialog(this, "삭제할 브랜드를 먼저 선택하거나 입력하세요.", "입력 오류", JOptionPane.WARNING_MESSAGE);
	        return;
	    }

	    // 2. 기존 목록에서 해당 브랜드가 있는지 확인
	    List<Brand> brandList = brandModel.getBrandList();
	    Brand matchedBrand = brandList.stream()
	        .filter(b -> b.getBd_name().equalsIgnoreCase(bd_name) && b.getBd_code().equalsIgnoreCase(bd_code))
	        .findFirst()
	        .orElse(null);

	    if (matchedBrand == null) {
	        JOptionPane.showMessageDialog(this, "해당 브랜드가 목록에 존재하지 않습니다.", "삭제 오류", JOptionPane.WARNING_MESSAGE);
	        return;
	    }

	    // 3. 사용자 확인 후 삭제 진행
	    int choice = JOptionPane.showConfirmDialog(this,
	            "정말로 '" + bd_name + "' 브랜드를 삭제하시겠습니까?",
	            "삭제 확인",
	            JOptionPane.YES_NO_OPTION,
	            JOptionPane.WARNING_MESSAGE);

	    if (choice == JOptionPane.YES_OPTION) {
	        int result = brandDAO.delete(matchedBrand.getBd_id());

	        if (result > 0) {
	            JOptionPane.showMessageDialog(this, "브랜드가 성공적으로 삭제되었습니다.", "삭제 완료", JOptionPane.INFORMATION_MESSAGE);

	            // 입력 필드 초기화
	            tf_br1.setText("");
	            tf_br2.setText("");

	            // 테이블 새로고침
	            refresh();

	            mainLayout.setDataDirty(true);
	            mainLayout.refreshIfDirty();
	        } else {
	            JOptionPane.showMessageDialog(this, "브랜드 삭제에 실패했습니다.", "삭제 실패", JOptionPane.ERROR_MESSAGE);
	        }
	    }
	}


    
    @Override
    public void refresh() {
    	// 콤보박스 초기화
        cb_cate.setSelectedIndex(0); // 첫 번째 선택

        // 입력 필드 초기화
        tf_ca1.setText("");
        tf_ca2.setText("");
        tf_ca3.setText("");
        tf_br1.setText("");
        tf_br2.setText("");

        // 좌측 카테고리 테이블 데이터 리로드
        categoryModel.clear();
        List<CategoryDetail> cdList = categoryDetailDAO.selectAll();
        categoryModel.setList(cdList);
        table.setModel(categoryModel);

        // 우측 브랜드 테이블 데이터 리로드
        brandModel.clear();
        List<Brand> brList = brandDAO.selectAll();
        brandModel.setList(brList);
        table_br.setModel(brandModel);

        // 테이블 다시 그리기
        table.revalidate();
        table.repaint();
        table_br.revalidate();
        table_br.repaint();
    }
}