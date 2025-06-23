package com.olive.common.view;

import java.awt.Dimension;

import javax.swing.JPanel;

import com.olive.common.config.Config;
import com.olive.mainlayout.MainLayout;

public class Panel extends JPanel{
	protected MainLayout mainLayout;

	public Panel(MainLayout mainLayout) {
		this.mainLayout = mainLayout;
		setPreferredSize(new Dimension(Config.CONTENT_W, Config.CONTENT_H));
		setVisible(false);
	}
	
	public void refresh() {
        // 재고 업데이트시 재고 모든 패널에서 overide하여 데이터 갱신
    }
}
