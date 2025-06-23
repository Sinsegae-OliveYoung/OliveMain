package com.olive.manage.approval;

import javax.swing.JPanel;

import com.olive.mainlayout.MainLayout;
import com.olive.manage.BasePanel;
import com.olive.manage.ManagePage;

public class ApprovalDetailPanel extends BasePanel{
	
	public ApprovalDetailPanel(MainLayout mainLayout, String title, ManagePage managePage) {
		super(mainLayout, title, managePage);
		
		
	}

	@Override
	public JPanel createContent() {
		return new JPanel();
	}

}
