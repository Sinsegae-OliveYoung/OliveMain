package com.olive.product.view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import com.olive.common.config.Config;
import com.olive.common.model.User;
import com.olive.common.view.Panel;
import com.olive.mainlayout.MainLayout;


public class BrandPanel  extends Panel{

	MainLayout mainLayout;
	User user; // 로그인한 계정 객체
	
	public BrandPanel(MainLayout mainLayout) {
		super(mainLayout);
		setLayout(new BorderLayout());
		
		this.mainLayout = mainLayout;
		this.user = mainLayout.user;
		
		setPreferredSize(new Dimension(Config.CONTENT_W, Config.CONTENT_H));
		setBackground(Config.DARK_GREEN);
	}
}
