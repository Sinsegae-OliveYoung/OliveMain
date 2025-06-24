package com.olive.stock.update.view;
import com.olive.common.view.Panel;

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
import javax.swing.table.DefaultTableModel;

import com.olive.common.config.Config;
import com.olive.common.model.User;
import com.olive.common.util.style.LabelUtil;
import com.olive.mainlayout.MainLayout;
import com.olive.stock.StockConfig;
import com.olive.stock.StockPage;
import com.olive.stock.StockPanel;
import com.olive.stock.model.ListModel;
import com.olive.stock.model.UpdateModel;
import com.olive.store.StorePage;

public class StockUpdatePanel extends Panel{
	
	    JTable table;
	    UpdateModel model;
	    
	    @Override
	    public void refresh() {
	        model.reload();     // ListModel에서 최신 데이터 로드
	        table.updateUI();   // 테이블 UI 갱신
	    }

	    public StockUpdatePanel(MainLayout mainLayout, StockPage stockPage) {
	        super(mainLayout);
	        setLayout(new BorderLayout());

	        // 상단 패널
	        JPanel topPanel = new JPanel(new BorderLayout());
	        StockConfig.panelStyle(topPanel);
	        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

	        JLabel titleLabel = new JLabel("수동 수량 재고 조정");
	        LabelUtil.applyTitleStyle(titleLabel);
	        titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
	        topPanel.add(titleLabel, BorderLayout.WEST);
	        topPanel.setBackground(Config.WHITE); 

	        // 테이블 생성
	        model = new UpdateModel(stockPage, mainLayout, mainLayout.user);
	        table = new JTable(model);
	        

	        // 테이블 스타일 적용
	        table.setRowHeight(25);
	        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
	        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
	        table.getTableHeader().setBackground(Config.LIGHT_GREEN); // 테이블 헤더 배경색 설정
	        table.getTableHeader().setForeground(Color.DARK_GRAY);
	        
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
	    }
}
