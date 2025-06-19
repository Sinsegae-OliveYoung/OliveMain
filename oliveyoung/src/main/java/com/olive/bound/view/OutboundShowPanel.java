package com.olive.bound.view;

import java.awt.Color;
import java.awt.Dimension;

import com.olive.common.config.Config;
import com.olive.common.view.Panel;
import com.olive.mainlayout.MainLayout;

public class OutboundShowPanel extends Panel{
	
	public OutboundShowPanel(MainLayout mainLayout) {
		super(mainLayout);
		
		setPreferredSize(new Dimension(Config.CONTENT_W, Config.CONTENT_H));
		setBackground(Color.YELLOW);
	}
}