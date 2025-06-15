package com.olive.bound.view;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.olive.common.model.Product;
import com.olive.common.repository.ProductDAO;

public class InboundModel extends AbstractTableModel{
	
	ProductDAO productDAO;
	List<Product> list;
	
	String[] column = {"카테고리", "상세카테고리", "브랜드", "제품명", "제품코드", "호수",  "가격", "재고수량"	};
	
	
	public InboundModel () {
		productDAO = new ProductDAO();
		list = productDAO.selectAll();
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

	@Override
	public Object getValueAt(int row, int col) {
		Product product = list.get(row);
		
		String value = null;
		switch (col) {
			case 0 : value = product.getProduct_name(); break; //value = product.getCategory().getCt_name(); break;
			case 1 : value = product.getProduct_name(); break;
			case 2 : value = product.getBrand().getBd_name(); break;
			case 3 : value = product.getProduct_name(); break;
			case 4 : value = product.getProduct_name(); break;
			case 5 : value = product.getProduct_name(); break;
			case 6 : value = product.getProduct_name(); break;
			case 7 : value = product.getProduct_name(); break;
		
		}
		
		return value;
	}

}
