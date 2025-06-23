package com.olive.manage.approval;

import java.sql.Date;

public class BoundFilterDTO {
	
	private int user_id;   //로그인한 사용자 id 
	private int br_id;
	private int boundstate_id;
	private String submitter_name = "이름";   
	private Date start_date;
	private Date end_date;
	
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public int getBr_id() {
		return br_id;
	}
	public void setBr_id(int br_id) {
		this.br_id = br_id;
	}
	public int getBoundstate_id() {
		return boundstate_id;
	}
	public void setBoundstate_id(int boundstate_id) {
		this.boundstate_id = boundstate_id;
	}
	public String getSubmitter_name() {
		return submitter_name;
	}
	public void setSubmitter_name(String submitter_name) {
		this.submitter_name = submitter_name;
	}
	public Date getStart_date() {
		return start_date;
	}
	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}
	public Date getEnd_date() {
		return end_date;
	}
	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}
	
}
