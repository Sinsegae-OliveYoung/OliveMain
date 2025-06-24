package com.olive.common.util.style;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.border.LineBorder;

import com.olive.common.config.Config;

public class ButtonUtil {
	
	public static JButton greenButtonUtil(String title) {
		JButton bt = new JButton(title);
		bt.setBackground(Config.LIGHT_GRAY);
		bt.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
		bt.setFont(new Font("Noto Sans KR", Font.BOLD, 16));
		bt.setPreferredSize(Config.BUTTON_SIZE);
		bt.setFocusPainted(false);
		
		bt.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				bt.setBackground(Config.GREEN);
			}
			public void mouseExited(MouseEvent e) {
				bt.setBackground(Config.LIGHT_GRAY);
			}
		});

		return bt; 
	}
	
	public static JButton pinkButtonUtil(String title) {
		JButton bt = new JButton(title);
		bt.setBackground(Config.LIGHT_GRAY);
		bt.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
		bt.setFont(new Font("Noto Sans KR", Font.BOLD, 16));
		bt.setPreferredSize(Config.BUTTON_SIZE);
		bt.setFocusPainted(false);
		
		bt.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				bt.setBackground(Config.PINK);
			}
			public void mouseExited(MouseEvent e) {
				bt.setBackground(Config.LIGHT_GRAY);
			}
		});

		return bt; 
	}
	
	public static JButton anotherButtonUtil(String title, int fontSize) {
		JButton bt = new JButton(title);
		bt.setPreferredSize(Config.BUTTON_SIZE);
		bt.setBackground(Config.LIGHT_GREEN);
		bt.setFont(new Font("Noto Sans KR", Font.BOLD, fontSize));
		bt.setFocusPainted(false);
		
		return bt; 
	}
	
	// 타이틀 뒤로가기 버튼 
	public static JButton createTransparentButton(String title) {
		JButton button = new JButton(title);
	    button.setOpaque(false);
	    button.setContentAreaFilled(false);
	    button.setBorderPainted(false);
	    button.setFocusPainted(false);
	    
	    button.setFont(new Font("Noto Sans KR", Font.BOLD, 18));
	    button.addMouseListener(new MouseAdapter() {
	        @Override
	        public void mouseEntered(MouseEvent e) {
	            button.setForeground(Config.DARK_GREEN); // 마우스 올렸을 때
	        }

	        @Override
	        public void mouseExited(MouseEvent e) {
	            button.setForeground(Color.BLACK); // 원래대로 복원
	        }
	    });

	    return button;
	}
	
}
