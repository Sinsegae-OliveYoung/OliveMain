package com.olive.bound.view;

import java.util.Date;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.olive.common.model.Inbound;
import com.olive.common.model.Stock;
import com.olive.common.repository.InboundDAO;

public class InboundListModel extends AbstractTableModel{
	InboundDAO inboundDAO;
	List<Inbound> list;
	
	String[] column = {"날짜", "작성자", "입고상태"};
	
	public InboundListModel (String str) {
		inboundDAO = new InboundDAO();
		// list = productDAO.selectAll();
		
		if(str.equals("now")) {
//        	list = inboundDAO.selectInbound();             	
        } 
//		else if(str.equals("newBranch")) {
//        	list = productDAO.listNewBranch();
//        }
	}
	
	public InboundListModel(Inbound inbound) {
		inboundDAO = new InboundDAO();
    	list = inboundDAO.selectInbound(inbound);
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
	
	public Inbound getStock(int row) {
	    return list.get(row);
	}

	@Override
	public Object getValueAt(int row, int col) {
		Inbound inbound = list.get(row);
		
        String value = null;

        switch (col) {      
            case 0: 
                value = Integer.toString(inbound.getIb_id());
                break;
            case 1: 
            	value = inbound.getIb_date().toString();
                break;
            case 2: 
            	value = inbound.getBoundState().getBo_state_name();
                break;

        }

        return value;
    }
}
