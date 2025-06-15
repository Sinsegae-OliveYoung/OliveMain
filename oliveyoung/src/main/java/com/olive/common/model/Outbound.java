package com.olive.common.model;

import java.sql.Date;

public class Outbound {
	
	private int ob_id;
	private User user;
	private Date ob_date;
	private String comment;
	private BoundState boundState;
	
	public int getOb_id() {
		return ob_id;
	}
	public void setOb_id(int ob_id) {
		this.ob_id = ob_id;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Date getOb_date() {
		return ob_date;
	}
	public void setOb_date(Date ob_date) {
		this.ob_date = ob_date;
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