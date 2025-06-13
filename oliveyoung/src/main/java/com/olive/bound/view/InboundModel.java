package com.olive.bound.view;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.olive.common.model.Product;
import com.olive.common.repository.ProductDAO;

public class InboundModel extends AbstractTableModel{
	
	ProductDAO productDAO;
	List<Product> list;
	
	String[] column = {"product_name"	};
	
	public InboundModel () {
		productDAO = new ProductDAO();
		list = productDAO.selectAll();
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return column.length;
	}
	
	public String getColumnName(int col) {
		return column[col];
	}

	@Override
	public Object getValueAt(int row, int col) {
		Product product = list.get(row);
		
		String value = null;
		value = product.getProduct_name();
		
		return value;
	}

}
