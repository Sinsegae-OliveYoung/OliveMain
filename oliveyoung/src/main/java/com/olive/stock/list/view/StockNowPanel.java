package com.olive.stock.list.view;

import com.olive.common.view.Panel;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import com.olive.common.config.Config;
import com.olive.common.model.Branch;
import com.olive.common.repository.BranchDAO;
import com.olive.common.util.TableUtil;
import com.olive.common.util.style.LabelUtil;
import com.olive.mainlayout.MainLayout;
import com.olive.stock.StockConfig;
import com.olive.stock.model.ListModel;

public class StockNowPanel extends Panel {

    JTable table;
    ListModel model;
    MainLayout mainLayout;

    @Override
    public void refresh() {
        model.reload();     // ListModel에서 최신 데이터 로드
        table.updateUI();   // 테이블 UI 갱신
    }

    public StockNowPanel(MainLayout mainLayout) {
        super(mainLayout);
        this.mainLayout = mainLayout;
        setLayout(new BorderLayout());
        setBackground(Config.WHITE);

        // 제목 패널
        JPanel topPanel = new JPanel(new BorderLayout());
        StockConfig.panelStyle(topPanel);
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JLabel titleLabel = new JLabel("현재 수량 확인");
        LabelUtil.applyTitleStyle(titleLabel);
        topPanel.add(titleLabel, BorderLayout.WEST);

        // 버튼 패널
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));

        Font buttonFont = new Font("SansSerif", Font.PLAIN, 13);
        Dimension buttonSize = new Dimension(130, 30);
        Color buttonGreen = new Color(170, 225, 130);
        Color buttonText = new Color(40, 40, 40);

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
            btn.setBorder(BorderFactory.createLineBorder(new Color(150, 200, 120)));
            buttonPanel.add(btn);
        }

        // 테이블 생성
        model = new ListModel("now", mainLayout.user);
        table = new JTable(model);

        // 테이블 스타일 적용
        TableUtil.applyStyle(table);

        // 셀 정렬
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        int[] columnWidths = {120, 100, 120, 210, 90, 80, 70, 110};

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(new Color(245, 248, 250));
        

        // 전체 조립
        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.setOpaque(false);
        topContainer.add(topPanel, BorderLayout.NORTH);
        topContainer.add(buttonPanel, BorderLayout.CENTER);

        add(topContainer, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        // 정렬 기능 연결
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
