package com.olive.common.model;

public class Product {

	private int product_id;
	private String product_name;
	private Category category;
	private CategoryDetail category_detail;
	private Brand brand;
	
	public int getProduct_id() {
		return product_id;
	}
	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}
	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	public CategoryDetail getCategory_detail() {
		return category_detail;
	}
	public void setCategory_detail(CategoryDetail category_detail) {
		this.category_detail = category_detail;
	}
	public Brand getBrand() {
		return brand;
	}
	public void setBrand(Brand brand) {
		this.brand = brand;
	}
}