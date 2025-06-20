package com.olive.common.view;

import java.awt.Dimension;

import javax.swing.JPanel;

import com.olive.common.config.Config;
import com.olive.mainlayout.MainLayout;
import com.olive.store.StorePage;

public class Panel extends JPanel{
	protected MainLayout mainLayout;
	protected StorePage storePage;
	
	// 입출고 사이드바 패널
	public Panel(MainLayout mainLayout) {
		this.mainLayout = mainLayout;
		setPreferredSize(new Dimension(Config.CONTENT_W, Config.CONTENT_H));
		setVisible(false);
	}
	public Panel(StorePage storePage) {
		this.storePage = storePage;
		setPreferredSize(new Dimension(Config.CONTENT_W, Config.CONTENT_H));
		setVisible(false);
	}
}
