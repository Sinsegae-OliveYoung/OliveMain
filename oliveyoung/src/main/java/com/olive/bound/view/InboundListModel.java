package com.olive.bound.view;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.olive.common.model.Bound;
import com.olive.common.model.BoundProduct;
import com.olive.common.repository.InboundDAO;

public class InboundListModel extends AbstractTableModel{
	InboundDAO inboundDAO;
	List<BoundProduct> list;
	
	String[] column = {"날짜", "작성자", "입고상태"};
	
	// 첫 화면 조회 -> 추후 로그인한 계정에 따른 지점 선택 추가
	public InboundListModel (String str) {
		inboundDAO = new InboundDAO();
		
		if(str.equals("now")) {
        	list = inboundDAO.selectInbound();             	
        } 
	}
	
	// 지점 변경에 따른 테이블 조회 변화
	public InboundListModel(BoundProduct boundproduct) {
		inboundDAO = new InboundDAO();
    	list = inboundDAO.selectInbound(boundproduct);
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
		
        String value = null;

        switch (col) {      
            case 0: 
            	value = boundproduct.getBound().getRequest_date().toString();
                break;
            case 1: 
            	value = boundproduct.getBound().getUser().getUser_name();
                break;
            case 2: 
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
