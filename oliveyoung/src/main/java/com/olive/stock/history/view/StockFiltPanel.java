package com.olive.stock.history.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

import com.olive.common.config.Config;
import com.olive.stock.StockConfig;
import com.olive.stock.StockPage;
import com.olive.stock.StockPanel;
import com.olive.stock.model.StockModel;
import com.olive.stock.model.ListModel;
import com.olive.store.StorePage;

public class StockFiltPanel extends StockPanel{
	
	JTable table;
    StockModel model;
    JComboBox cb_approver;

    public StockFiltPanel(StockPage stockPage) {
        super(stockPage);
        setLayout(new BorderLayout());

        // 상단 패널
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel titleLabel = new JLabel("시간대 별 기록");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.setBackground(StockConfig.bgColor);

        // 시간대 별 ComboBox 구현 
//        JPanel comboPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
//        comboPanel.setOpaque(false);
//
//        cb_approver = new JComboBox<>();
//        cb_approver.setPreferredSize(new Dimension(200, 30));
//        cb_approver.setFont(new Font("SansSerif", Font.PLAIN, 14));
//        cb_approver.setBackground(Config.LIGHT_GREEN);
//        cb_approver.setForeground(Color.DARK_GRAY);
//        cb_approver.setFocusable(false);
//        cb_approver.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//        comboPanel.add(cb_approver);
//
//        topPanel.add(comboPanel, BorderLayout.EAST);

        // 테이블 생성
        model = new StockModel("in");
        table = new JTable(model);

        // 💡 테이블 스타일 적용
        table.setRowHeight(25);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        table.getTableHeader().setBackground(Config.LIGHT_GREEN); // 테이블 헤더 배경색 설정
        table.getTableHeader().setForeground(Color.DARK_GRAY);
        
        // 테이블 셀 가운데 정렬
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(Color.WHITE);
        
        // 전체 레이아웃 구성
        add(topPanel, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        
        // 정렬 기능 구현
//        btnDateAsc.addActionListener(e -> {
//            model.sortByDateAsc();
//            table.updateUI();
//        });
//
//        btnDateDesc.addActionListener(e -> {
//            model.sortByDateDesc();
//            table.updateUI();
//        });
    }
}
