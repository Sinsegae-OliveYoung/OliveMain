package com.olive.bound.view;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.olive.common.model.Branch;
import com.olive.common.model.Stock;
import com.olive.common.repository.ProductDAO;

public class InboundModel extends AbstractTableModel{
	
	ProductDAO productDAO;
	List<Stock> list;
	
	String[] column = {"카테고리", "상세카테고리", "브랜드", "제품명",  "호수",  "가격", "재고수량"	};
	
	public InboundModel (String str) {
		productDAO = new ProductDAO();
		// list = productDAO.selectAll();
		
		if(str.equals("now")) {
        	list = productDAO.selectNow();             	
        } 
//		else if(str.equals("newBranch")) {
//        	list = productDAO.listNewBranch();
//        }
	}
	
	public InboundModel(Branch branch) {
		productDAO = new ProductDAO();
    	list = productDAO.listNewBranch(branch);
    }

	@Override
	public int getRowCount() {
		return list.size();
	}

	@Override
	public int getColumnCount() {
		return column.length;
	}
	
	public String getColumnName(int col) {
		return column[col];
	}
	
	public Stock getStock(int row) {
	    return list.get(row);
	}

	@Override
	public Object getValueAt(int row, int col) {
		Stock stock = list.get(row);
		
        String value = null;

        switch (col) {      
            case 0: //카테고리
                value = stock.getProductOption().getProduct().getCategory().getCt_name();
                break;
            case 1: //상세카테고리
            	value = stock.getProductOption().getProduct().getCategory_detail().getCt_dt_name();
                break;
            case 2: //브랜드
            	value = stock.getProductOption().getProduct().getBrand().getBd_name();
                break;
            case 3: //제품명
            	value = stock.getProductOption().getProduct().getProduct_name();
                break;

            case 4: //호수
            	value = Integer.toString(stock.getProductOption().getOption_no());
                break;
            case 5: //가격
            	value = Integer.toString(stock.getProductOption().getPrice());
                break;
            case 6: //재고수량
            	value =  Integer.toString(stock.getSt_quantity());
                break;
        }

        return value;
    }

}
