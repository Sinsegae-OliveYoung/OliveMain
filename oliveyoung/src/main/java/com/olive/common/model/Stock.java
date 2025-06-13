package com.olive.common.model;

import java.sql.Date;

public class Stock {
	
	private int st_id;
	private int st_quantity;
	private Date st_update;
	private ProductOption productOption;
	private Branch branch;
	
	public int getSt_id() {
		return st_id;
	}
	public void setSt_id(int st_id) {
		this.st_id = st_id;
	}
	public int getSt_quantity() {
		return st_quantity;
	}
	public void setSt_quantity(int st_quantity) {
		this.st_quantity = st_quantity;
	}
	public Date getSt_update() {
		return st_update;
	}
	public void setSt_update(Date st_update) {
		this.st_update = st_update;
	}
	public ProductOption getProductOption() {
		return productOption;
	}
	public void setProductOption(ProductOption productOption) {
		this.productOption = productOption;
	}
	public Branch getBranch() {
		return branch;
	}
	public void setBranch(Branch branch) {
		this.branch = branch;
	}
	
	
}
