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
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

import com.olive.common.config.Config;
import com.olive.common.model.Brand;
import com.olive.common.model.Category;
import com.olive.common.model.CategoryDetail;
import com.olive.common.repository.BrandDAO;
import com.olive.common.repository.CategoryDAO;
import com.olive.common.repository.CategoryDetailDAO;
import com.olive.common.util.TableUtil;
import com.olive.common.util.style.LabelUtil;
import com.olive.common.view.Panel;
import com.olive.mainlayout.MainLayout;
import com.olive.product.model.ProductModel;
import com.olive.stock.StockConfig;

public class ProductListPanel extends Panel {

    JTable table;
    JComboBox<CategoryDetail> cbCategoryDetail;
    ProductModel model;
    CategoryDetailDAO categoryDetailDAO;

    public ProductListPanel(MainLayout mainLayout) {
        super(mainLayout);
        setLayout(new BorderLayout());
        
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
                dialog.setSize(500, 400);
                dialog.setLocationRelativeTo(null);
                dialog.setModal(true);

                JPanel contentPanel = new JPanel(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(10, 10, 10, 10);
                gbc.fill = GridBagConstraints.HORIZONTAL;

                JLabel lblBrand = new JLabel("브랜드명:");
                JComboBox<String> cbBrand = new JComboBox<>();
                for (Brand b : new BrandDAO().selectAll()) cbBrand.addItem(b.getBd_name());

                JLabel lblName = new JLabel("상품명:");
                JTextField tfName = new JTextField();

                JLabel lblCategory = new JLabel("카테고리:");
                JComboBox<Category> cbCategory = new JComboBox<>();
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

                JLabel lblPrice = new JLabel("가격:");
                JTextField tfPrice = new JTextField();

                JLabel lblActive = new JLabel("활성화:");
                JComboBox<String> cbActive = new JComboBox<>(new String[]{"y", "n"});

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
                contentPanel.add(lblPrice, gbc);
                gbc.gridx = 1; contentPanel.add(tfPrice, gbc);

                gbc.gridx = 0; gbc.gridy++;
                contentPanel.add(lblActive, gbc);
                gbc.gridx = 1; contentPanel.add(cbActive, gbc);

                JButton btnSave = new JButton("저장");
                gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
                contentPanel.add(btnSave, gbc);

                dialog.add(contentPanel);
                dialog.setVisible(true);
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
}
