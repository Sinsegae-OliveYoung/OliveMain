package com.olive.stock.list.view;

import java.awt.Dimension;

import com.olive.common.config.Config;
import com.olive.stock.StockPage;
import com.olive.stock.StockPanel;
import com.olive.store.StorePage;

public class StockCatPanel extends StockPanel{
	
	public StockCatPanel(StockPage stockPage) {
		super(stockPage);
		
		setPreferredSize(new Dimension(Config.CONTENT_W, Config.CONTENT_H));
		setBackground(Config.LIGHT_GREEN);
	}
}
