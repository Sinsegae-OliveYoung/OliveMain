package com.olive.common.model;

import java.util.Date;

public class StockHistory {
    private ProductOption productOption;
    private int quantity; // 입고수량 또는 출고수량
    private Date requestDate; // 입고요청일 또는 출고요청일
    private Date approvalDate; // 결재 승인 날짜
    private User manager; // 승인자

    public ProductOption getProductOption() {
        return productOption;
    }

    public void setProductOption(ProductOption productOption) {
        this.productOption = productOption;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public Date getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(Date approvalDate) {
        this.approvalDate = approvalDate;
    }

    public User getManager() {
        return manager;
    }

    public void setManager(User manager) {
        this.manager = manager;
    }
}
