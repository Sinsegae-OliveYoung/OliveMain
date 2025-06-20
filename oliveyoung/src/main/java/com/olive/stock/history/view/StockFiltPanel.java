package com.olive.stock.history.view;
import com.olive.common.view.Panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.time.LocalDate;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
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
import com.olive.manage.DatePicker;
import com.olive.stock.StockConfig;
import com.olive.stock.StockPage;
import com.olive.stock.StockPanel;
import com.olive.stock.model.StockModel;
import com.olive.stock.model.ListModel;
import com.olive.store.StorePage;
import com.toedter.calendar.JDateChooser;
import com.olive.common.util.ImageUtil;
import com.olive.common.util.TableUtil;

public class StockFiltPanel extends Panel {

    JTable table;
    JPanel p_north;
    JPanel p_dateArea;
    JLabel lb_start, lb_end;
    JButton bt_start, bt_end;
    StockModel model;
    
    JComboBox<String> categoryBox;

    ImageUtil imgUtil = new ImageUtil();

    public StockFiltPanel(MainLayout mainLayout) {
        super(mainLayout);
        setLayout(new BorderLayout(0, 10)); // 테이블 아래 간격

        // 제목 영역
        p_north = new JPanel(new BorderLayout());
        p_north.setBackground(StockConfig.bgColor);
        p_north.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));

        JLabel titleLabel = new JLabel("시간대 별 기록");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        p_north.add(titleLabel, BorderLayout.WEST);

        // 날짜 + 버튼 영역
        p_dateArea = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        p_dateArea.setOpaque(false);
        p_dateArea.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        lb_start = new JLabel("yyyy.mm.dd");
        lb_end = new JLabel(LocalDate.now().toString().replace("-", "."));

        bt_start = createDateButton("images/calendar_icon.png");
        bt_end = createDateButton("images/calendar_icon.png");

        JPanel pStart = wrapDateField(lb_start, bt_start);
        JPanel pEnd = wrapDateField(lb_end, bt_end);
        
        // 카테고리 콤보박스 추가
        categoryBox = new JComboBox<>(new String[]{"in", "out"});
        categoryBox.setPreferredSize(new Dimension(80, 30));
        categoryBox.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JButton bt_search = new JButton("검색");
        bt_search.setFont(new Font("SansSerif", Font.BOLD, 13));
        bt_search.setBackground(new Color(92, 158, 115));
        bt_search.setForeground(Color.WHITE);
        bt_search.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bt_search.setFocusPainted(false);
        bt_search.setPreferredSize(new Dimension(80, 30));

        p_dateArea.add(pStart);
        p_dateArea.add(pEnd);
        p_dateArea.add(categoryBox);
        p_dateArea.add(bt_search);

        p_north.add(p_dateArea, BorderLayout.EAST);

        // 테이블 생성 및 스타일
        model = new StockModel("in");
        table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        // 테이블 스타일 적용
        TableUtil.applyStyle(table);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        int[] columnWidths = {120, 100, 120, 220, 90, 80, 70, 110, 70, 110};

        for (int i = 0; i < columnWidths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        scroll.getViewport().setBackground(Color.WHITE);

        // 이벤트 연결
        bt_start.addActionListener(e -> new DatePicker(lb_start));
        bt_end.addActionListener(e -> new DatePicker(lb_end));
        bt_search.addActionListener(e -> {
        	String state = categoryBox.getSelectedItem().toString();
            String start = lb_start.getText();
            String end = lb_end.getText();
            // 필터링 로직 추가 가능
            model = new StockModel(state ,start, end);
            System.out.println("검색 후 상태: " + state + "검색: " + start + " ~ " + end);
            table.setModel(model);
            
            // 선택 변경 후 렌더러 다시 설정
            for (int i = 0; i < table.getColumnCount(); i++) {
            	table.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
                table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
            table.updateUI();
        });

        add(p_north, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
    }

    private JButton createDateButton(String path) {
        Image img = imgUtil.getImage(path, 20, 20);
        JButton btn = new JButton(new ImageIcon(img));
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JPanel wrapDateField(JLabel label, JButton button) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        panel.setPreferredSize(new Dimension(150, 30));
        panel.setBackground(Color.WHITE);
        label.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        panel.add(label, BorderLayout.WEST);
        panel.add(button, BorderLayout.EAST);
        return panel;
    }
}

