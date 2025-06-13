package com.olive.common.view;

import java.awt.Dimension;

import javax.swing.JPanel;

import com.olive.bound.BoundPage;
import com.olive.common.config.Config;
import com.olive.store.StorePage;

public class Panel extends JPanel{
	protected StorePage storePage;
	
	protected BoundPage boundPage;
	
	// 재고 사이드바 패널
	public Panel(StorePage storePage) {
		this.storePage = storePage;
		setPreferredSize(new Dimension(Config.CONTENT_W, Config.CONTENT_H));
		setVisible(false);
	}
	
	// 입출고 사이드바 패널
	public Panel(BoundPage boundPage) {
		this.boundPage = boundPage;
		setPreferredSize(new Dimension(Config.CONTENT_W, Config.CONTENT_H));
		setVisible(false);
	}
}
