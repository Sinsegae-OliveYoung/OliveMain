package com.olive.stock;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class StockConfig {
//	색상정의
	
	public static final Color bgColor = new Color(245, 248, 250);
	
	public static void panelStyle(JPanel topPanel) {
		topPanel.setBackground(bgColor);
		topPanel.setBorder(BorderFactory.createEmptyBorder(25, 20, 0, 20));
	}
}
