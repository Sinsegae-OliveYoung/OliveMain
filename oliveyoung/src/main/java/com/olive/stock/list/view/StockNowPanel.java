package com.olive.stock.list.view;

import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.table.JTableHeader;

import com.olive.common.config.Config;
import com.olive.common.model.Branch;
import com.olive.common.util.TableUtil;
import com.olive.common.util.style.ButtonUtil;
import com.olive.common.util.style.ComboBoxUtil;
import com.olive.common.util.style.LabelUtil;
import com.olive.common.view.Panel;
import com.olive.mainlayout.MainLayout;
import com.olive.stock.StockConfig;
import com.olive.stock.StockPage;
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
        topPanel.setBorder(BorderFactory.createEmptyBorder(30, 27, 10, 20));

        JLabel titleLabel = new JLabel("현재 수량 확인");
        LabelUtil.applyTitleStyle(titleLabel);
        topPanel.add(titleLabel, BorderLayout.WEST);

        // 버튼 패널
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));
        buttonPanel.setBackground(Config.WHITE); 
        
      
        

        Font buttonFont = new Font("SansSerif", Font.PLAIN, 13);
        Dimension buttonSize = new Dimension(130, 30);
        Color buttonText = new Color(40, 40, 40);

        JButton btnDateAsc = ButtonUtil.greenButtonUtil("입고일 ↑");
        JButton btnDateDesc = ButtonUtil.greenButtonUtil("입고일 ↓");
        JButton btnQtyDesc = ButtonUtil.greenButtonUtil("재고수량 ↓");
        JButton btnNameAsc = ButtonUtil.greenButtonUtil("상품명 ↑");
        JButton btnNameDesc = ButtonUtil.greenButtonUtil("상품명 ↓");

        JButton[] buttons = {btnDateAsc, btnDateDesc, btnQtyDesc, btnNameAsc, btnNameDesc};
        for (JButton btn : buttons) {
            buttonPanel.add(btn);
        }

        // 테이블 , 테이블 패널 생성
        JPanel tablePanel = new JPanel();
        // 테이블 생성 및 스타일 적용
        model = new ListModel("now", mainLayout.user);
        table = new JTable(model);

        // JScrollPane 생성
        JScrollPane scroll = new JScrollPane(table);
        TableUtil.tableStyleUtil(table, scroll, 500, false); // 스타일 유틸 적용
        int[] columnWidths = {120, 100, 120, 210, 90, 80, 80, 70, 110};
        for (int i = 0; i < table.getColumnCount(); i++) {
        	table.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
        }

        // 테이블 header 스타일 추가적으로 적용 가능
        JTableHeader header = table.getTableHeader();
        header.setBackground(Config.LIGHT_GREEN);
        header.setFont(new Font("Noto Sans KR", Font.BOLD, 15));
        header.setPreferredSize(new Dimension(Integer.MIN_VALUE, 33));
        header.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

     // scroll을 감싸는 패널 생성 (여백 + 테두리 적용)
        JPanel scrollWrapper = new JPanel(new BorderLayout());
        scrollWrapper.setBackground(Config.WHITE);

        // 얇은 테두리 + 내부 여백 적용
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 15));
        scrollWrapper.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        scrollWrapper.add(scroll, BorderLayout.CENTER);

        // ★ dummyPanel 생성해서 크기 강제
        JPanel dummyPanel = new JPanel(null);
        dummyPanel.setPreferredSize(new Dimension(Config.CONTENT_W, 500));
        dummyPanel.setBackground(Config.WHITE);

        scrollWrapper.setBounds(0, 0, Config.CONTENT_W, 500);
        dummyPanel.add(scrollWrapper);

        // 전체 레이아웃 조립
        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.setOpaque(false);
        topContainer.add(topPanel, BorderLayout.NORTH);
        topContainer.add(buttonPanel, BorderLayout.CENTER);

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(Config.CONTENT_W, Config.CONTENT_H));

        add(topContainer, BorderLayout.NORTH);
        add(dummyPanel, BorderLayout.CENTER);


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
