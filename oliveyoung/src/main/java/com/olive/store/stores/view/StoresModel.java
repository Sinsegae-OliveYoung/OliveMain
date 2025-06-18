package com.olive.store.stores.view;

import java.util.Date;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.olive.common.model.Branch;
import com.olive.common.model.Stock;
import com.olive.common.repository.BranchDAO;

public class StoresModel extends AbstractTableModel {

	BranchDAO branchDAO;
	List<Stock> list;
	
	String[] column = { "브랜드", "상위 카테고리", "하위 카테고리", "상품명", "재고", "최근 수정일"};
	
	public StoresModel(String br_name) {
		branchDAO = new BranchDAO();
		list = branchDAO.selectBranchStock(br_name);
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
		case 0: value = stock.getProductOption().getProduct().getBrand().getBd_name(); break;
		case 1: value = stock.getProductOption().getProduct().getCategory_detail().getCategory().getCt_name(); break;
		case 2: value = stock.getProductOption().getProduct().getCategory_detail().getCt_dt_name(); break;
		case 3: value = stock.getProductOption().getProduct().getProduct_name(); break;
		case 4: value = Integer.toString(stock.getSt_quantity()); break;
		case 5: value = stock.getSt_update().toString(); break;
		}
		System.out.println(value);
		return value;
	}
	
	
}
