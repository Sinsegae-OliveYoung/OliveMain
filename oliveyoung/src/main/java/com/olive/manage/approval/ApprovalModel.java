package com.olive.manage.approval;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.olive.common.model.Bound;
import com.olive.common.model.Member;
import com.olive.common.repository.BoundDAO;

public class ApprovalModel extends AbstractTableModel{
	
	BoundDAO boundDAO = new BoundDAO();
	
	List<Bound> list;
	String[] column = { "번호", "지점", "입출고", "요청자", "요청일", "상태" };
	
	public ApprovalModel(BoundFilterDTO filter) {
		list = boundDAO.select(filter);
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
		Bound bo= list.get(row);
		
		switch(col) {
			case 0: return bo.getBound_id();
			case 1: return bo.getBranch().getBr_name();
			case 2: // "in" → "입고", "out" → "출고"
				String flag = bo.getBound_flag();
				if ("in".equalsIgnoreCase(flag)) {
					return "입고";
				} else if ("out".equalsIgnoreCase(flag)) {
					return "출고";
				} else {
					return flag; // 혹시 모를 예외값 처리
				}
			case 3: return bo.getUser().getUser_name();
			case 4: return bo.getRequest_date();
			case 5: return bo.getBoundState().getBo_state_name();
			default: return null;
		}
	}

		
}
