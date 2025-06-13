package com.olive.common.view;

import java.awt.Dimension;

import javax.swing.JPanel;

import com.olive.common.config.Config;
import com.olive.store.StorePage;

public class Panel extends JPanel{
	protected StorePage storePage;
	
	public Panel(StorePage storePage) {
		this.storePage = storePage;
		setPreferredSize(new Dimension(Config.CONTENT_W, Config.CONTENT_H));
		setVisible(false);
	}
}
