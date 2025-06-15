package com.olive.store.storeconfig.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import com.olive.common.config.Config;
import com.olive.common.view.Panel;
import com.olive.common.view.SubmitFrame;
import com.olive.store.StorePage;
import com.olive.store.stores.submit.view.RegistFrame;

public class StoreConfigMenu extends Panel {

	JPanel p_title;
	JLabel lb_title;

	JPanel p_btns;
	JButton bt_regist;
	JButton bt_edit;
	JButton bt_delete;

	JTable table;
	JScrollPane scroll;

	StoreConfigModel storeConfigModel;
	SubmitFrame registFrame, editFrame, deleteFrame;

	public StoreConfigMenu(StorePage storePage) {
		super(storePage);

		// create
		p_title = new JPanel();
		lb_title = new JLabel("지점 관리");

		p_btns = new JPanel();
		bt_regist = new JButton("등록");
		bt_edit = new JButton("수정");
		bt_delete = new JButton("삭제");

		table = new JTable(storeConfigModel = new StoreConfigModel());
		scroll = new JScrollPane(table);

		// style
		setLayout(new FlowLayout());
		setPreferredSize(new Dimension(Config.CONTENT_W, Config.CONTENT_H));
		setBackground(Config.WHITE);

		p_title.setPreferredSize(new Dimension(Config.CONTENT_W, 70));
		p_title.setBorder(BorderFactory.createEmptyBorder(20, 40, 0, 0));
		p_title.setLayout(new FlowLayout(FlowLayout.LEFT));
		p_title.setOpaque(false);

		lb_title.setFont(new Font("Noto Sans KR", Font.BOLD, 26));
		lb_title.setHorizontalAlignment(JLabel.RIGHT);

		p_btns.setPreferredSize(new Dimension(Config.CONTENT_W, 55));
		p_btns.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 50));
		p_btns.setLayout(new FlowLayout(FlowLayout.RIGHT));
		p_btns.setOpaque(false);

		bt_regist.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
		bt_regist.setFont(new Font("Noto Sans KR", Font.BOLD, 16));
		bt_regist.setPreferredSize(new Dimension(50, 30));
		bt_regist.setBackground(Config.LIGHT_GRAY);
		bt_regist.setFocusPainted(false);

		bt_edit.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
		bt_edit.setFont(new Font("Noto Sans KR", Font.BOLD, 16));
		bt_edit.setPreferredSize(new Dimension(50, 30));
		bt_edit.setBackground(Config.LIGHT_GRAY);
		bt_edit.setFocusPainted(false);

		bt_delete.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
		bt_delete.setFont(new Font("Noto Sans KR", Font.BOLD, 16));
		bt_delete.setPreferredSize(new Dimension(50, 30));
		bt_delete.setBackground(Config.LIGHT_GRAY);
		bt_delete.setFocusPainted(false);

		
		/* 테이블 설정 */

		// 테이블 헤더
        JTableHeader header = table.getTableHeader();
        header.setBackground(Config.LIGHT_GREEN);
        header.setFont(new Font("Noto Sans KR", Font.BOLD, 15));
        header.setPreferredSize(new Dimension(Integer.MIN_VALUE, 33));
        header.setBorder(new LineBorder(Color.GRAY, 1, true));
        
        table.setGridColor(Color.WHITE);
        table.setRowSelectionAllowed(true);            // 행 선택 활성화
        table.setColumnSelectionAllowed(false);      // 열 선택 비활성화
        table.setRequestFocusEnabled(false);          // 셀 포커스(테두리) 비활성화
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);	// 다중선택 비활성화
		table.setBackground(Config.WHITE);	// 셀 배경색
		table.setFont(new Font("Noto Sans KR", Font.PLAIN, 13));
		table.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, Color.LIGHT_GRAY));
		table.setSelectionBackground(Config.LIGHT_GRAY); // 선택된 행 배경색
		table.setSelectionForeground(Color.BLACK);              // 선택된 행 텍스트 색

		// 행 높이
		table.setRowHeight(30);
        // 열 너비
        table.getColumnModel().getColumn(0).setPreferredWidth(15);
        table.getColumnModel().getColumn(1).setPreferredWidth(60);
        table.getColumnModel().getColumn(2).setPreferredWidth(60);
        table.getColumnModel().getColumn(3).setPreferredWidth(300);
        table.getColumnModel().getColumn(4).setPreferredWidth(30);
        
		// 셀 글자 정렬
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
		dtcr.setHorizontalAlignment(SwingConstants.CENTER);
		TableColumnModel tcm = table.getColumnModel();
		for (int i = 0; i < tcm.getColumnCount(); i++)
			tcm.getColumn(i).setCellRenderer(dtcr);

		scroll.setBorder(BorderFactory.createEmptyBorder());
		scroll.getViewport().setBackground(Config.WHITE);
		scroll.setPreferredSize(new Dimension(Config.CONTENT_W - 100, 530));

		/*  레이아웃 갱신 */
		// table.revalidate();
		// table.repaint();
		
		// assemble
		p_title.add(lb_title);
		add(p_title);

		p_btns.add(bt_regist);
		p_btns.add(bt_edit);
		p_btns.add(bt_delete);
		add(p_btns);

		add(scroll);
		
		// event listener
		for (JButton btn : new JButton[] { bt_regist, bt_edit, bt_delete }) {
			btn.addMouseListener(new MouseAdapter() {
				public void mouseEntered(MouseEvent e) {btn.setBackground(Config.GREEN);};
				public void mouseExited(MouseEvent e) {btn.setBackground(Config.LIGHT_GRAY);};
				public void mouseClicked(MouseEvent e) {
				      JButton source = (JButton) e.getSource();
				     if (source == bt_regist)
				    	  registFrame = new RegistFrame();
				      else if (source == bt_edit)
				    	  editFrame = new EditFrame();
				      else if (source == bt_delete)
				    	  deleteFrame = new DeleteFrame();
				}
			});
		}
	}
}
