package com.olive.bound.view;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.olive.common.model.Bound;
import com.olive.common.model.BoundProduct;
import com.olive.common.repository.InboundDAO;

public class InboundListDetailModel extends AbstractTableModel{
	InboundDAO inboundDAO;
	List<BoundProduct> list;
	
	String[] column = {"날짜", "작성자", "입고상태"};
	
	// 첫 화면 조회 -> 추후 로그인한 계정에 따른 지점 선택 추가
	public InboundListDetailModel (String str) {
		inboundDAO = new InboundDAO();
		
		if(str.equals("now")) {
        	list = inboundDAO.selectInbound();             	
        } 
	}
	
	// 지점 변경에 따른 테이블 조회 변화
	public InboundListDetailModel(BoundProduct boundBroduct) {
		inboundDAO = new InboundDAO();
    	list = inboundDAO.selectInbound(boundBroduct);
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
		BoundProduct boundProduct = list.get(row);
		
        String value = null;

        switch (col) {      
            case 0: 
            	value = boundProduct.getBound().getRequest_date().toString();
                break;
            case 1: 
            	value = boundProduct.getBound().getUser().getUser_name();
                break;
            case 2: 
            	value = boundProduct.getBound().getBoundState().getBo_state_name();
                break;
            default: return "";
        }

        return value;
    }
}
