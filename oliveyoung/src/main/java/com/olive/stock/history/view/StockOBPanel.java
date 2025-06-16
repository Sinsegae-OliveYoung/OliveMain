package com.olive.stock.history.view;

import java.awt.Dimension;

import com.olive.common.config.Config;
import com.olive.stock.StockPage;
import com.olive.stock.StockPanel;
import com.olive.store.StorePage;

public class StockOBPanel extends StockPanel{
	
	public StockOBPanel(StockPage stockPage) {
		super(stockPage);
		
		setPreferredSize(new Dimension(Config.CONTENT_W, Config.CONTENT_H));
		setBackground(Config.WHITE);
	}
}
