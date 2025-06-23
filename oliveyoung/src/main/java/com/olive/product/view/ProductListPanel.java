package com.olive.product.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.olive.common.config.Config;
import com.olive.common.exception.ProductException;
import com.olive.common.model.Brand;
import com.olive.common.model.Category;
import com.olive.common.model.CategoryDetail;
import com.olive.common.model.Product;
import com.olive.common.model.ProductOption;
import com.olive.common.repository.BrandDAO;
import com.olive.common.repository.CategoryDAO;
import com.olive.common.repository.CategoryDetailDAO;
import com.olive.common.repository.ProductDAO;
import com.olive.common.repository.ProductOptionDAO;
import com.olive.common.util.DBManager;
import com.olive.common.util.TableUtil;
import com.olive.common.util.style.LabelUtil;
import com.olive.common.view.Panel;
import com.olive.mainlayout.MainLayout;
import com.olive.product.model.ProductModel;
import com.olive.stock.StockConfig;

public class ProductListPanel extends Panel {

    JTable table;
    ProductModel model;
    
    JTextField tfName;
    JTextField tfPrice;
    JTextField tfOptionName;
   
    JComboBox<String> cbActive;
    JComboBox<Brand> cbBrand;
    
    JComboBox<Category> cbCategory;
    JComboBox<CategoryDetail> cbCategoryDetail;
    
    ProductDAO productDAO;
    ProductOptionDAO productOptionDAO;
    CategoryDetailDAO categoryDetailDAO;
    DBManager dbManager = DBManager.getInstance();
    
    @Override
    public void refresh() {
    	model.reload();     // ListModel에서 최신 데이터 로드
        table.updateUI();   // 테이블 UI 갱신
    }

    public ProductListPanel(MainLayout mainLayout) {
        super(mainLayout);
        setLayout(new BorderLayout());
        
        productDAO = new ProductDAO();
        productOptionDAO = new ProductOptionDAO();
        categoryDetailDAO = new CategoryDetailDAO();

        Color bgColor = new Color(245, 248, 250);
        Font defaultFont = new Font("SansSerif", Font.PLAIN, 13);

        setBackground(bgColor);

        // 상단 제목 패널
        JPanel titlePanel = new JPanel(new BorderLayout());
        StockConfig.panelStyle(titlePanel);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JLabel titleLabel = new JLabel("상품 리스트 관리");
        LabelUtil.applyTitleStyle(titleLabel);
        titleLabel.setForeground(new Color(40, 40, 40));
        titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
        titlePanel.add(titleLabel, BorderLayout.WEST);

        // 버튼 패널 (세로 정렬)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 20));

        Font buttonFont = new Font("SansSerif", Font.PLAIN, 13);
        Dimension buttonSize = new Dimension(130, 30);
        Color buttonGreen = new Color(170, 225, 130);
        Color buttonText = new Color(40, 40, 40);

        JButton btnAdd = new JButton("상품 등록");
        JButton btnEdit = new JButton("상품 수정");
        JButton btnDelete = new JButton("상품 삭제");

        JButton[] buttons = {btnAdd, btnEdit, btnDelete};
        for (JButton btn : buttons) {
            btn.setPreferredSize(buttonSize);
            btn.setAlignmentX(JButton.CENTER_ALIGNMENT);
            btn.setFont(buttonFont);
            btn.setBackground(buttonGreen);
            btn.setForeground(buttonText);
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createLineBorder(new Color(150, 200, 120)));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            buttonPanel.add(btn);
            buttonPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        }

        // 테이블 생성
        model = new ProductModel(mainLayout.user);
        table = new JTable(model);

        TableUtil.applyStyle(table);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        int[] columnWidths = {130, 150, 200, 80, 80, 70};

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(Color.WHITE);

        add(titlePanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.EAST);
        add(scroll, BorderLayout.CENTER);

        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog dialog = new JDialog();
                dialog.setTitle("상품 등록");
                dialog.setSize(600, 500);
                dialog.setLocationRelativeTo(null);
                dialog.setModal(true);

                JPanel contentPanel = new JPanel(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(10, 10, 10, 10);
                gbc.fill = GridBagConstraints.HORIZONTAL;

                JLabel lblBrand = new JLabel("브랜드명:");
                cbBrand = new JComboBox<>();
                cbBrand.setPreferredSize(new Dimension(200, 30));
                for (Brand b : new BrandDAO().selectAll()) cbBrand.addItem(b);

                JLabel lblName = new JLabel("상품명:");
                tfName = new JTextField();
                tfName.setPreferredSize(new Dimension(200, 30));

                JLabel lblCategory = new JLabel("카테고리:");
                cbCategory = new JComboBox<>();
                cbCategory.setPreferredSize(new Dimension(200, 30));
                
                Category dummy = new Category();
        		dummy.setCt_name("카테고리를 선택하세요");
        		dummy.setCt_id(0);
        		cbCategory.addItem(dummy);
                
                List<Category> categories = new CategoryDAO().selectAll();
                for (Category c : categories) cbCategory.addItem(c);
                
            	//최상위 카테고리에 이벤트 연결 
        		cbCategory.addItemListener(new ItemListener() {
        			public void itemStateChanged(ItemEvent e) {
        				if(e.getStateChange() == ItemEvent.SELECTED) {
        					Category category = (Category)cbCategory.getSelectedItem();
        					
        					getCategoryDetail(category);
        				}
        			}
        		});
        		

                JLabel lblCategoryDetail = new JLabel("상세 카테고리:");
                cbCategoryDetail = new JComboBox<>();
                cbCategoryDetail.setPreferredSize(new Dimension(200, 30));
                
                JLabel lblOptionName = new JLabel("상품 옵션명:");
                tfOptionName = new JTextField();
                tfOptionName.setPreferredSize(new Dimension(200, 30));

                JLabel lblPrice = new JLabel("가격:");
                tfPrice = new JTextField();
                tfPrice.setPreferredSize(new Dimension(200, 30));

                JLabel lblActive = new JLabel("활성화:");
                cbActive = new JComboBox<>(new String[]{"y", "n"});
                cbActive.setPreferredSize(new Dimension(200, 30));
                
                // 콤보박스 가운데 정렬 렌더러
                DefaultListCellRenderer centerRenderer = new DefaultListCellRenderer();
                centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

                cbBrand.setRenderer(centerRenderer);
                cbCategory.setRenderer(centerRenderer);
                cbCategoryDetail.setRenderer(centerRenderer);
                cbActive.setRenderer(centerRenderer);
                
                // 스타일
                Color dialogBgColor = new Color(250, 252, 255);
                Color labelColor = new Color(60, 60, 60);

                contentPanel.setBackground(dialogBgColor);
                lblBrand.setForeground(labelColor);
                lblName.setForeground(labelColor);
                lblCategory.setForeground(labelColor);
                lblCategoryDetail.setForeground(labelColor);
                lblOptionName.setForeground(labelColor);
                lblPrice.setForeground(labelColor);
                lblActive.setForeground(labelColor);

                // 텍스트 필드 가운데 정렬
                tfName.setHorizontalAlignment(SwingConstants.CENTER);
                tfPrice.setHorizontalAlignment(SwingConstants.CENTER);
                tfOptionName.setHorizontalAlignment(SwingConstants.CENTER);


                gbc.gridx = 0; gbc.gridy = 0; contentPanel.add(lblBrand, gbc);
                gbc.gridx = 1; contentPanel.add(cbBrand, gbc);

                gbc.gridx = 0; gbc.gridy++;
                contentPanel.add(lblName, gbc);
                gbc.gridx = 1; contentPanel.add(tfName, gbc);

                gbc.gridx = 0; gbc.gridy++;
                contentPanel.add(lblCategory, gbc);
                gbc.gridx = 1; contentPanel.add(cbCategory, gbc);

                gbc.gridx = 0; gbc.gridy++;
                contentPanel.add(lblCategoryDetail, gbc);
                gbc.gridx = 1; contentPanel.add(cbCategoryDetail, gbc);
                
                gbc.gridx = 0; gbc.gridy++;
                contentPanel.add(lblOptionName, gbc);
                gbc.gridx = 1; contentPanel.add(tfOptionName, gbc);

                gbc.gridx = 0; gbc.gridy++;
                contentPanel.add(lblPrice, gbc);
                gbc.gridx = 1; contentPanel.add(tfPrice, gbc);

                gbc.gridx = 0; gbc.gridy++;
                contentPanel.add(lblActive, gbc);
                gbc.gridx = 1; contentPanel.add(cbActive, gbc);

                JPanel btnPanel = new JPanel();
                
                JButton btnSave = new JButton("저장");
                gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
                
                // 저장 버튼 스타일
                btnSave.setPreferredSize(new Dimension(100, 35));
                btnSave.setFont(new Font("SansSerif", Font.BOLD, 13));
                btnSave.setBackground(new Color(130, 180, 250));
                btnSave.setForeground(Color.WHITE);
                btnSave.setFocusPainted(false);
                btnSave.setBorder(BorderFactory.createLineBorder(new Color(100, 150, 220)));
                btnSave.setCursor(new Cursor(Cursor.HAND_CURSOR));
                
                btnSave.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                    	insert();
                    	
                    	mainLayout.setDataDirty(true); 
                        mainLayout.refreshIfDirty();
                    	
                    	JOptionPane.showMessageDialog(dialog, "상품이 등록되었습니다.");
                    	dialog.dispose();    
                    }
                });

                // 저장 버튼 위치 설정 (기존 설정 변경)
                gbc.anchor = GridBagConstraints.WEST;
                gbc.fill = GridBagConstraints.NONE; // 이걸로 크기 강제
                gbc.weightx = 0;                    // 공간 분배 없음
                btnPanel.add(btnSave, gbc);

                JButton btnCancel = new JButton("취소");
                gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
                
                // 취소 버튼 스타일
                btnCancel.setPreferredSize(new Dimension(100, 35));
                btnCancel.setFont(new Font("SansSerif", Font.BOLD, 13));
                btnCancel.setBackground(Color.gray);
                btnCancel.setForeground(Color.WHITE);
                btnCancel.setFocusPainted(false);
                btnCancel.setBorder(BorderFactory.createLineBorder(new Color(100, 150, 220)));
                btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
                
                btnCancel.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                    	dialog.dispose();
                    }
                });

                // 취소 버튼 위치 설정 (기존 설정 변경)
                gbc.anchor = GridBagConstraints.EAST;
                gbc.fill = GridBagConstraints.NONE; // 이걸로 크기 강제
                gbc.weightx = 0;                    // 공간 분배 없음
                btnPanel.add(btnCancel, gbc);

                gbc.anchor = GridBagConstraints.SOUTH;
                contentPanel.add(btnPanel, gbc);
                dialog.add(contentPanel);
                dialog.setVisible(true);
            }
        });
        
     // 1. 정렬 기능 설정
        TableRowSorter<TableModel> sorter_list = new TableRowSorter<>(table.getModel());
        table.setRowSorter(sorter_list);

        // 2. 헤더 클릭 이벤트로 정렬 상태 출력
        JTableHeader header_list = table.getTableHeader();
        header_list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int columnIndex = header_list.columnAtPoint(e.getPoint());
                String columnName = table.getColumnName(columnIndex);
                SortOrder order = getSortOrder(sorter_list, columnIndex);

//                System.out.println("헤더 클릭됨: " + columnName + " (인덱스: " + columnIndex + ")");
//                if (order == SortOrder.ASCENDING) {
//                    System.out.println("정렬 방향: 오름차순");
//                } else if (order == SortOrder.DESCENDING) {
//                    System.out.println("정렬 방향: 내림차순");
//                } else {
//                    System.out.println("정렬 방향 없음");
//                }
            }

            private SortOrder getSortOrder(TableRowSorter<?> sorter, int columnIndex) {
                for (RowSorter.SortKey key : sorter.getSortKeys()) {
                    if (key.getColumn() == columnIndex) {
                        return key.getSortOrder();
                    }
                }
                return SortOrder.UNSORTED;
            }
        });
    }

	public void getCategoryDetail(Category category) {
		//하위 카테고리 목록 가져오기
		List<CategoryDetail> detail = categoryDetailDAO.selectByCategoryId(category.getCt_id());
		
		//모든 하위 카테고리 콤보아이템 지우기 
		cbCategoryDetail.removeAllItems();
		
		CategoryDetail dummy = new CategoryDetail();
		dummy.setCt_dt_name("상세카테고리를 선택하세요");
		dummy.setCt_dt_id(0);
		cbCategoryDetail.addItem(dummy);
		
		//서브 카테고리 수만큼 반복하면서, 두번째 콤포박스에 SubCategory 모델을 채워넣기 
		for(int i=0;i<detail.size();i++) {
			CategoryDetail categoryDetail=detail.get(i);//i번째 요소 꺼내기
			cbCategoryDetail.addItem(categoryDetail);
		}
	};
	
	public void insert() {
		// mysql에서 트랜잭션이 적용되려면, 4개의 DAO 모두 같은 Connection이어야 한다
		Connection con = dbManager.getConnection();
		
		try { 
			con.setAutoCommit(false);
			
			Category category = (Category)cbCategory.getSelectedItem();
			CategoryDetail categoryDetail = (CategoryDetail)cbCategoryDetail.getSelectedItem();
			Brand brand = (Brand) cbBrand.getSelectedItem();
			String active = (String)cbActive.getSelectedItem();
			int optionNum = 0;
			
			Product product = new Product();
			
			product.setCategory(category);
			product.setProduct_name(tfName.getText());
			product.setCategory_detail(categoryDetail);
			product.setBrand(brand);
			
			productDAO.insert(product);
			
			int product_id=productDAO.selectRecentPk();
			product.setProduct_id(product_id);//구해온 최신 pk를 Product에 반영 
			
			ProductOption productOption = new ProductOption();
			
			productOption.setOption_active(active);
			productOption.setOption_name(tfOptionName.getText());
			productOption.setPrice(Integer.parseInt(tfPrice.getText()));
			productOption.setProduct(product);
		
			int productOption_id = productOptionDAO.selectRecentPk();
			productOption.setOption_id(productOption_id);
			
			StringBuffer codeMaker = new StringBuffer();
			codeMaker.append(category.getCt_id());
			codeMaker.append("-");
			codeMaker.append(categoryDetail.getCt_dt_id());
			codeMaker.append("-");
			codeMaker.append(brand.getBd_id());
			codeMaker.append("-");
			codeMaker.append(product_id + "00");
			codeMaker.append(productOption_id);
			
			productOption.setOption_code(codeMaker.toString());
			System.out.println("codeMaker : " + codeMaker.toString());
			
			if(active.equals("y")) {
				int maxOptionNo = productOptionDAO.selectMaxOptionNo(product_id);
			    optionNum = maxOptionNo + 1;
			} else if(active.equals("n")){
				optionNum = 99;
			}
			productOption.setOption_no(optionNum);
			
			productOptionDAO.insert(productOption);
			
			con.commit();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ProductException e){ 
			e.printStackTrace();
		} finally {
			try {
				con.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}













