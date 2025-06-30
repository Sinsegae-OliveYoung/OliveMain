package com.olive.common.model;

import java.util.Date;

public class Bound {
	
	private int bound_id;
	private User user;
	private User approver;
	private Branch branch;
	private BoundState boundState;
	private Date request_date;
	private Date approve_date;
	private String comment;
	private String bound_flag;
	
	public int getBound_id() {
		return bound_id;
	}
	public void setBound_id(int bound_id) {
		this.bound_id = bound_id;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public User getApprover() {
		return approver;
	}
	public void setApprover(User approver) {
		this.approver = approver;
	}
	public Branch getBranch() {
		return branch;
	}
	public void setBranch(Branch branch) {
		this.branch = branch;
	}
	public BoundState getBoundState() {
		return boundState;
	}
	public void setBoundState(BoundState boundState) {
		this.boundState = boundState;
	}
	public Date getRequest_date() {
		return request_date;
	}
	public void setRequest_date(Date request_date) {
		this.request_date = request_date;
	}
	public Date getApprove_date() {
		return approve_date;
	}
	public void setApprove_date(Date approve_date) {
		this.approve_date = approve_date;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getBound_flag() {
		return bound_flag;
	}
	public void setBound_flag(String bound_flag) {
		this.bound_flag = bound_flag;
	} 
}
