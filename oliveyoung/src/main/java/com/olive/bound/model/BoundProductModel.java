package com.olive.bound.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.olive.common.model.BoundProduct;

public class BoundProductModel extends AbstractTableModel {

    private List<RequestItem> requestList = new ArrayList<>();
    private String[] column = {"제품명", "제품코드", "요청수량"};

    private static class RequestItem {
        BoundProduct boundProduct;
        int quantity;

        RequestItem(BoundProduct bp, int quantity) {
            this.boundProduct = bp;
            this.quantity = quantity;
        }
    }

    public void addProduct(BoundProduct bp) {
        for (RequestItem item : requestList) {
            if (item.boundProduct.getProductOption().getOption_code().equals(bp.getProductOption().getOption_code())) {
                item.quantity++;
                fireTableDataChanged();
                return;
            }
        }
        requestList.add(new RequestItem(bp, 1));
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return requestList.size();
    }

    @Override
    public int getColumnCount() {
        return column.length;
    }

    @Override
    public String getColumnName(int col) {
        return column[col];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        RequestItem item = requestList.get(rowIndex);
        switch (columnIndex) {
            case 0: return item.boundProduct.getProductOption().getProduct().getProduct_name();
            case 1: return item.boundProduct.getProductOption().getOption_code();
            case 2: return item.quantity;
            default: return "";
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 2;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex == 2) {
            try {
                int newQuantity = Integer.parseInt(aValue.toString());
                if (newQuantity >= 0) {
                    requestList.get(rowIndex).quantity = newQuantity;
                    fireTableCellUpdated(rowIndex, columnIndex);
                }
            } catch (NumberFormatException ignored) {}
        }
    }

    public List<BoundProduct> getProductList() {
        List<BoundProduct> result = new ArrayList<>();
        for (RequestItem item : requestList) {
            BoundProduct bp = new BoundProduct();
            bp.setProductOption(item.boundProduct.getProductOption());
            bp.setB_count(item.quantity);
            result.add(bp);
        }
        return result;
    }

    public void clear() {
        requestList.clear();
        fireTableDataChanged();
    }
}
