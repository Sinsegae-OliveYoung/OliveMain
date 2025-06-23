package com.olive.manage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.olive.common.util.ImageUtil;

public class DatePickerPanel extends JPanel{
	ImageUtil imgUtil = new ImageUtil();
	
	public JLabel lb_date;
	JButton bt_img;
	
	public DatePickerPanel(String date) {
		// 전체 패널 설정 
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createLineBorder(Color.GRAY));
		
		// 날짜 label 
		lb_date = new JLabel(date);
		lb_date.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0)); // tlbr, 왼쪽 여백 주기
		
		add(lb_date, BorderLayout.WEST);
		
		// 달력 아이콘 
		Image img = imgUtil.getImage(ManageConfig.CALENDAR_IMAGE, 20, 20);
		bt_img = new JButton(new ImageIcon(img));
		bt_img.setBorderPainted(false);           // 테두리 제거 
		bt_img.setContentAreaFilled(false);       // 배경 제거 
		bt_img.setMargin(new Insets(0, 0, 0, 0)); // 마진 제거
		bt_img.setFocusPainted(false);            // 포커스 테두리 제거
		
		//bt_img.setOpaque(false);                // 불투명 해제 (배경 투명화)
		bt_img.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10)); // tlbr, 왼쪽 여백 주기
		add(bt_img, BorderLayout.EAST);
		
		bt_img.addActionListener(e -> {
			new DatePicker(lb_date);
		});
		
		setPreferredSize(new Dimension(120, 30));
	}
}



 


