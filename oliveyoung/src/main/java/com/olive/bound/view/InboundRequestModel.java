package com.olive.bound.view;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import com.olive.common.model.Stock;

public class InboundRequestModel extends AbstractTableModel {

    private List<RequestItem> requestList = new ArrayList<>();
    private String[] column = {"제품명", "제품코드", "요청수량"};

    // 내부 클래스: Stock + 요청수량 관리
    private static class RequestItem {
        Stock stock;
        int quantity;

        RequestItem(Stock stock, int quantity) {
            this.stock = stock;
            this.quantity = quantity;
        }
    }

    public void addStock(Stock stock) {
        for (RequestItem item : requestList) {
            if (item.stock.getProductOption().getOption_code().equals(stock.getProductOption().getOption_code())) {
                item.quantity++;  // 이미 있는 경우 수량 증가
                fireTableDataChanged();
                return;
            }
        }
        // 신규 추가
        requestList.add(new RequestItem(stock, 1));
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
            case 0: return item.stock.getProductOption().getProduct().getProduct_name();
            case 1: return item.stock.getProductOption().getOption_code();
            case 2: return item.quantity;
            default: return "";
        }
    }

    // 셀 수정 가능 여부
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 2;  // 요청수량 컬럼만 편집 가능
    }

    // 셀 수정 시 데이터 반영
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex == 2) {
            try {
                int newQuantity = Integer.parseInt(aValue.toString());
                if (newQuantity >= 0) {
                    requestList.get(rowIndex).quantity = newQuantity;
                    fireTableCellUpdated(rowIndex, columnIndex);
                }
            } catch (NumberFormatException e) {
                // 입력값이 숫자가 아니면 무시
            }
        }
    }
}
