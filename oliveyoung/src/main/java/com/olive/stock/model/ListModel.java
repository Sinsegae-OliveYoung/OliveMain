package com.olive.stock.model;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.olive.common.model.Category;
import com.olive.common.model.Stock;
import com.olive.common.repository.StockDAO;


public class ListModel extends AbstractTableModel {

    StockDAO stockDAO;
    List<Stock> list;

    String[] column = {
        "상품옵션코드", "카테고리명", "상세카테고리명",
        "상품명", "브랜드", "가격",
        "재고수량", "입고일"
    };
//    String[] column = {
//    		"option_code", "ct_code", "cd_dt_code",
//    		"product_name", "brand", "price",
//    		"st_quantity", "st_update"
//    };

    public ListModel(String str) {
        stockDAO = new StockDAO();
        if(str.equals("now")) {
        	list = stockDAO.listNow();             	
        } else if(str.equals("countAlert")) {
        	list = stockDAO.listCountAlert();
        } else if(str.equals("oldAlert")) {
        	list = stockDAO.listOldAlert();
        }
    }
    
    public ListModel(Category category) {
    	stockDAO = new StockDAO();
    	list = stockDAO.listCat(category);
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
                             .getCt_name();
                break;
            case 2:
                value = stock.getProductOption()
                             .getProduct()
                             .getCategory_detail()
                             .getCt_dt_name();
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
    
    public void sortByDateAsc() {
        list.sort(Comparator.comparing(Stock::getSt_update));
        fireTableDataChanged();
    }

    public void sortByDateDesc() {
        list.sort(Comparator.comparing(Stock::getSt_update).reversed());
        fireTableDataChanged();
    }

    public void sortByQuantityDesc() {
        list.sort(Comparator.comparingInt(Stock::getSt_quantity).reversed());
        fireTableDataChanged();
    }

    public void sortByNameAsc() {
        list.sort(Comparator.comparing(
            s -> s.getProductOption().getProduct().getProduct_name(), String.CASE_INSENSITIVE_ORDER));
        fireTableDataChanged();
    }

    public void sortByNameDesc() {
        list.sort(Comparator.comparing(
            s -> s.getProductOption().getProduct().getProduct_name(), String.CASE_INSENSITIVE_ORDER.reversed()));
        fireTableDataChanged();
    }


}
