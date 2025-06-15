package com.olive.store.storeconfig.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

import com.olive.common.config.Config;
import com.olive.common.exception.BranchException;
import com.olive.common.exception.UserException;
import com.olive.common.model.Branch;
import com.olive.common.repository.BranchDAO;
import com.olive.common.util.DBManager;
import com.olive.common.view.Panel;
import com.olive.store.StorePage;
import com.olive.store.stores.submit.view.EditFrame;
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

	RegistFrame registFrame;
	EditFrame editFrame;
	
	public StoreConfigModel storeConfigModel;
	Branch selectedBranch; // 선택된 테이블 행값을 저장

	DBManager dbManager = DBManager.getInstance();
	BranchDAO branchDAO;
	
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

		branchDAO = new BranchDAO();
		
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
		table.setRowSelectionAllowed(true); // 행 선택 활성화
		table.setColumnSelectionAllowed(false); // 열 선택 비활성화
		table.setRequestFocusEnabled(false); // 셀 포커스(테두리) 비활성화
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // 다중선택 비활성화
		table.setBackground(Config.WHITE); // 셀 배경색
		table.setFont(new Font("Noto Sans KR", Font.PLAIN, 13));
		table.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, Color.LIGHT_GRAY));
		table.setSelectionBackground(Config.LIGHT_GRAY); // 선택된 행 배경색
		table.setSelectionForeground(Color.BLACK); // 선택된 행 텍스트 색

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

		// assemble
		p_title.add(lb_title);
		add(p_title);

		p_btns.add(bt_regist);
		p_btns.add(bt_edit);
		p_btns.add(bt_delete);
		add(p_btns);

		add(scroll);

		/* event listener */
		// table
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int row = table.rowAtPoint(e.getPoint());
				selectedBranch = storeConfigModel.getBranchAt(row);
			}
		});

		// button
		for (JButton btn : new JButton[] { bt_regist, bt_edit, bt_delete }) {
			btn.addMouseListener(new MouseAdapter() {
				public void mouseEntered(MouseEvent e) {
					btn.setBackground(Config.GREEN);
				};

				public void mouseExited(MouseEvent e) {
					btn.setBackground(Config.LIGHT_GRAY);
				};

				public void mouseClicked(MouseEvent e) {
					JButton source = (JButton) e.getSource();
					if (source == bt_regist)
						registFrame = new RegistFrame(StoreConfigMenu.this);
					else if (source == bt_edit) {
						if (selectedBranch != null)
							editFrame = new EditFrame(StoreConfigMenu.this, selectedBranch);
						else
							JOptionPane.showMessageDialog(StoreConfigMenu.this, "수정할 지점을 선택해주세요");
					} else if (source == bt_delete) {
						if (selectedBranch != null) {
							int result = JOptionPane.showConfirmDialog(StoreConfigMenu.this, "정말 삭제하시겠습니까?", "중요", JOptionPane.YES_NO_OPTION);
							if (result == JOptionPane.YES_OPTION) delete();
						} else
							JOptionPane.showMessageDialog(StoreConfigMenu.this, "삭제할 지점을 선택해주세요");
					}
				}
			});
		}
	}
	
	public void loadData() {
		storeConfigModel.list = storeConfigModel.branchDAO.selectBranch();
		storeConfigModel.fireTableDataChanged();
		table.revalidate();
		table.repaint();
	}
	
	// 테이블의 한 행값을 삭제
	public void delete() {
		Connection con = dbManager.getConnection();
		try {
			con.setAutoCommit(false);
			
			Branch branch = new Branch();
			branch.setBr_id(selectedBranch.getBr_id());
			
			branchDAO.delete(branch);
					
			con.commit();
			JOptionPane.showMessageDialog(this, "지점이 삭제되었습니다");
			loadData();
		} catch (BranchException | UserException e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();														
			JOptionPane.showMessageDialog(this, e.getMessage());	
		}	catch (SQLException e) {
			e.printStackTrace();
		}	finally {
			try {
				con.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
