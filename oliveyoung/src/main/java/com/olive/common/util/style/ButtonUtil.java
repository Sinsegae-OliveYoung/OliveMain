package com.olive.common.util.style;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.border.LineBorder;

import com.olive.common.config.Config;

public class ButtonUtil {
	
	public static JButton blueButtonUtil(String title) {
	    JButton bt = new JButton(title);

	    Color baseBlue = new Color(100, 149, 237); // Cornflower Blue
	    Color hoverBlue = new Color(70, 130, 180); // Hover color: Steel Blue

	    bt.setFont(new Font("SansSerif", Font.BOLD, 13));
	    bt.setForeground(Color.WHITE);
	    bt.setBackground(baseBlue);
	    bt.setCursor(new Cursor(Cursor.HAND_CURSOR));
	    bt.setFocusPainted(false);
	    bt.setBorderPainted(false);
	    bt.setBorder(null);

	    bt.addMouseListener(new MouseAdapter() {
	        public void mouseEntered(MouseEvent e) {
	            bt.setBackground(hoverBlue);
	        }

	        public void mouseExited(MouseEvent e) {
	            bt.setBackground(baseBlue);
	        }
	    });

	    return bt;
	}
	
	public static JButton greenButtonUtil(String title) {
		JButton bt = new JButton(title);
		
		bt.setFont(new Font("SansSerif", Font.BOLD, 13));
        bt.setForeground(Color.WHITE);
        bt.setBackground(new Color(90, 160, 90));
        bt.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bt.setFocusPainted(false);
		
		bt.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				bt.setBackground(Config.GREEN);
			}
			public void mouseExited(MouseEvent e) {
			    bt.setBackground(new Color(90, 160, 90));
			}
		});

		return bt; 
	}
	public static JButton grayButtonUtil(String title) {
		JButton bt = new JButton(title);
		
		bt.setFont(new Font("SansSerif", Font.BOLD, 13));
        bt.setForeground(Color.black);
        bt.setBackground(Color.LIGHT_GRAY);
        bt.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bt.setFocusPainted(false);
		
		bt.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				bt.setBackground(Color.GRAY);
			}
			public void mouseExited(MouseEvent e) {
			    bt.setBackground(Color.LIGHT_GRAY);
			}
		});

		return bt; 
	}
	
	
	public static JButton pinkButtonUtil(String title) {
		JButton bt = new JButton(title);
		
		bt.setFont(new Font("SansSerif", Font.BOLD, 13));
        bt.setForeground(Color.WHITE);
        bt.setBackground(Config.PINK);
        bt.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bt.setFocusPainted(false);
		
		bt.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				bt.setBackground(Config.LIGHT_GRAY);
			}
			public void mouseExited(MouseEvent e) {
				bt.setBackground(Config.PINK);
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