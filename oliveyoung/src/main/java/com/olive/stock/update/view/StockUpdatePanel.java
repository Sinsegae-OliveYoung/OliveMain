package com.olive.stock.update.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
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
import com.olive.stock.model.ListModel;
import com.olive.store.StorePage;

public class StockUpdatePanel extends StockPanel{
	
	   JTable table;
	    ListModel model;

	    public StockUpdatePanel(StockPage stockPage) {
	        super(stockPage);
	        setLayout(new BorderLayout());

	        // 상단 패널
	        JPanel topPanel = new JPanel(new BorderLayout());
	        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

	        JLabel titleLabel = new JLabel("수동 수량 재고 조정");
	        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
	        titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
	        topPanel.add(titleLabel, BorderLayout.WEST);
	        topPanel.setBackground(StockConfig.bgColor);

	        // 테이블 생성
	        model = new ListModel("now");
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
	    }
}
