package com.olive.common.util.style;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.util.List;

import javax.swing.JComboBox;

import com.olive.common.config.Config;
import com.olive.common.model.Role;
import com.olive.common.repository.RoleDAO;

public class ComboBoxUtil {
	static Font font = new Font("SansSerif", Font.PLAIN, 14); 

	public static void applyDefaultStyle(JComboBox cb) {
		cb.setFont(font);
		cb.setBackground(Config.LIGHT_GREEN);
		cb.setForeground(Color.DARK_GRAY);
		cb.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		cb.setFocusable(false);
	}
	
	
	public static JComboBox<Role> createRoleComboBox() {
		RoleDAO roleDAO = new RoleDAO();
		List<Role> list = roleDAO.selectAll();
		
		JComboBox<Role> cb = new JComboBox<Role>();
		applyDefaultStyle(cb);
		
		for(int i = 0; i < list.size(); i++) {
			cb.addItem(list.get(i));
		}
		return cb;
	}
}
