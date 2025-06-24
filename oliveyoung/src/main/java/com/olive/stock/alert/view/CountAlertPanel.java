package com.olive.stock.alert.view;
import com.olive.common.view.Panel;

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
import javax.swing.table.JTableHeader;

import com.olive.common.config.Config;
import com.olive.common.model.User;
import com.olive.common.util.TableUtil;
import com.olive.common.util.style.ButtonUtil;
import com.olive.common.util.style.LabelUtil;
import com.olive.mainlayout.MainLayout;
import com.olive.stock.StockConfig;
import com.olive.stock.StockPage;
import com.olive.stock.StockPanel;
import com.olive.stock.model.ListModel;
import com.olive.store.StorePage;

public class CountAlertPanel extends Panel{
	

    JTable table;
    ListModel model;
    MainLayout mainLayout;
    
    @Override
    public void refresh() {
        model.reload();     // ListModel에서 최신 데이터 로드
        table.updateUI();   // 테이블 UI 갱신
    }

    public CountAlertPanel(MainLayout mainLayout) {
        super(mainLayout);
        this.mainLayout = mainLayout;
        setLayout(new BorderLayout());
        setBackground(Config.WHITE);

        // 상단 패널
        JPanel topPanel = new JPanel(new BorderLayout());
        StockConfig.panelStyle(topPanel);
        topPanel.setBorder(BorderFactory.createEmptyBorder(30, 27, 10, 20));

        JLabel titleLabel = new JLabel("재고 수량 부족 ");
        LabelUtil.applyTitleStyle(titleLabel);
        topPanel.add(titleLabel, BorderLayout.WEST);

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
        JButton btnQtyDesc = ButtonUtil.greenButtonUtil("재고수량 ↓");
        JButton btnNameAsc = ButtonUtil.greenButtonUtil("상품명 ↑");
        JButton btnNameDesc = ButtonUtil.greenButtonUtil("상품명 ↓");

        JButton[] buttons = {btnDateAsc, btnDateDesc, btnQtyDesc, btnNameAsc, btnNameDesc};
        for (JButton btn : buttons) {
            buttonPanel.add(btn);
        }


        // 테이블 생성
        model = new ListModel("countAlert", mainLayout.user);
        table = new JTable(model);

        // 테이블 스타일 적용
        TableUtil.applyStyle(table);
        
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
        
        int[] columnWidths = {120, 100, 120, 210, 90, 80, 70, 110};

        // 컬럼별 렌더러 적용
        for (int i = 0; i < table.getColumnCount(); i++) {
            if (i == quantityColumnIndex) {
                table.getColumnModel().getColumn(i).setCellRenderer(redTextRenderer);
            } else {
                table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        	table.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
        }

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(Color.WHITE);
        
        // 전체 조립
        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.setOpaque(false);
        topContainer.add(topPanel, BorderLayout.NORTH);
        topContainer.add(buttonPanel, BorderLayout.CENTER);

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
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 15));
        scrollWrapper.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));

        scrollWrapper.add(scroll, BorderLayout.CENTER);
        
        // 전체 레이아웃 구성
        add(topContainer, BorderLayout.NORTH);
        add(scrollWrapper, BorderLayout.CENTER);
        
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
