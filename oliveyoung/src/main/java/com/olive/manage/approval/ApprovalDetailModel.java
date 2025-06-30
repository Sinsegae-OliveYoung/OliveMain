package com.olive.manage.approval;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.olive.common.model.BoundProduct;
import com.olive.common.repository.BoundProductDAO;

public class ApprovalDetailModel extends AbstractTableModel{
	
	BoundProductDAO boundProductDAO = new BoundProductDAO();
	
	List<BoundProduct> list;
	String[] column = { "번호", "제품명", "옵션", "제품가격", "수량", "총액" };
	
	public ApprovalDetailModel(int bound_id) {
		list = boundProductDAO.select(bound_id); // 선택한 요청서의 bound id
	}

	@Override
	public int getRowCount() {
		return list.size();
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
		BoundProduct bp = list.get(row);
		
		switch(col) {
			case 0: return row + 1;
			case 1: return bp.getProductOption().getProduct().getProduct_name();
			case 2: return bp.getProductOption().getOption_name();
			case 3: return bp.getProductOption().getPrice();
			case 4: return bp.getB_count();
			case 5: return (bp.getB_count() * bp.getProductOption().getPrice());
			default: return null;
		}
	}
	
}
