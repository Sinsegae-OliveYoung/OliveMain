package com.olive.common.util.style;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.plaf.basic.BasicComboBoxUI;

import com.olive.common.config.Config;
import com.olive.common.model.BoundState;
import com.olive.common.model.Branch;
import com.olive.common.model.Category;
import com.olive.common.model.Role;
import com.olive.common.repository.BoundStateDAO;
import com.olive.common.repository.BranchDAO;
import com.olive.common.repository.CategoryDAO;
import com.olive.common.repository.RoleDAO;

/* 각 페이지에서 크기와 border style만 추가로 설정해주세요!
 * comboBox.setUI(new ComboBoxUtil());
 * comboBox.setPrefferedSize(new Dimension(width, 30));
 *	comboBox.setBorder(new LineBorder(Color.GRAY, 1, true)); */

public class ComboBoxUtil extends BasicComboBoxUI {
	static Font font = new Font("SansSerif", Font.PLAIN, 14);

	protected JButton createArrowButton() {
		JButton button = new JButton("▼");
		button.setFont(font);
		button.setForeground(Color.BLACK);
		button.setBackground(Config.LIGHT_GREEN);
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		button.setFocusable(false);
		return button;
	}

	public static void applyDefaultStyle(JComboBox cb) {
		cb.setFont(font);
		cb.setBackground(Config.WHITE);
		cb.setForeground(Color.DARK_GRAY);
		cb.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		cb.setFocusable(false);
	}

	public static JComboBox<Role> createRoleComboBox() {
		RoleDAO roleDAO = new RoleDAO();
		List<Role> list = roleDAO.selectAll();

		JComboBox<Role> cb = new JComboBox<Role>();
		cb.setUI(new ComboBoxUtil());

		Role r = new Role();
		r.setRole_name("직급");
		cb.addItem(r);

		for (int i = 0; i < list.size(); i++) {
			cb.addItem(list.get(i));
		}
		return cb;
	}
	
	public static JComboBox<Role> createRoleComboBoxWithNoDummy(int roleId) {
		RoleDAO roleDAO = new RoleDAO();
		List<Role> list = roleDAO.selectAll();

		JComboBox<Role> cb = new JComboBox<Role>();
		cb.setUI(new ComboBoxUtil());

		for (int i = roleId; i < list.size(); i++) {
			cb.addItem(list.get(i));
		}
		return cb;
	}

	public static JComboBox<BoundState> createBoundStateComboBox() {
		BoundStateDAO boundStateDAO = new BoundStateDAO();
		List<BoundState> list = boundStateDAO.selectAll();

		JComboBox<BoundState> cb = new JComboBox<>();
		cb.setUI(new ComboBoxUtil());

		BoundState bs = new BoundState();
		bs.setBo_state_name("상태");
		cb.addItem(bs);

		for (int i = 0; i < list.size(); i++) {
			cb.addItem(list.get(i));
		}
		return cb;
	}
	
	public static JComboBox<Branch> createBranchComboBox(int userId){
		BranchDAO branchDAO = new BranchDAO();
		List<Branch> list = branchDAO.getBranchList(userId);
	
		JComboBox<Branch> cb = new JComboBox<>();
		cb.setUI(new ComboBoxUtil());

		Branch br = new Branch();
		br.setBr_name("지점");
		cb.addItem(br);

		for (int i = 0; i < list.size(); i++) {
			cb.addItem(list.get(i));
		}

		return cb;
	}
	
	public static JComboBox<Branch> createBranchComboBoxWithNoDummy(int userId){
		BranchDAO branchDAO = new BranchDAO();
		List<Branch> list = branchDAO.getBranchList(userId);
	
		JComboBox<Branch> cb = new JComboBox<>();
		cb.setUI(new ComboBoxUtil());

		for (int i = 0; i < list.size(); i++) {
			cb.addItem(list.get(i));
		}

		return cb;
	}
	
	
	public static JComboBox<Category> createCategoryComboBox() {
	    CategoryDAO categoryDAO = new CategoryDAO();
	    List<Category> list = categoryDAO.selectAll();

	    JComboBox<Category> cb = new JComboBox<>();
	    cb.setUI(new ComboBoxUtil());
	    cb.setEditable(true); // ✅ 입력 가능하도록 설정

	    Category ct = new Category();
	    ct.setCt_name("전체");
	    cb.addItem(ct);

	    for (Category category : list) {
	        cb.addItem(category);
	    }

	    return cb;
	}
}