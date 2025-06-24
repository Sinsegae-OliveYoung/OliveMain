package com.olive.store.storeconfig.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.LineBorder;

import com.olive.common.config.Config;
import com.olive.common.model.Branch;
import com.olive.common.repository.BranchDAO;
import com.olive.common.util.DBManager;
import com.olive.common.util.style.ButtonUtil;
import com.olive.common.view.Panel;
import com.olive.mainlayout.MainLayout;
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

	static JTable table;
	JScrollPane scroll;

	RegistFrame registFrame;
	EditFrame editFrame;

	public static StoreConfigModel storeConfigModel;
	Branch selectedBranch; // 선택된 테이블 행값을 저장

	DBManager dbManager = DBManager.getInstance();
	BranchDAO branchDAO;
	private StorePage storePage;

	public StoreConfigMenu(MainLayout mainLayout, StorePage storePage) {
		super(mainLayout);
		this.storePage = storePage;

		// create
		p_title = new JPanel();
		lb_title = new JLabel("지점 관리");

		p_btns = new JPanel();
		bt_regist = ButtonUtil.greenButtonUtil("등록");
		bt_edit = ButtonUtil.greenButtonUtil("수정");
		bt_delete = ButtonUtil.greenButtonUtil("삭제");

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

		/* 테이블 설정 */
		storePage.tableStyleUtil(table, scroll, 530);
		
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
				public void mouseClicked(MouseEvent e) {
					JButton source = (JButton) e.getSource();
					if (source == bt_regist)
						registFrame = new RegistFrame(storePage, StoreConfigMenu.this);
					else if (source == bt_edit) {
						if (selectedBranch != null)
							editFrame = new EditFrame(mainLayout, storePage, StoreConfigMenu.this, selectedBranch);
						else
							JOptionPane.showMessageDialog(StoreConfigMenu.this, "수정할 지점을 선택해주세요");
					} else if (source == bt_delete) {
						if (selectedBranch != null) {
							int result = JOptionPane.showConfirmDialog(StoreConfigMenu.this, "정말 삭제하시겠습니까?", "중요",
									JOptionPane.YES_NO_OPTION);
							if (result == JOptionPane.YES_OPTION)
								delete();
						} else
							JOptionPane.showMessageDialog(StoreConfigMenu.this, "삭제할 지점을 선택해주세요");
					}
				}
			});
		}
	}
	
	
	// 테이블 로드 및 출력
	public void loadData() {
		storeConfigModel.list = storeConfigModel.branchDAO.selectBranch();
		storeConfigModel.fireTableDataChanged();
		table.revalidate();
		table.repaint();
		storePage.tableStyleUtil(table, scroll, 530);
	}

	// 테이블의 한 행값을 삭제
	public void delete() {
		Branch branch = new Branch();
		branch.setBr_id(selectedBranch.getBr_id());		// 선택된 지점의 id값을 가져옴
		branchDAO.delete(branch, mainLayout.user);	// 쿼리문 날리기
		
		JOptionPane.showMessageDialog(this, "지점이 삭제되었습니다");
		loadData();	// 테이블 재출력
		((StorePage) storePage).createMenus(); // 사이드 메뉴 재생성
		storePage.showPanel(0);	// 삭제 후 보여줄 페이지 설정
	}

}
