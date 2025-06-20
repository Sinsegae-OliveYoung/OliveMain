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
import com.olive.mainlayout.MainLayout;
import com.olive.stock.StockConfig;
import com.olive.stock.StockPage;
import com.olive.stock.StockPanel;
import com.olive.stock.model.StockModel;
import com.olive.stock.model.ListModel;
import com.olive.store.StorePage;
import com.toedter.calendar.JDateChooser;

public class StockFiltPanel extends StockPanel{
	
	JTable table;
    StockModel model;
    JDateChooser dt_start;
    JDateChooser dt_last;
    
        public StockFiltPanel(MainLayout mainLayout) {
            super(mainLayout);
            setLayout(new BorderLayout(0, 10)); // 아래 여백 추가

            //  상단 패널
            JPanel topPanel = new JPanel(new BorderLayout());
            topPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));
            topPanel.setBackground(StockConfig.bgColor);

            JLabel titleLabel = new JLabel("시간대 별 기록");
            titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
            topPanel.add(titleLabel, BorderLayout.WEST);

            // 날짜 선택 패널
            JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
            datePanel.setOpaque(false);

            dt_start = new JDateChooser();
            dt_start.setPreferredSize(new Dimension(200, 30));
            dt_last = new JDateChooser();
            dt_last.setPreferredSize(new Dimension(200, 30));

            datePanel.add(dt_start);
            datePanel.add(dt_last);
            topPanel.add(datePanel, BorderLayout.EAST);

            //  테이블 설정
            model = new StockModel("in");
            table = new JTable(model);
            table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // 열 너비 고정

            table.setRowHeight(25);
            table.setFont(new Font("SansSerif", Font.PLAIN, 13));
            table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
            table.getTableHeader().setBackground(Config.LIGHT_GREEN);
            table.getTableHeader().setForeground(Color.DARK_GRAY);

            // 컬럼 정렬 + 너비 조정
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);


            int[] columnWidths = {120, 100, 120, 210, 90, 80, 70, 110, 70, 110};

            for (int i = 0; i < columnWidths.length; i++) {
                table.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
                table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }

            // 스크롤 영역 조정
            JScrollPane scroll = new JScrollPane(table);
            scroll.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
            scroll.getViewport().setBackground(Color.WHITE);

            // 최종 조립
            add(topPanel, BorderLayout.NORTH);
            add(scroll, BorderLayout.CENTER);
        }
    }

