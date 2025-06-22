package com.olive.common.util;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JTable;
import javax.swing.table.TableModel;

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
   }
   
}