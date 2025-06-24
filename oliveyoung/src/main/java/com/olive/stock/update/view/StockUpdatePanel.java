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
import javax.swing.table.JTableHeader;

import com.olive.common.config.Config;
import com.olive.common.model.User;
import com.olive.common.util.TableUtil;
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
	        topPanel.setBorder(BorderFactory.createEmptyBorder(30, 27, 10, 20));

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
	        TableUtil.tableStyleUtil(table, scroll, 500, true); // 스타일 유틸 적용
	        
	        // 테이블 header 스타일 추가적으로 적용 가능
	        JTableHeader header = table.getTableHeader();
	        header.setBackground(Config.LIGHT_GREEN);
	        header.setFont(new Font("Noto Sans KR", Font.BOLD, 15));
	        header.setPreferredSize(new Dimension(Integer.MIN_VALUE, 33));
	        header.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

	        // scroll을 감싸는 패널 생성 (여백 + 테두리 적용)
	        JPanel scrollWrapper = new JPanel(new BorderLayout());
	        scrollWrapper.setBackground(Config.WHITE);

	        // 얇은 테두리 + 내부 여백 적용 (순서 중요!)
	        scrollWrapper.setBorder(BorderFactory.createEmptyBorder(35, 25, 10, 25));

	        scrollWrapper.add(scroll, BorderLayout.CENTER);
	        
	        // 전체 레이아웃 구성
	        add(topPanel, BorderLayout.NORTH);
	        add(scrollWrapper, BorderLayout.CENTER);
	    }
}
