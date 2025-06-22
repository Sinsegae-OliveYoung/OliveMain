package com.olive.manage.approval;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.olive.common.model.Bound;
import com.olive.common.model.Member;
import com.olive.common.repository.BoundDAO;

public class ApprovalModel extends AbstractTableModel{
	
	BoundDAO boundDAO = new BoundDAO();
	
	List<Bound> list;
	String[] column = { "번호", "요청자", "요청일", "상태" };
	
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
			case 1: return bo.getUser().getUser_name();
			case 2: return bo.getRequest_date();
			case 3: return bo.getBoundState().getBo_state_name();
			default: return null;
		}
	}

		
}
