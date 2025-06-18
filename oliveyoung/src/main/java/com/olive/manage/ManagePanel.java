package com.olive.manage;

import java.awt.Dimension;

import javax.swing.JPanel;

import com.olive.common.config.Config;
import com.olive.mainlayout.MainLayout;

public class ManagePanel extends JPanel{

	MainLayout mainLayout;
	
	public ManagePanel(MainLayout mainLayout) {
		this.mainLayout = mainLayout;
		
		setPreferredSize(new Dimension(Config.CONTENT_W, Config.CONTENT_H));
		setBackground(Config.LIGHT_GREEN);
		setVisible(false);
	}
}
