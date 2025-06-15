package com.olive.store.report.view;

import java.awt.Color;
import java.awt.Dimension;

import com.olive.common.config.Config;
import com.olive.common.view.Panel;
import com.olive.store.StorePage;

public class ReportStoreMenu extends Panel{
	
	public ReportStoreMenu(StorePage storePage) {
		super(storePage);
		
		setPreferredSize(new Dimension(Config.CONTENT_W, Config.CONTENT_H));
		setBackground(Color.GREEN);
	}
}
