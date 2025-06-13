package com.olive.common.view;

import java.awt.Dimension;

import com.olive.common.config.Config;
import com.olive.store.StorePage;

public class Menu1Panel extends Panel{
	
	public Menu1Panel(StorePage storePage) {
		super(storePage);
		
		setPreferredSize(new Dimension(Config.CONTENT_W, Config.CONTENT_H));
		setBackground(Config.LIGHT_GREEN);
	}
}
