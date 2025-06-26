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
import com.olive.store.StorePage;

public class StoresMenu extends Panel {

	JPanel p_title;
	JLabel lb_title;

	JPanel p_details;
	JLabel lb_top;
	JLabel lb_bottom;

	private JTable table;
	JScrollPane scroll;

	private StoresModel storesModel;
	private BranchDAO branchDAO;
	private List<Branch> list;
	private String storeName;
	private StorePage storePage;

	public StoresMenu(MainLayout mainLayout, StorePage storePage, String storeName) {
		super(mainLayout);
		this.storePage = storePage;
		this.storeName = storeName;
		
		// create
		p_title = new JPanel();
		lb_title = new JLabel(storeName);

		p_details = new JPanel();

		table = new JTable(storesModel = new StoresModel(storeName));
		scroll = new JScrollPane(table);

		branchDAO = new BranchDAO();
		list = branchDAO.selectBranchDetail(storeName);	// 지점의 상세 정보를 가져옴
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

		p_details.setPreferredSize(new Dimension(Config.CONTENT_W, 80));
		p_details.setBorder(BorderFactory.createEmptyBorder(0, 500, 0, 50));
		p_details.setLayout(new FlowLayout(FlowLayout.RIGHT));
		p_details.setOpaque(false);
		
		lb_top.setFont(new Font("Noto Sans KR", Font.BOLD, 16));
		lb_top.setHorizontalAlignment(JLabel.RIGHT);

		lb_bottom.setFont(new Font("Noto Sans KR", Font.BOLD, 16));
		lb_bottom.setHorizontalAlignment(JLabel.RIGHT);
		
		/* 테이블 설정 */
		storePage.tableStyleUtil(table, scroll, 490);
        table.setCellSelectionEnabled(false);	// 행 선택 불가

		// assemble
		p_title.add(lb_title);
		add(p_title);

		p_details.add(lb_top);
		p_details.add(lb_bottom);
		add(p_details);

		add(scroll);

	}

	// 테이블 로드 및 출력
	public void refresh() {
		storesModel = new StoresModel(storeName);
		table.setModel(storesModel);
		storesModel.fireTableDataChanged();
		table.revalidate();
		table.repaint();
		storePage.tableStyleUtil(table, scroll, 490);
	}
	
}
