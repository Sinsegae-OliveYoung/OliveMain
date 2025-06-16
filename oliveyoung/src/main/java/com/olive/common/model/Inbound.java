package com.olive.common.model;

import java.sql.Date;

public class Inbound {
	
	private int ib_id;
//	private User user;
	private Date ib_date;
	private String comment;
	private BoundState boundState;
	
	public int getIb_id() {
		return ib_id;
	}
	public void setIb_id(int ib_id) {
		this.ib_id = ib_id;
	}
//	public User getUser() {
//		return user;
//	}
//	public void setUser(User user) {
//		this.user = user;
//	}
	public Date getIb_date() {
		return ib_date;
	}
	public void setIb_date(Date ib_date) {
		this.ib_date = ib_date;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public BoundState getBoundState() {
		return boundState;
	}
	public void setBoundState(BoundState boundState) {
		this.boundState = boundState;
	}
	
}
