package com.olive.manage;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.olive.common.view.Panel;
import com.olive.mainlayout.MainLayout;

public abstract class BasePanel extends Panel{
	
	public ManagePage managePage;
	JPanel p_top;
	JLabel lb_title;
	JButton bt_back;
	
	public BasePanel(MainLayout mainLayout, String title, ManagePage managePage) {
		super(mainLayout);
		this.managePage = managePage;
		setLayout(new BorderLayout());
		
		//상단 타이틀 패널
		p_top = new JPanel(new FlowLayout(FlowLayout.LEFT));
		add(p_top, BorderLayout.NORTH);
		
		//뒤로가기 버튼 
		bt_back = new JButton("<");
		p_top.add(bt_back);
		
		// 상단 타이틀
		lb_title = new JLabel(title);
		lb_title.setFont(ManageConfig.TITLE_FONT);
		p_top.add(lb_title);
		
		//실제 내용이 보일 컨텐트 패널, 자식 클래스에서 구현 
		add(createContent(), BorderLayout.CENTER);
		
		bt_back.addActionListener(e -> {
			//이전에 눌렀던 페이지를 띄우기 
			managePage.back();
		});
		
	}
	
	public abstract JPanel createContent();
	
	/**
	 * 목록페이지와 상세페이지에서 뒤로가기 버튼 visible 여부 결정 
	 * @param b : 뒤로가기 버튼 visible 여부 
	 */
	public void setButtonVisible(boolean b) {
		bt_back.setVisible(b);
	}
	
}
