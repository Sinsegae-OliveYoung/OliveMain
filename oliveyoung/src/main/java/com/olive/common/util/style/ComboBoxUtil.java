package com.olive.common.util.style;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JComboBox;

import com.olive.common.config.Config;

public class ComboBoxUtil {
	static Font font = new Font("SansSerif", Font.PLAIN, 14); 

	public static void applyDefaultStyle(JComboBox cb) {
		cb.setFont(font);
		cb.setBackground(Config.LIGHT_GREEN);
		cb.setForeground(Color.DARK_GRAY);
		cb.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		cb.setFocusable(false);
	}
}
