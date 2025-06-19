package com.olive.bound.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.olive.common.model.BoundProduct;
import com.olive.common.model.Stock;

public class BoundShowModel extends AbstractTableModel {
    private List<BoundProduct> boundProductList = new ArrayList<>();
    private String[] column = { "상품명", "상품코드", "요청수량" };
    
    @Override
    public int getRowCount() {
        return boundProductList.size();
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
    public Object getValueAt(int row, int col) {
    	BoundProduct boundProduct = boundProductList.get(row);
        switch (col) {
            case 0:
                return boundProduct.getProductOption().getProduct().getProduct_name();
            case 1:
                return boundProduct.getProductOption().getOption_code();
            case 2:
                return boundProduct.getB_count();
            default:
                return null;
        }
    }
    
    public void setBoundProductList(List<BoundProduct> boundProductList) {
        this.boundProductList = boundProductList != null ? boundProductList : new ArrayList<>();
        fireTableDataChanged();
    }
}
