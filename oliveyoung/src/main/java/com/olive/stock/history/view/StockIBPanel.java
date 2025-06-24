package com.olive.stock.history.view;

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
import javax.swing.table.JTableHeader;

import com.olive.common.config.Config;
import com.olive.common.util.TableUtil;
import com.olive.common.util.style.ButtonUtil;
import com.olive.common.util.style.LabelUtil;
import com.olive.mainlayout.MainLayout;
import com.olive.stock.StockConfig;
import com.olive.stock.StockPage;
import com.olive.stock.StockPanel;
import com.olive.stock.model.StockModel;
import com.olive.stock.model.ListModel;
import com.olive.store.StorePage;

public class StockIBPanel extends Panel{
	
	JTable table;
    StockModel model;
    MainLayout mainLayout;

    public StockIBPanel(MainLayout mainLayout) {
	   super(mainLayout);
       this.mainLayout = mainLayout;
       setLayout(new BorderLayout(0, 10));
       setBackground(Config.WHITE);

        // 상단 패널
        JPanel topPanel = new JPanel(new BorderLayout());
        StockConfig.panelStyle(topPanel);
        topPanel.setBorder(BorderFactory.createEmptyBorder(30, 27, 10, 20));

        JLabel titleLabel = new JLabel("재고 입고 기록");
        LabelUtil.applyTitleStyle(titleLabel);
        titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.setBackground(Config.WHITE); 

        // 버튼 패널
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));
        
        Font buttonFont = new Font("SansSerif", Font.PLAIN, 13);
        Dimension buttonSize = new Dimension(130, 30);

        Color buttonGreen = new Color(170, 225, 130); // 조화로운 색상
        Color buttonText = new Color(40, 40, 40); // 어두운 회색

        JButton btnDateAsc = ButtonUtil.greenButtonUtil("입고일 ↑");
        JButton btnDateDesc = ButtonUtil.greenButtonUtil("입고일 ↓");

        JButton[] buttons = {btnDateAsc, btnDateDesc};
        for (JButton btn : buttons) {
            buttonPanel.add(btn);
        }

        // 테이블 생성
        model = new StockModel("in");
        table = new JTable(model);

        // 테이블 스타일 적용
        TableUtil.applyStyle(table);
        
        // 테이블 셀 가운데 정렬
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
       int[] columnWidths = { 110, 90, 100, 210, 90, 70, 60, 100, 60, 100};
        
        for (int i = 0; i < table.getColumnCount(); i++) {
        	table.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(Color.WHITE);
        TableUtil.tableStyleUtil(table, scroll, 500, false); // 스타일 유틸 적용
        
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
        scrollWrapper.setBorder(BorderFactory.createEmptyBorder(50, 25, 10, 25));

        scrollWrapper.add(scroll, BorderLayout.CENTER);
        
        // 전체 레이아웃 조립
        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.setOpaque(false);
        topContainer.add(topPanel, BorderLayout.NORTH);
        topContainer.add(buttonPanel, BorderLayout.CENTER);

        // 레이아웃은 BorderLayout 유지
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(Config.CONTENT_W, Config.CONTENT_H));

        add(topContainer, BorderLayout.NORTH);
        add(scrollWrapper, BorderLayout.CENTER);
        //버튼 기능 구현
        btnDateAsc.addActionListener(e -> {
            model.sortByDateAsc();
            table.updateUI();
        });

        btnDateDesc.addActionListener(e -> {
            model.sortByDateDesc();
            table.updateUI();
        });
    }
}
