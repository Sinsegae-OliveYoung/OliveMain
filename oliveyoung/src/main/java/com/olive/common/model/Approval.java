package com.olive.common.model;

import java.sql.Date;

public class Approval {
	
	private int approval_id;

	private Inbound inbound;
	private User user;
	private Date approval_date;
	
	public int getApproval_id() {
		return approval_id;
	}
	public void setApproval_id(int approval_id) {
		this.approval_id = approval_id;
	}

	public Inbound getInbound() {
		return inbound;
	}
	public void setInbound(Inbound inbound) {
		this.inbound = inbound;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Date getApproval_date() {
		return approval_date;
	}
	public void setApproval_date(Date approval_date) {
		this.approval_date = approval_date;
	}
}