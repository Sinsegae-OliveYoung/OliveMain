package com.olive.common.util.style;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;

import com.olive.common.config.Config;

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
}
