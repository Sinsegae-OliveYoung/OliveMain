package com.olive.stock.model;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.olive.common.model.Category;
import com.olive.common.model.Stock;
import com.olive.common.model.StockHistory;
import com.olive.common.repository.StockDAO;
import com.olive.common.repository.StockLogDAO;

public class StockIBModel extends AbstractTableModel{
    StockLogDAO stockLogDAO;
    List<StockHistory> list;

    String[] column = {
        "상품옵션코드", "카테고리코드", "상세카테고리코드",
        "상품명", "브랜드", "가격",
        "입고수량", "입고일", "관리자", "결재승인날짜"
    };
    public StockIBModel() {
    	stockLogDAO = new StockLogDAO();
    	list = stockLogDAO.listIB();
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
        StockHistory his = list.get(row);

        String value = null;

        switch (col) {
            case 0:
                value = his.getProductOption().getOption_code();
                break;
            case 1:
                value = his.getProductOption()
                             .getProduct()
                             .getCategory_detail()
                             .getCategory()
                             .getCt_code();
                break;
            case 2:
                value = his.getProductOption()
                             .getProduct()
                             .getCategory_detail()
                             .getCt_dt_code();
                break;
            case 3:
                value = his.getProductOption()
                             .getProduct()
                             .getProduct_name();
                break;
            case 4:
                value = his.getProductOption()
                             .getProduct()
                             .getBrand()
                             .getBd_name(); 
                break;
            case 5:
                value = Integer.toString(his.getProductOption().getPrice());
                break;
            case 6:
                value = Integer.toString(his.getQuantity());
                break;
            case 7:
            	SimpleDateFormat sdf_req = new SimpleDateFormat("yyyy-MM-dd");
        	    value = sdf_req.format(his.getRequestDate());
                break;
            case 8:
            	value = his.getManager().getUser_name();
            	break;
            case 9:
            	SimpleDateFormat sdf_app = new SimpleDateFormat("yyyy-MM-dd");
            	value = sdf_app.format(his.getApprovalDate());
            	break;
        }

        return value;
    }
}
