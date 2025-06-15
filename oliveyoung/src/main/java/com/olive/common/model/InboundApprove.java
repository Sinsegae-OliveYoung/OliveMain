package com.olive.common.model;

import java.sql.Date;

public class InboundApprove {
	
	private int ia_id;
	private Inbound inbound;
	private User user;
	private ApproveState approveState;
	private Date ia_date;
	
	public int getIa_id() {
		return ia_id;
	}
	public void setIa_id(int ia_id) {
		this.ia_id = ia_id;
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
	public ApproveState getApproveState() {
		return approveState;
	}
	public void setApproveState(ApproveState approveState) {
		this.approveState = approveState;
	}
	public Date getIa_date() {
		return ia_date;
	}
	public void setIa_date(Date ia_date) {
		this.ia_date = ia_date;
	}
	
}
