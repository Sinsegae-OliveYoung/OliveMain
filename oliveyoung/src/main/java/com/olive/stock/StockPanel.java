package com.olive.stock;

import java.awt.Dimension;

import javax.swing.JPanel;

import com.olive.common.config.Config;
import com.olive.store.StorePage;

public class StockPanel extends JPanel{
	protected StockPage stockPage;
	
	public StockPanel(StockPage stockPage) {
		this.stockPage = stockPage;
		setPreferredSize(new Dimension(Config.CONTENT_W, Config.CONTENT_H));
		setVisible(false);
	}
}
