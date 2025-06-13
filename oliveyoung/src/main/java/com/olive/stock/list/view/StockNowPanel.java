package com.olive.stock.list.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

import com.olive.stock.StockPage;
import com.olive.stock.StockPanel;
import com.olive.stock.model.ListNowModel;

public class StockNowPanel extends StockPanel {

    JTable table;
    ListNowModel model;

    public StockNowPanel(StockPage stockPage) {
        super(stockPage);
        setLayout(new BorderLayout());

        // ✅ 상단 패널
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // padding

        // 🔹 제목
        JLabel titleLabel = new JLabel("현재 수량 확인");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        titleLabel.setHorizontalAlignment(SwingConstants.LEFT); // 왼쪽 정렬
        topPanel.add(titleLabel, BorderLayout.WEST);

        // 🔹 버튼 패널
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0)); // 오른쪽 + 간격
        Font buttonFont = new Font("SansSerif", Font.PLAIN, 13);
        Dimension buttonSize = new Dimension(130, 30);

        JButton btnDateAsc = new JButton("입고일 ↑");
        JButton btnDateDesc = new JButton("입고일 ↓");
        JButton btnQtyDesc = new JButton("재고수량 ↓");
        JButton btnNameAsc = new JButton("상품명 ↑");
        JButton btnNameDesc = new JButton("상품명 ↓");

        // 공통 스타일 적용
        JButton[] buttons = {btnDateAsc, btnDateDesc, btnQtyDesc, btnNameAsc, btnNameDesc};
        for (JButton btn : buttons) {
            btn.setPreferredSize(buttonSize);
            btn.setFont(buttonFont);
            buttonPanel.add(btn);
        }

        topPanel.add(buttonPanel, BorderLayout.EAST);

        // ✅ 테이블 생성
        model = new ListNowModel();
        table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);

        // ✅ 패널 구성
        add(topPanel, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        // ✅ 정렬 기능 구현
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
