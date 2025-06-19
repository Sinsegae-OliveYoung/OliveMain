package com.olive.common.view;

import java.awt.Dimension;

import com.olive.common.config.Config;
import com.olive.mainlayout.MainLayout;

public class Menu1Panel extends Panel{
	
	public Menu1Panel(MainLayout mainLayout) {
		super(mainLayout);
		
		setPreferredSize(new Dimension(Config.CONTENT_W, Config.CONTENT_H));
		setBackground(Config.LIGHT_GREEN);
	}
}
