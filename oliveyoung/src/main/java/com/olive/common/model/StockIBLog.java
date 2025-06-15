package com.olive.common.model;

import java.sql.Date;

public class StockIBLog {

    private String optionCode;
    private String categoryCode;
    private String categoryDetailCode;
    private String productName;
    private String brandName;
    private int price;
    private int quantity;
    private Date ibDate;
    private String adminName;
    private Date approvalDate;
    
	public String getOptionCode() {
		return optionCode;
	}
	public void setOptionCode(String optionCode) {
		this.optionCode = optionCode;
	}
	public String getCategoryCode() {
		return categoryCode;
	}
	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
	public String getCategoryDetailCode() {
		return categoryDetailCode;
	}
	public void setCategoryDetailCode(String categoryDetailCode) {
		this.categoryDetailCode = categoryDetailCode;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public Date getIbDate() {
		return ibDate;
	}
	public void setIbDate(Date ibDate) {
		this.ibDate = ibDate;
	}
	public String getAdminName() {
		return adminName;
	}
	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}
	public Date getApprovalDate() {
		return approvalDate;
	}
	public void setApprovalDate(Date approvalDate) {
		this.approvalDate = approvalDate;
	}
    
    
}
