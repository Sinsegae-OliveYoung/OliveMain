package com.olive.common.model;

public class InboundProduct {
	
	private int ib_pd_id;
	private Inbound inbound;
	private ProductOption productOption;
	private int ib_pd_count;
	
	public int getIb_pd_id() {
		return ib_pd_id;
	}
	public void setIb_pd_id(int ib_pd_id) {
		this.ib_pd_id = ib_pd_id;
	}
	public Inbound getInbound() {
		return inbound;
	}
	public void setInbound(Inbound inbound) {
		this.inbound = inbound;
	}
	public ProductOption getProductOption() {
		return productOption;
	}
	public void setProductOption(ProductOption productOption) {
		this.productOption = productOption;
	}
	public int getIb_pd_count() {
		return ib_pd_count;
	}
	public void setIb_pd_count(int ib_pd_count) {
		this.ib_pd_count = ib_pd_count;
	}

}
