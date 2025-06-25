package com.olive.common.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;


import com.olive.common.config.Config;

public class TableUtil{

   static Font headerFont = new Font("SansSerif", Font.BOLD, 13);
   static Font tableFont = new Font("SansSerif", Font.PLAIN, 13);
   
   
   static public void applyStyle(JTable table) {
      table.setRowHeight(25);
      table.setFont(tableFont);
      table.getTableHeader().setFont(headerFont);
      table.getTableHeader().setBackground(Config.LIGHT_GREEN); // 테이블 헤더 배경색 설정
      table.getTableHeader().setForeground(Color.DARK_GRAY);
      
      // 셀 정렬
      DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
      centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		for (int i = 0; i < table.getColumnCount(); i++) {
			table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
   		}
   }
   
	static public void tableStyleUtil(JTable table, JScrollPane scroll, int height, boolean flag){
		// 테이블 헤더
        JTableHeader header = table.getTableHeader();
        header.setBackground(Config.LIGHT_GREEN);
        header.setFont(new Font("Noto Sans KR", Font.BOLD, 15));
        header.setPreferredSize(new Dimension(Integer.MIN_VALUE, 33));
        header.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        header.setBorder(BorderFactory.createLineBorder(Color.GRAY,1));
        
        table.setGridColor(Color.WHITE);
        table.setBorder(BorderFactory.createLineBorder(Color.GRAY,1));
        table.setCellSelectionEnabled(flag);	// 행 선택 불가
        table.setRequestFocusEnabled(flag);	// 셀 선택 불가
		table.setBackground(Config.WHITE);	// 셀 배경색
		table.setFont(new Font("Noto Sans KR", Font.PLAIN, 13));
		
		// 행 높이
		table.setRowHeight(30);
	
		// 셀 글자 정렬
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
		dtcr.setHorizontalAlignment(SwingConstants.CENTER);
		TableColumnModel tcm = table.getColumnModel();
		for (int i = 0; i < tcm.getColumnCount(); i++)
			tcm.getColumn(i).setCellRenderer(dtcr);
		
		scroll.setBorder(BorderFactory.createEmptyBorder());
		scroll.getViewport().setBackground(Config.WHITE);
		scroll.setPreferredSize(new Dimension(Config.CONTENT_W - 300, height));
	}
}
