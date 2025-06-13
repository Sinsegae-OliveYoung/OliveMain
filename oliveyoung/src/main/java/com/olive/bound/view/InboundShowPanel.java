package com.olive.bound.view;

import java.awt.Color;
import java.awt.Dimension;

import com.olive.bound.BoundPage;
import com.olive.common.config.Config;
import com.olive.common.view.Panel;

public class InboundShowPanel extends Panel{
	
	public InboundShowPanel(BoundPage boundPage) {
		super(boundPage);
		
		setPreferredSize(new Dimension(Config.CONTENT_W, Config.CONTENT_H));
		setBackground(Color.RED);
	}
}