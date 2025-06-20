package com.olive.store.stores.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

import com.olive.common.config.Config;
import com.olive.common.model.Branch;
import com.olive.common.repository.BranchDAO;
import com.olive.common.view.Panel;
import com.olive.mainlayout.MainLayout;

public class StoresMenu extends Panel {

	JPanel p_title;
	JLabel lb_title;

	JPanel p_details;
	JLabel lb_top;
	JLabel lb_bottom;

	JTable table;
	JScrollPane scroll;

	StoresModel storesModel;
	BranchDAO branchDAO;
	List<Branch> list;

	public StoresMenu(MainLayout mainLayout, String storeName) {
		super(mainLayout);

		// create
		p_title = new JPanel();
		lb_title = new JLabel(storeName);

		p_details = new JPanel();

		table = new JTable(storesModel = new StoresModel(storeName));
		scroll = new JScrollPane(table);

		branchDAO = new BranchDAO();
		list = branchDAO.selectBranchDetail(storeName);
		lb_top = new JLabel("담당자 : " + list.get(0).getUser().getUser_name() + "     이메일 : " + list.get(0).getUser().getEmail() + "     연락처 : " + list.get(0).getUser().getTel());
		lb_bottom = new JLabel("매장 주소 : " + list.get(0).getBr_address() + "     매장 전화 : " + list.get(0).getBr_tel());

		// style
		setLayout(new FlowLayout());
		setPreferredSize(new Dimension(Config.CONTENT_W, Config.CONTENT_H));
		setBackground(Config.WHITE);

		p_title.setPreferredSize(new Dimension(Config.CONTENT_W, 60));
		p_title.setBorder(BorderFactory.createEmptyBorder(20, 40, 0, 0));
		p_title.setLayout(new FlowLayout(FlowLayout.LEFT));
		p_title.setOpaque(false);

		lb_title.setFont(new Font("Noto Sans KR", Font.BOLD, 26));
		lb_title.setHorizontalAlignment(JLabel.RIGHT);

		p_details.setPreferredSize(new Dimension(Config.CONTENT_W, 100));
		p_details.setBorder(BorderFactory.createEmptyBorder(0, 500, 0, 50));
		p_details.setLayout(new FlowLayout(FlowLayout.RIGHT));
		p_details.setOpaque(false);
		
		lb_top.setFont(new Font("Noto Sans KR", Font.BOLD, 16));
		lb_top.setHorizontalAlignment(JLabel.RIGHT);

		lb_bottom.setFont(new Font("Noto Sans KR", Font.BOLD, 16));
		lb_bottom.setHorizontalAlignment(JLabel.RIGHT);
		
		/* 테이블 설정 */

		// 테이블 헤더
        JTableHeader header = table.getTableHeader();
        header.setBackground(Config.LIGHT_GREEN);
        header.setFont(new Font("Noto Sans KR", Font.BOLD, 15));
        header.setPreferredSize(new Dimension(Integer.MIN_VALUE, 33));
        header.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        //header.setBorder(BorderFactory.createMatteBorder(1, 1,0, 1, Color.GRAY));
        header.setBorder(BorderFactory.createLineBorder(Color.GRAY,1));
        
        table.setGridColor(Color.WHITE);
        //table.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, Color.LIGHT_GRAY));
        table.setBorder(BorderFactory.createLineBorder(Color.GRAY,1));
        table.setCellSelectionEnabled(false);	// 행 선택 불가
        table.setRequestFocusEnabled(false);	// 셀 선택 불가
		table.setBackground(Config.WHITE);	// 셀 배경색
		table.setFont(new Font("Noto Sans KR", Font.PLAIN, 13));
		
        // 행 높이
        table.setRowHeight(30);
        // 열 너비
        table.getColumnModel().getColumn(0).setPreferredWidth(40);
        table.getColumnModel().getColumn(1).setPreferredWidth(60);
        table.getColumnModel().getColumn(2).setPreferredWidth(60);
        table.getColumnModel().getColumn(3).setPreferredWidth(300);
        table.getColumnModel().getColumn(4).setPreferredWidth(30);
        table.getColumnModel().getColumn(5).setPreferredWidth(40);
        
		// 셀 글자 정렬
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
		dtcr.setHorizontalAlignment(SwingConstants.CENTER);
		TableColumnModel tcm = table.getColumnModel();
		for (int i = 0; i < tcm.getColumnCount(); i++)
			tcm.getColumn(i).setCellRenderer(dtcr);

		scroll.setBorder(BorderFactory.createEmptyBorder());
		scroll.getViewport().setBackground(Config.WHITE);
		scroll.setPreferredSize(new Dimension(Config.CONTENT_W - 100, 490));

		// assemble
		p_title.add(lb_title);
		add(p_title);

		p_details.add(lb_top);
		p_details.add(lb_bottom);
		add(p_details);

		add(scroll);

	}
}
