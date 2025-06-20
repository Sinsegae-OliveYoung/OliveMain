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
}
