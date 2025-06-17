package com.olive.common.model;

public class Branch {

	private int br_id;
	private String br_name;
	private String br_address;
	private String br_tel;
	private User user;
	
	public int getBr_id() {
		return br_id;
	}
	public void setBr_id(int br_id) {
		this.br_id = br_id;
	}
	public String getBr_name() {
		return br_name;
	}
	public void setBr_name(String br_name) {
		this.br_name = br_name;
	}
	public String getBr_address() {
		return br_address;
	}
	public void setBr_address(String br_address) {
		this.br_address = br_address;
	}
	public String getBr_tel() {
		return br_tel;
	}
	public void setBr_tel(String br_tel) {
		this.br_tel = br_tel;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	public String toString() {
		return this.br_name;
	}
}
