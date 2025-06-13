package com.olive.common.view;

import java.awt.Dimension;

import javax.swing.JPanel;

import com.olive.common.config.Config;
import com.olive.mainlayout.MainLayout;

/*-------------------------------------------
 * MainLayout에 붙여질 페이지의 최상위 객체
 * ------------------------------------------- */

public class Page extends JPanel {
	protected MainLayout mainLayout;
	
	public Page(MainLayout mainLayout) {
		this.mainLayout = mainLayout;
		setPreferredSize(new Dimension(Config.LAYOUT_W, Config.CONTENT_H));
		setVisible(false);
	}
}
