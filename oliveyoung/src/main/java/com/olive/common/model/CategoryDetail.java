
package com.olive.common.model;

public class CategoryDetail {
	
	private int ct_dt_id;
	private String ct_dt_code;
	private String ct_dt_name;
	private Category category;
	
	public int getCt_dt_id() {
		return ct_dt_id;
	}
	public void setCt_dt_id(int ct_dt_id) {
		this.ct_dt_id = ct_dt_id;
	}
	public String getCt_dt_code() {
		return ct_dt_code;
	}
	public void setCt_dt_code(String ct_dt_code) {
		this.ct_dt_code = ct_dt_code;
	}
	public String getCt_dt_name() {
		return ct_dt_name;
	}
	public void setCt_dt_name(String ct_dt_name) {
		this.ct_dt_name = ct_dt_name;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	
	@Override
	public String toString() {
		return ct_dt_name;
	}
}
