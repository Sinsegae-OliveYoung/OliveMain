package com.olive.common.model;

import java.sql.Date;

public class Approval {
	
	private int approval_id;
	//private Ib ib;
//	private User user;
	private Date approval_date;
	
	public int getApproval_id() {
		return approval_id;
	}
	public void setApproval_id(int approval_id) {
		this.approval_id = approval_id;
	}
//	public Ib getIb() {
//		return ib;
//	}
//	public void setIb(Ib ib) {
//		this.ib = ib;
//	}
//	public User getUser() {
//		return user;
//	} 
//	public void setUser(User user) {
//		this.user = user;
//	}
	public Date getApproval_date() {
		return approval_date;
	}
	public void setApproval_date(Date approval_date) {
		this.approval_date = approval_date;
	}
}
