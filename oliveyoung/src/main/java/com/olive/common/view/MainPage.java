package com.olive.common.view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

import com.olive.common.config.Config;
import com.olive.common.util.ImageUtil;
import com.olive.mainlayout.MainLayout;

public class MainPage extends Page{
	
	JPanel p_visual; 
	ImageUtil imageUtil=new ImageUtil();
	Image image;
	
	public MainPage(MainLayout mainLayout) {
		super(mainLayout);
		
		image = imageUtil.getImage("images/mainPage.PNG",Config.LAYOUT_W - 100, Config.LAYOUT_H - 100);
		
		p_visual = new JPanel() {
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);// update() 에 지워진 배경을 스스로 복구
				
				//Toolkit 은 이미지를 구성하는 바이트 정보에 접근 불가하다..
				//BufferedImage 객체를 이용하여 얻어온 이미지는 훨씬 더 다양한 제어가 가능..
				//우리가 원하는 그림을 그리자..즉 패널의 그림을 뺏어 그리자!!
				g.drawImage(image, 0, 0, Config.LAYOUT_W, Config.LAYOUT_H - 100, p_visual);
			}
		};
		//스타일
		p_visual.setPreferredSize(new Dimension(Config.LAYOUT_W, Config.LAYOUT_H - 100));
		
		add(p_visual);
		setVisible(true);
	}

}
