package com.olive.stock.model;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.olive.common.model.Category;
import com.olive.common.model.Stock;
import com.olive.common.model.StockHistory;
import com.olive.common.model.User;
import com.olive.common.repository.StockDAO;
import com.olive.common.repository.StockLogDAO;

public class StockModel extends AbstractTableModel{
    StockLogDAO stockLogDAO;
    List<StockHistory> list;
    String status = null;
    User user = null;
    
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    
    String[] column = {
    	    "옵션 코드",       // po.option_code
    	    "카테고리명",   // ct.ct_name
    	    "상세카테고리명", // cd.ct_dt_name
    	    "상품명",         // p.product_name
    	    "브랜드명",       // b.bd_name
    	    "옵션명",
    	    "가격",           // po.price
    	    "수량",           // bp.b_count
    	    "입/출고 날짜",         // bd.request_date
    	    "승인자",      // u.approval_id
    	    "승인일"          // bd.approve_date
    	};

    public StockModel(String str, User user) {
    	this.user = user;
    	stockLogDAO = new StockLogDAO();
    	status = str;
    	list = stockLogDAO.listBound(str, user);  	
    }
    public StockModel(String str, String start, String end, User user) {
    	this.user = user;
    	stockLogDAO = new StockLogDAO();
    	status = str;
    	if ("in".equals(str))
    		column[8] = "입고 날짜";
    	else if ("out".equals(str))
    		column[8] = "출고 날짜";
    	list = stockLogDAO.listBoundDate(str, start, end, user);  	
    }
    
    public void reload() {
    	stockLogDAO = new StockLogDAO();
     	list = stockLogDAO.listBound(status, user);  	
     	fireTableDataChanged();
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
                             .getCt_name();
                break;
            case 2:
                value = his.getProductOption()
                             .getProduct()
                             .getCategory_detail()
                             .getCt_dt_name();
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
            	value = his.getProductOption().getOption_name();
            	break;
            case 6:
                value = Integer.toString(his.getProductOption().getPrice());
                break;
            case 7:
            	value = Integer.toString(his.getQuantity());
                break;
            case 8:
            	if (his.getRequestDate() != null) {
                    
                    return sdf.format(his.getRequestDate());
                } else {
                    return ""; // 또는 "요청일 없음"
                }
            case 9:
            	value = his.getManager().getUser_name();
            	break;
            case 10:
            	if (his.getApprovalDate() != null) {
                    return sdf.format(his.getApprovalDate());
                } else {
                    return ""; // 또는 "결재일 없음"
                }
            default:
                return null;
        }

        return value;
    }
    
    public void sortByDateAsc() {
        list.sort(Comparator.comparing(StockHistory::getRequestDate));
        fireTableDataChanged();
    }

    public void sortByDateDesc() {
        list.sort(Comparator.comparing(StockHistory::getRequestDate).reversed());
        fireTableDataChanged();
    }
}


