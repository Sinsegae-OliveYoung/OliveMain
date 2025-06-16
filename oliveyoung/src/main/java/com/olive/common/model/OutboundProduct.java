package com.olive.common.model;

public class OutboundProduct {
	
	private int ob_pd_id;
	private Outbound outbound;
	private ProductOption productOption;
	private int ob_pd_count;
	
	public int getOb_pd_id() {
		return ob_pd_id;
	}
	public void setOb_pd_id(int ob_pd_id) {
		this.ob_pd_id = ob_pd_id;
	}
	public Outbound getOutbound() {
		return outbound;
	}
	public void setOutbound(Outbound outbound) {
		this.outbound = outbound;
	}
	public ProductOption getProductOption() {
		return productOption;
	}
	public void setProductOption(ProductOption productOption) {
		this.productOption = productOption;
	}
	public int getOb_pd_count() {
		return ob_pd_count;
	}
	public void setOb_pd_count(int ob_pd_count) {
		this.ob_pd_count = ob_pd_count;
	}
}
