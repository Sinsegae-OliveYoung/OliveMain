package com.olive.stock.alert.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
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
import com.olive.mainlayout.MainLayout;
import com.olive.stock.StockConfig;
import com.olive.stock.StockPage;
import com.olive.stock.StockPanel;
import com.olive.stock.model.ListModel;
import com.olive.store.StorePage;

public class CountAlertPanel extends StockPanel{
	

    JTable table;
    ListModel model;
    
    @Override
    public void refresh() {
        model.reload();     // ListModel에서 최신 데이터 로드
        table.updateUI();   // 테이블 UI 갱신
    }

    public CountAlertPanel(MainLayout mainLayout) {
        super(mainLayout);
        setLayout(new BorderLayout());

        // 상단 패널
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel titleLabel = new JLabel("재고 수량 부족 ");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.setBackground(StockConfig.bgColor);

        // 버튼 패널
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        Font buttonFont = new Font("SansSerif", Font.PLAIN, 13);
        Dimension buttonSize = new Dimension(130, 30);

        Color buttonGreen = new Color(170, 225, 130); // 조화로운 색상
        Color buttonText = new Color(40, 40, 40); // 어두운 회색

        JButton btnDateAsc = new JButton("입고일 ↑");
        JButton btnDateDesc = new JButton("입고일 ↓");
        JButton btnQtyDesc = new JButton("재고수량 ↓");
        JButton btnNameAsc = new JButton("상품명 ↑");
        JButton btnNameDesc = new JButton("상품명 ↓");

        JButton[] buttons = {btnDateAsc, btnDateDesc, btnQtyDesc, btnNameAsc, btnNameDesc};
        for (JButton btn : buttons) {
            btn.setPreferredSize(buttonSize);   
            btn.setFont(buttonFont);
            btn.setBackground(buttonGreen);
            btn.setForeground(buttonText);
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createLineBorder(new Color(150, 200, 120))); // 테두리도 조화롭게
            buttonPanel.add(btn);
        }

        topPanel.add(buttonPanel, BorderLayout.EAST);

        // 테이블 생성
        model = new ListModel("countAlert");
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
        // 수량 컬럼 전체를 빨간색으로 렌더링하는 렌더러
        DefaultTableCellRenderer redTextRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table,
                                                           Object value,
                                                           boolean isSelected,
                                                           boolean hasFocus,
                                                           int row,
                                                           int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setForeground(Color.RED);
                setHorizontalAlignment(SwingConstants.CENTER);
                return c;
            }
        };

        // 수량 컬럼 인덱스
        int quantityColumnIndex = model.findColumn("재고수량");

        // 컬럼별 렌더러 적용
        for (int i = 0; i < table.getColumnCount(); i++) {
            if (i == quantityColumnIndex) {
                table.getColumnModel().getColumn(i).setCellRenderer(redTextRenderer);
            } else {
                table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        }

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(Color.WHITE);
        
        // 전체 레이아웃 구성
        add(topPanel, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        
        // 정렬 기능 구현
        btnDateAsc.addActionListener(e -> {
            model.sortByDateAsc();
            table.updateUI();
        });

        btnDateDesc.addActionListener(e -> {
            model.sortByDateDesc();
            table.updateUI();
        });

        btnQtyDesc.addActionListener(e -> {
            model.sortByQuantityDesc();
            table.updateUI();
        });

        btnNameAsc.addActionListener(e -> {
            model.sortByNameAsc();
            table.updateUI();
        });

        btnNameDesc.addActionListener(e -> {
            model.sortByNameDesc();
            table.updateUI();
        });
    }
}
