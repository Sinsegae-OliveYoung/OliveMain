package com.olive.common.util.style;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import com.olive.stock.StockConfig;

public class LabelUtil {
	
	static Font titleFont = new Font("SansSerif", Font.BOLD, 22);
	
	/**
	 * 사이드바 메뉴 클릭 시 패널에 나오는 라벨 스타일 
	 */
	public static void applyTitleStyle(JLabel label) {
		label.setFont(titleFont);
	}
	
}
