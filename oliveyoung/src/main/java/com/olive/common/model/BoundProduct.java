package com.olive.common.model;

public class BoundProduct {

	private int b_pd_id;
	private int b_count;
	private ProductOption productOption;
	private Bound bound;
	
	public int getB_pd_id() {
		return b_pd_id;
	}
	public void setB_pd_id(int b_pd_id) {
		this.b_pd_id = b_pd_id;
	}
	public int getB_count() {
		return b_count;
	}
	public void setB_count(int b_count) {
		this.b_count = b_count;
	}
	public ProductOption getProductOption() {
		return productOption;
	}
	public void setProductOption(ProductOption productOption) {
		this.productOption = productOption;
	}
	public Bound getBound() {
		return bound;
	}
	public void setBound(Bound bound) {
		this.bound = bound;
	}
	
}
