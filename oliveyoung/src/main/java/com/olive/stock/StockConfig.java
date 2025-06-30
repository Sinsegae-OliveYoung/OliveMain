package com.olive.stock;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.olive.common.config.Config;

public class StockConfig {
//	색상정의
	
	
	public static void panelStyle(JPanel topPanel) {
		topPanel.setBackground(Config.WHITE);
		topPanel.setBorder(BorderFactory.createEmptyBorder(25, 20, 0, 20));
	}
}
