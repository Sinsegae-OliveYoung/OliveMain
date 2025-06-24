package com.olive.common.util.style;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.util.List;

import javax.swing.JComboBox;

import com.olive.common.config.Config;
import com.olive.common.model.BoundState;
import com.olive.common.model.Branch;
import com.olive.common.model.Role;
import com.olive.common.repository.BoundStateDAO;
import com.olive.common.repository.BranchDAO;
import com.olive.common.repository.RoleDAO;

public class ComboBoxUtil {
	static Font font = new Font("SansSerif", Font.PLAIN, 14); 

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
		applyDefaultStyle(cb);
		
		
		Role r = new Role();
		r.setRole_name("직급");
		cb.addItem(r);
		
		for(int i = 0; i < list.size(); i++) {
			cb.addItem(list.get(i));
		}
		return cb;
	}
	
	public static JComboBox<BoundState> createBoundStateComboBox() {
		BoundStateDAO boundStateDAO = new BoundStateDAO();
		List<BoundState> list = boundStateDAO.selectAll();
		
		JComboBox<BoundState> cb = new JComboBox<>();
		applyDefaultStyle(cb);
		
		BoundState bs = new BoundState();
		bs.setBo_state_name("상태");
		cb.addItem(bs);
		
		for(int i = 0; i < list.size(); i++) {
			cb.addItem(list.get(i));
		}
		return cb;
	}
	
	public static JComboBox<Branch> createBranchComboBox(int userId){
		BranchDAO branchDAO = new BranchDAO();
		List<Branch> list = branchDAO.getBranchList(userId);
		
		JComboBox<Branch> cb = new JComboBox<>();
		applyDefaultStyle(cb);
		
		Branch br = new Branch();
		br.setBr_name("지점");
		cb.addItem(br);
		
		for(int i = 0; i < list.size(); i++) {
			cb.addItem(list.get(i));
		}
		
		return cb;
	}
}
