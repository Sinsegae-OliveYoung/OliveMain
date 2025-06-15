package com.olive.store.storeconfig.view;

import java.util.Date;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.olive.common.model.Branch;
import com.olive.common.model.Stock;
import com.olive.common.repository.BranchDAO;

public class StoreConfigModel extends AbstractTableModel {

	BranchDAO branchDAO;
	List<Branch> list;
	
	String[] column = { "등록 번호", "지점명", "담당자", "주소", "연락처" };
	
	public StoreConfigModel() {
		branchDAO = new BranchDAO();
		list = branchDAO.selectBranch();
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
	
	public Branch getBranchAt(int row) {
	    return list.get(row);
	}
	
	public Object getValueAt(int row, int col) {
		Branch branch = list.get(row);
		String value = null;
		
		switch (col) {
		case 0: value = Integer.toString(branch.getBr_id()); break;
		case 1: value = branch.getBr_name(); break;
		case 2: value = branch.getUser().getUser_name(); break;
		case 3: value = branch.getBr_address(); break;
		case 4: value = branch.getBr_tel(); break;
		}
		return value;
	}
	
	
}
