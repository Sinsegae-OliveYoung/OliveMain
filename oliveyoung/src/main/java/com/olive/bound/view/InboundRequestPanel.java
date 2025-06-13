package com.olive.bound.view;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.olive.bound.BoundPage;
import com.olive.common.config.Config;
import com.olive.common.view.Panel;

public class InboundRequestPanel extends Panel{
	// 좌측 상품 테이블
	JPanel p_list;
	JLabel la_left;
	JTable table;
	JScrollPane scroll;
	InboundModel inboundModel;
	
	// 우측 입고 요청 폼
	JPanel p_request;
	JLabel la_right;
	
	public InboundRequestPanel(BoundPage boundPage) {
		super(boundPage);
		
		p_list = new JPanel();
		la_left = new JLabel("좌측");
		table = new JTable(inboundModel = new InboundModel());
		scroll = new JScrollPane(table);
		
		p_request = new JPanel();
		la_right = new JLabel("우측");
		
		// 스타일
		Dimension d = new Dimension(540, 730);
		p_list.setPreferredSize(d);
		p_list.setBackground(Color.gray);
		
		scroll.setPreferredSize(new Dimension(500, 600));
		
		p_request.setPreferredSize(d);
		p_request.setBackground(Color.white);

		la_left.setPreferredSize(new Dimension(400, 50));
		la_right.setPreferredSize(new Dimension(400, 50));
		
		// 조립
		p_list.add(la_left);
		p_list.add(scroll);
		
		p_request.add(la_right);
		
		
		add(p_list);
		add(p_request);
		
		setPreferredSize(new Dimension(Config.CONTENT_W, Config.CONTENT_H));
		setBackground(Color.BLUE);
	}
}