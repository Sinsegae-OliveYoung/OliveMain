package com.olive.bound.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;

import com.olive.bound.BoundPage;
import com.olive.common.config.Config;
import com.olive.common.model.Bound;
import com.olive.common.model.Branch;
import com.olive.common.repository.InboundDAO;
import com.olive.common.view.Panel;
import com.toedter.calendar.JDateChooser;

public class InboundShowPanel2 extends Panel{
	// 상단
	JPanel topPanel;
	JPanel rightButtonPanel;
	JLabel titleLabel;
	JComboBox<Branch> cb_branch;
	JComboBox<Branch> cb_appuser;
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
	JDateChooser dateChooser;
	

	Bound bound;
	InboundListModel model;
    InboundDAO inboundDAO;

    public InboundShowPanel2(BoundPage boundPage) {
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
        titleLabel = new JLabel("입고요청서 List2");
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
        p_center = new JPanel();
        p_center.setPreferredSize(new Dimension(Config.CONTENT_W-200, Config.CONTENT_H-200));
        
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

        
        
     // 중앙 패널을 BorderLayout 유지
        p_center = new JPanel(new BorderLayout());

        // 왼쪽 스크롤 패널 (JTable)
        scroll_list = new JScrollPane(table_list);
        scroll_list.setPreferredSize(new Dimension(600, 0)); // 고정 너비 설정

        // 오른쪽 상세 패널 생성
        createDetailPanel(); // 아래 별도 메서드

        // 좌측: 테이블, 우측: 입력 상세
        p_center.add(scroll_list, BorderLayout.WEST);
        p_center.add(p_detail, BorderLayout.CENTER);
        
        
        
        
        
        
        
        
        
        
        
        
        
     // JTable 클릭 이벤트 처리
        table_list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table_list.getSelectedRow();
                if (row != -1) {
                    Bound selected = model.getBoundAt(row);
                    showDetail(selected);
                }
            }
        });

    }
    
    
    private void showDetail(Bound bound) {
        // 예시: 콤보박스와 날짜, 메모 텍스트 필드 등에 값 설정
        cb_branch.setSelectedItem(bound.getBranch());
        cb_appuser.setSelectedItem(bound.getUser());
        dateChooser.setDate(bound.getRequest_date());
        
        // 메모 등 추가 텍스트 필드가 있다면 여기에 설정
        // 예: t_memo.setText(bound.getMemo());

        // 상품 리스트가 있다면 table_detail에 바인딩 필요
    }

    
    private void createDetailPanel() {
        p_detail = new JPanel(new GridBagLayout());
        p_detail.setBackground(Config.WHITE);
        p_detail.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 5, 10, 5); // 위아래 여백
        gbc.gridx = 0;
        gbc.gridy = 0;

        // 지점 선택
        p_detail.add(new JLabel("지점:"), gbc);
        gbc.gridx = 1;
        cb_branch = new JComboBox<>();
        cb_branch.setPreferredSize(new Dimension(200, 30));
        p_detail.add(cb_branch, gbc);

        // 다음 행
        gbc.gridy++;
        gbc.gridx = 0;
        p_detail.add(new JLabel("결재자:"), gbc);
        gbc.gridx = 1;
        cb_appuser = new JComboBox<>();
        cb_appuser.setPreferredSize(new Dimension(200, 30));
        p_detail.add(cb_appuser, gbc);

        // 요청일
        gbc.gridy++;
        gbc.gridx = 0;
        p_detail.add(new JLabel("요청일:"), gbc);
        gbc.gridx = 1;
        dateChooser = new JDateChooser();
        dateChooser.setPreferredSize(new Dimension(200, 30));
        p_detail.add(dateChooser, gbc);

        // 메모
        gbc.gridy++;
        gbc.gridx = 0;
        p_detail.add(new JLabel("메모:"), gbc);
        gbc.gridx = 1;
        JTextField t_memo = new JTextField();
        t_memo.setPreferredSize(new Dimension(200, 30));
        p_detail.add(t_memo, gbc);
    }

}