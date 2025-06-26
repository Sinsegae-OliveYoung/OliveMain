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
import com.olive.common.model.User;
import com.olive.common.repository.CategoryDAO;
import com.olive.common.util.TableUtil;
import com.olive.common.util.style.ComboBoxUtil;
import com.olive.common.util.style.LabelUtil;
import com.olive.mainlayout.MainLayout;
import com.olive.stock.StockConfig;
import com.olive.stock.StockPage;
import com.olive.stock.StockPanel;
import com.olive.stock.model.ListModel;

public class StockCatPanel extends Panel {

    JTable table;
    ListModel model;
    JComboBox<Category> cb_category;
    CategoryDAO categoryDAO;
    
    User user;
    
    @Override
    public void refresh() {
        Category selected = (Category) cb_category.getSelectedItem();
        ListModel newModel;

        if (selected != null && selected.getCt_id() != 0) {
            newModel = new ListModel(selected, user);
        } else {
            newModel = new ListModel("now", user);
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
        user = mainLayout.user;
        setLayout(new BorderLayout());

        // 공통 색상 및 폰트
        Color bgColor = Config.WHITE;
        Color comboColor = new Color(100, 149, 237); // Cornflower Blue
        Font defaultFont = new Font("SansSerif", Font.PLAIN, 13);

        setBackground(bgColor);

        // 상단 패널
        JPanel topPanel = new JPanel(new BorderLayout());
        StockConfig.panelStyle(topPanel);
        topPanel.setBorder(BorderFactory.createEmptyBorder(30, 27, 10, 20));

        // 제목 라벨
        JLabel titleLabel = new JLabel("카테고리별 재고 확인");
        LabelUtil.applyTitleStyle(titleLabel);
        titleLabel.setForeground(new Color(40, 40, 40));
        titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
        topPanel.add(titleLabel, BorderLayout.WEST);

        // 콤보박스 패널
        JPanel comboPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        comboPanel.setOpaque(false);

        cb_category = new JComboBox<>();
        cb_category.setUI(new ComboBoxUtil());
        cb_category.setPreferredSize(new Dimension(190, 30));
        ComboBoxUtil.applyDefaultStyle(cb_category);
        cb_category.setBackground(Config.LIGHT_GRAY); // 이건 applyDefaultStyle에 없으니 유지
        comboPanel.add(cb_category);


        // 테이블 생성
        model = new ListModel("now", user);
        table = new JTable(model);

        // JScrollPane 생성
        JScrollPane scroll = new JScrollPane(table);
        TableUtil.tableStyleUtil(table, scroll, 500, false); // 스타일 유틸 적용
        int[] columnWidths = {125, 100, 130, 240, 75, 70, 70, 70, 70};
        
        for (int i = 0; i < table.getColumnCount(); i++) {
        	table.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
        }
       
        // 테이블 header 스타일 추가적으로 적용 가능
        JTableHeader header = table.getTableHeader();
        header.setBackground(Config.LIGHT_GREEN);
        header.setFont(new Font("Noto Sans KR", Font.BOLD, 15));
        header.setPreferredSize(new Dimension(Integer.MIN_VALUE, 33));
        header.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
  		for (int i = 0; i < table.getColumnCount(); i++) {
  			table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
     		}
        
        // scroll을 감싸는 패널 생성 (여백 + 테두리 적용)
        JPanel scrollWrapper = new JPanel(new BorderLayout());
        scrollWrapper.setBackground(Config.WHITE);

        // 얇은 테두리 + 내부 여백 적용 (순서 중요!)
        scrollWrapper.setBorder(BorderFactory.createEmptyBorder(35, 25, 10, 25));
        comboPanel.setBorder(BorderFactory.createEmptyBorder(35, 0, 0, 20));
        
        scrollWrapper.add(scroll, BorderLayout.CENTER);
        
        // 전체 레이아웃 구성
        add(topPanel, BorderLayout.NORTH);
        add(comboPanel, BorderLayout.EAST);
        add(scrollWrapper, BorderLayout.CENTER);

        // 콤보박스 이벤트 연결
        cb_category.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    Category category = (Category) cb_category.getSelectedItem();
                    if (category.getCt_id() != 0) {
                        ListModel newModel = new ListModel(category, user);
                        table.setModel(newModel);
                    } else {
                        table.setModel(new ListModel("now", user));
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
