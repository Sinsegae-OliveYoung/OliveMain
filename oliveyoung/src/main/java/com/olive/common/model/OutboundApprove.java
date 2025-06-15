package com.olive.common.model;

import java.sql.Date;

import com.olive.common.model.ApproveState;
import com.olive.common.model.Outbound;
import com.olive.common.model.User;

public class OutboundApprove {
	
	private int oa_id;
	private Outbound outbound;
	private User user;
	private ApproveState approveState;
	private Date oa_date;
	
	public int getOa_id() {
		return oa_id;
	}
	public void setOa_id(int oa_id) {
		this.oa_id = oa_id;
	}
	public Outbound getOutbound() {
		return outbound;
	}
	public void setOutbound(Outbound outbound) {
		this.outbound = outbound;
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
	public Date getOa_date() {
		return oa_date;
	}
	public void setOa_date(Date oa_date) {
		this.oa_date = oa_date;
	}
	
}
