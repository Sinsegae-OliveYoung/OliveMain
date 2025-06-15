package com.olive.bound.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;

import com.olive.bound.BoundPage;
import com.olive.common.config.Config;
import com.olive.common.model.Inbound;
import com.olive.common.repository.InboundDAO;
import com.olive.common.view.Panel;

public class InboundShowPanel extends Panel{

	JTable table;
	Inbound inbound;
	InboundListModel model;
    InboundDAO inboundDAO;

    public InboundShowPanel(BoundPage boundPage) {
        super(boundPage);
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
        JLabel titleLabel = new JLabel("입고요청서 List");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        titleLabel.setForeground(new Color(40, 40, 40));
        titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
        topPanel.add(titleLabel, BorderLayout.WEST);


        // 테이블 생성
//        model = new InboundListModel("now");
        inbound = new Inbound();
        model = new InboundListModel(inbound);
        table = new JTable(model);

        // 테이블 헤더 스타일
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