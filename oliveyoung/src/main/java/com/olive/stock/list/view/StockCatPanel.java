package com.olive.stock.list.view;
import com.olive.common.view.Panel;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

import com.olive.common.config.Config;
import com.olive.common.model.Category;
import com.olive.common.repository.CategoryDAO;
import com.olive.common.util.TableUtil;
import com.olive.mainlayout.MainLayout;
import com.olive.stock.StockPage;
import com.olive.stock.StockPanel;
import com.olive.stock.model.ListModel;

public class StockCatPanel extends Panel {

    JTable table;
    ListModel model;
    JComboBox<Category> cb_category;
    CategoryDAO categoryDAO;
    
    @Override
    public void refresh() {
        Category selected = (Category) cb_category.getSelectedItem();
        ListModel newModel;

        if (selected != null && selected.getCt_id() != 0) {
            newModel = new ListModel(selected);
        } else {
            newModel = new ListModel("now");
        }

        table.setModel(newModel);

        // 렌더러 재적용
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        table.updateUI();
    }

    public StockCatPanel(MainLayout mainLayout) {
        super(mainLayout);
        setLayout(new BorderLayout());

        // 공통 색상 및 폰트
        Color bgColor = new Color(245, 248, 250);
        Color comboColor = new Color(100, 149, 237); // Cornflower Blue
        Font defaultFont = new Font("SansSerif", Font.PLAIN, 13);

        setBackground(bgColor);

        // 상단 패널
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(bgColor);
        topPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        // 제목 라벨
        JLabel titleLabel = new JLabel("카테고리별 재고 확인");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        titleLabel.setForeground(new Color(40, 40, 40));
        titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
        topPanel.add(titleLabel, BorderLayout.WEST);

        // 콤보박스 패널
        JPanel comboPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        comboPanel.setOpaque(false);

        cb_category = new JComboBox<>();
        cb_category.setPreferredSize(new Dimension(200, 30));
        cb_category.setFont(new Font("SansSerif", Font.PLAIN, 14));
        cb_category.setBackground(Config.LIGHT_GREEN);
        cb_category.setForeground(Color.DARK_GRAY);
        cb_category.setFocusable(false);
        cb_category.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        comboPanel.add(cb_category);

        topPanel.add(comboPanel, BorderLayout.EAST);

        // 테이블 생성
        model = new ListModel("now");
        table = new JTable(model);

        // 테이블 스타일 적용
        TableUtil.applyStyle(table);

        // 테이블 셀 가운데 정렬
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        
        int[] columnWidths = {120, 100, 120, 210, 90, 80, 70, 110};
        
        for (int i = 0; i < table.getColumnCount(); i++) {
        	table.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(Color.WHITE);

        // 전체 레이아웃 구성
        add(topPanel, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        // 콤보박스 이벤트 연결
        cb_category.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    Category category = (Category) cb_category.getSelectedItem();
                    if (category.getCt_id() != 0) {
                        ListModel newModel = new ListModel(category);
                        table.setModel(newModel);
                    } else {
                        table.setModel(new ListModel("now"));
                    }

                    // 선택 변경 후 렌더러 다시 설정
                    for (int i = 0; i < table.getColumnCount(); i++) {
                    	table.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
                        table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
                    }
                    table.updateUI();
                }
            }
        });

        loadCategories();
    }

    // 카테고리 목록 불러오기
    private void loadCategories() {
        categoryDAO = new CategoryDAO();
        List<Category> cateList = categoryDAO.selectAll();

        Category dummy = new Category();
        dummy.setCt_id(0);
        dummy.setCt_name("카테고리를 선택하세요");
        cb_category.addItem(dummy);
        
        for (Category category : cateList) {
            cb_category.addItem(category);
        }
    }
}
