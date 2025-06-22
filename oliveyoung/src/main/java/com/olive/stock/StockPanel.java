package com.olive.stock;

import java.awt.Dimension;

import javax.swing.JPanel;

import com.olive.common.config.Config;
import com.olive.common.model.User;
import com.olive.mainlayout.MainLayout;
import com.olive.store.StorePage;

public class StockPanel extends JPanel{
   protected MainLayout mainLayout;
	public StockPanel(MainLayout mainLayout) {
		this.mainLayout = mainLayout;
		setPreferredSize(new Dimension(Config.CONTENT_W, Config.CONTENT_H));
		setVisible(false);
	}
	
	 public void refresh() {
        // 업데이트시 모든 패널에서 overide하여 데이터 갱신
    }
}
