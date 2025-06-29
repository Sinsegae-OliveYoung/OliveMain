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

    // 새 상품 단독 추가만 수행
    public void addProduct(BoundProduct bp) {
        for (RequestItem item : requestList) {
            if (item.boundProduct.getProductOption().getOption_code()
                  .equals(bp.getProductOption().getOption_code())) {
                item.quantity++; // 수량만 증가
                fireTableDataChanged();
                return;
            }
        }
        // 처음 추가되는 경우
        requestList.add(new RequestItem(bp, bp.getB_count()));
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
	public Class<?> getColumnClass(int columnIndex) {
	    switch (columnIndex) {
	        case 2: return Integer.class; // 수량
	        default: return String.class;
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
                if (newQuantity > 0) {
                    requestList.get(rowIndex).quantity = newQuantity;
                    fireTableCellUpdated(rowIndex, columnIndex);
                } else {
                    requestList.remove(rowIndex);
                    fireTableRowsDeleted(rowIndex, rowIndex); // ✅ 이걸로만 호출해야 함
                }
            } catch (NumberFormatException ignored) {}
        }
    }

    // UI 업데이트 용도
    public void updateQuantityByOptionId(int optionId, int newQuantity) {
        for (RequestItem item : requestList) {
            if (item.boundProduct.getProductOption().getOption_id() == optionId) {
                item.quantity = newQuantity;
                fireTableDataChanged();
                break;
            }
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
    
    // 0 입력시 요청 컬럼 삭제
    public void removeRow(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < requestList.size()) {
            requestList.remove(rowIndex);
            fireTableDataChanged();
        }
    }

    public void clear() {
        requestList.clear();
        fireTableDataChanged();
    }
}
