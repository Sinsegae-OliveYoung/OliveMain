package com.olive.bound.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;

import com.olive.bound.BoundPage;
import com.olive.common.config.Config;
import com.olive.common.model.Bound;
import com.olive.common.model.Branch;
import com.olive.common.repository.InboundDAO;
import com.olive.common.view.Panel;

public class InboundShowPanel extends Panel{
	// 상단
	JPanel topPanel;
	JPanel rightButtonPanel;
	JLabel titleLabel;
	JComboBox<Branch> cb_branch;
	JButton bt_delete;
	JButton bt_save;
	
	// 중앙
	JPanel p_center;
	
	// 좌측 요청서 목록
	JPanel p_list;
	JTable table_list;
	
	// 우측 요청서 상세 조회
	JScrollPane scroll_list;
	JPanel p_detail;
	JScrollPane scroll_detail;
	JTable table_detail;

	Bound bound;
	InboundListModel model;
    InboundDAO inboundDAO;

    public InboundShowPanel(BoundPage boundPage) {
        super(boundPage);
        setLayout(new BorderLayout());

        // 공통 색상 및 폰트
        Color bgColor = new Color(245, 248, 250);
        Color comboColor = new Color(100, 149, 237); // Cornflower Blue
        Font defaultFont = new Font("SansSerif", Font.PLAIN, 13);

        setBackground(Config.WHITE);

        // 상단 패널
        topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Config.WHITE);
        topPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
        
        rightButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        rightButtonPanel.setBackground(Config.WHITE);

        // 제목 라벨
        titleLabel = new JLabel("입고요청서 List");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        titleLabel.setForeground(new Color(40, 40, 40));
        titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
        
        // 상단 삭제 버튼
        bt_delete = new JButton("삭제");
        bt_delete.setPreferredSize(new Dimension(80, 30));
        bt_delete.setBackground(Config.LIGHT_GRAY);
        
        // 상단 저장 버튼
        bt_save = new JButton("저장");
        bt_save.setPreferredSize(new Dimension(80, 30));
        bt_save.setBackground(Config.LIGHT_GRAY);
        
        // 상단 패널에 요소 부착
        rightButtonPanel.add(bt_save);
        rightButtonPanel.add(bt_delete);

        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(rightButtonPanel, BorderLayout.EAST);

        
        // 중앙 패널
        p_center = new JPanel(new BorderLayout());
        
        // 리스트 테이블 생성
        bound = new Bound();
//        model = new InboundListModel(bound);  // # 입고 요청서 model 연결
        
        model = new InboundListModel("now");  // # 입고 요청서 model 연결
        table_list = new JTable(model);

        // 리스트 테이블 헤더 스타일
        table_list.setRowHeight(25);
        table_list.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table_list.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        table_list.getTableHeader().setBackground(Config.LIGHT_GREEN); // 테이블 헤더 배경색 설정
        table_list.getTableHeader().setForeground(Color.DARK_GRAY);

        // 리스트 테이블 셀 가운데 정렬
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table_list.getColumnCount(); i++) {
        	table_list.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        scroll_list = new JScrollPane(table_list);
        scroll_list.getViewport().setBackground(Config.WHITE);
        
        // 상세 테이블 생성
        table_detail = new JTable(model);
        
        // 리스트 테이블 헤더 스타일
        table_detail.setRowHeight(25);
        table_detail.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table_detail.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        table_detail.getTableHeader().setBackground(Config.LIGHT_GREEN); // 테이블 헤더 배경색 설정
        table_detail.getTableHeader().setForeground(Color.DARK_GRAY);
        
        scroll_detail = new JScrollPane(table_detail);
        scroll_detail.getViewport().setBackground(Config.WHITE);

        // SplitPane 생성
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scroll_list, scroll_detail);
        splitPane.setDividerLocation(500); // 초기 분할 위치 (px)
        splitPane.setResizeWeight(0.5); // 크기 조절 시 왼쪽:오른쪽 비율
        splitPane.setContinuousLayout(true);
        splitPane.setOneTouchExpandable(true); // 화살표로 접었다 펼 수 있게

        // 중앙 패널에 SplitPane 추가
        p_center.add(splitPane, BorderLayout.CENTER);

        // 전체 레이아웃 구성
        add(topPanel, BorderLayout.NORTH);
        add(p_center, BorderLayout.CENTER);

    }

}