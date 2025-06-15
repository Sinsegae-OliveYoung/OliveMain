package com.olive.common.model;

public class Category {

	private int ct_id;
	private String ct_code;
	private String ct_name;
	
	public int getCt_id() {
		return ct_id;
	}
	public void setCt_id(int ct_id) {
		this.ct_id = ct_id;
	}
	public String getCt_code() {
		return ct_code;
	}
	public void setCt_code(String ct_code) {
		this.ct_code = ct_code;
	}
	public String getCt_name() {
		return ct_name;
	}
	public void setCt_name(String ct_name) {
		this.ct_name = ct_name;
	}
	
	@Override
	public String toString() {
		return ct_code;
	}
}
