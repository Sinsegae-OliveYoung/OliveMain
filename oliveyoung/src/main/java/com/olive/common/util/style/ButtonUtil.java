package com.olive.common.util.style;

import javax.swing.JButton;

import com.olive.common.config.Config;

public class ButtonUtil {
	
	public static void applyDefaultStyle(JButton bt){
		bt.setBackground(Config.LIGHT_GRAY);
	}
}
