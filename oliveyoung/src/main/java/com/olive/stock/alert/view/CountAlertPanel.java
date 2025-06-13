package com.olive.stock.alert.view;

import java.awt.Dimension;

import com.olive.common.config.Config;
import com.olive.stock.StockPage;
import com.olive.stock.StockPanel;
import com.olive.store.StorePage;

public class CountAlertPanel extends StockPanel{
	
	public CountAlertPanel(StockPage stockPage) {
		super(stockPage);
		
		setPreferredSize(new Dimension(Config.CONTENT_W, Config.CONTENT_H));
		setBackground(Config.GREEN);
	}
}
