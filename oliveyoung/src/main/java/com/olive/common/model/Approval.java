package com.olive.common.model;

import java.sql.Date;

public class Approval {
	
	private int approval_id;
<<<<<<< HEAD
	//private Ib ib;
//	private User user;
=======
	private Inbound inbound;
	private User user;
>>>>>>> develop
	private Date approval_date;
	
	public int getApproval_id() {
		return approval_id;
	}
	public void setApproval_id(int approval_id) {
		this.approval_id = approval_id;
	}
<<<<<<< HEAD
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
=======
	
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
>>>>>>> develop
	public Date getApproval_date() {
		return approval_date;
	}
	public void setApproval_date(Date approval_date) {
		this.approval_date = approval_date;
	}
}
