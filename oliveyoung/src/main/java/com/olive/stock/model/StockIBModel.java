package com.olive.stock.model;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.olive.common.model.Category;
import com.olive.common.model.Stock;
import com.olive.common.repository.StockDAO;

public class StockIBModel extends AbstractTableModel{
    StockDAO stockDAO;
    List<Stock> list;

    String[] column = {
        "상품옵션코드", "카테고리코드", "상세카테고리코드",
        "상품명", "브랜드", "가격",
        "입고수량", "입고일", "관리자", "결재승인날짜"
    };
    public StockIBModel() {
    	stockDAO = new StockDAO();
//    	list = stockDAO.listCat();
    }

    public int getRowCount() {
        return list.size();
    }

    public int getColumnCount() {
        return column.length;
    }

    public String getColumnName(int col) {
        return column[col];
    }

    public Object getValueAt(int row, int col) {
        Stock stock = list.get(row);

        String value = null;

        switch (col) {
            case 0:
                value = stock.getProductOption().getOption_code();
                break;
            case 1:
                value = stock.getProductOption()
                             .getProduct()
                             .getCategory_detail()
                             .getCategory()
                             .getCt_code();
                break;
            case 2:
                value = stock.getProductOption()
                             .getProduct()
                             .getCategory_detail()
                             .getCt_dt_code();
                break;
            case 3:
                value = stock.getProductOption()
                             .getProduct()
                             .getProduct_name();
                break;
            case 4:
                value = stock.getProductOption()
                             .getProduct()
                             .getBrand()
                             .getBd_name(); 
                break;
            case 5:
                value = Integer.toString(stock.getProductOption().getPrice());
                break;
            case 6:
                value = Integer.toString(stock.getSt_quantity());
                break;
            case 7:
            	 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        	    value = sdf.format(stock.getSt_update());
                break;
        }

        return value;
    }
}
