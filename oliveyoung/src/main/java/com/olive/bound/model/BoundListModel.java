package com.olive.bound.model;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.olive.common.model.BoundProduct;
import com.olive.common.model.Branch;
import com.olive.common.repository.BoundDAO;

public class BoundListModel extends AbstractTableModel{
	BoundDAO boundDAO;
	List<BoundProduct> list;
	
	String[] column = {"날짜", "지점명", "작성자", "입고상태"};
	
	public BoundListModel(List<Branch> branchList, String flag) {
        
        if(flag == "in") {
        	boundDAO = new BoundDAO();
        	list = boundDAO.selectInboundByBranches(branchList);
        }
        else if(flag == "out") {
        	boundDAO = new BoundDAO();
        	list = boundDAO.selectOutboundByBranches(branchList);
        }
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
	
	public BoundProduct getStock(int row) {
	    return list.get(row);
	}

	@Override
	public Object getValueAt(int row, int col) {
		BoundProduct boundproduct = list.get(row);
		
		System.out.println(list.get(row));
		
        String value = null;

        switch (col) {      
            case 0: 
            	value = boundproduct.getBound().getRequest_date().toString();
                break;
            case 1: 
            	value = boundproduct.getBound().getBranch().getBr_name();
                break;
            case 2: 
            	value = boundproduct.getBound().getUser().getUser_name();
                break;
            case 3: 
            	value = boundproduct.getBound().getBoundState().getBo_state_name();
                break;
            default: return "";
        }

        return value;
    }
	
	
	
	
	public BoundProduct getBoundAt(int row) {
	    return list.get(row);  // list는 Bound 객체 리스트
	}
}
