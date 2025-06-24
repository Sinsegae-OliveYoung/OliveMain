package com.olive.bound.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import com.olive.common.model.BoundProduct;
import com.olive.common.model.ProductOption;
import com.olive.common.model.Stock;
import com.olive.common.repository.BoundDAO;

public class BoundProductEditModel extends AbstractTableModel{
	
	BoundDAO boundDAO;
	public List<BoundProduct> list;
	List<Stock> stockList;
	
	boolean editFlag;
	
	Map<Integer, Integer> stockMap = new HashMap<>(); // option_id → 재고수량
	
	String[] column = {"카테고리", "상세카테고리", "브랜드", "제품명",  "호수",  "가격", "재고수량", "요청수량"	};
	
	// 요청서 id로 해당 지점의 재고 및 요청서의 제품 정보 가져오기
	public BoundProductEditModel(int bound_id) {
		this.editFlag = true;
		boundDAO = new BoundDAO();
        this.list = boundDAO.boundEditProduct(bound_id); // BoundProduct만 리턴

        // 재고 수량 따로 가져오기
        stockList = boundDAO.selectStockForBound(bound_id);
        for (Stock stock : stockList) {
            stockMap.put(stock.getProductOption().getOption_id(), stock.getSt_quantity());
        }
    }
	
	public BoundProductEditModel(int bound_id, boolean editFlag) {
		this(bound_id);
		this.editFlag = editFlag;
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
	
	public BoundProduct getBoundProduct(int row) {
//	    return list.get(row);
		BoundProduct bp = list.get(row);
	    return bp;
	}
	
	
	// 요청수량 클릭시 변경가능하도록
	@Override
	public boolean isCellEditable(int row, int column) {
	    return editFlag && column == 7; // 요청수량 컬럼만 수정 가능
	}
	
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
	    BoundProduct bp = list.get(rowIndex);

	    if (columnIndex == 7) { // 요청수량
	        try {
	            int newCount = Integer.parseInt(aValue.toString());
	            bp.setB_count(newCount);
	        } catch (NumberFormatException e) {
	            System.out.println("숫자가 아닙니다: " + aValue);
	        }
	    }

	    fireTableCellUpdated(rowIndex, columnIndex);
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
	    if (columnIndex == 7) return Integer.class; // 요청수량
	    return String.class;
	}

	@Override
	public Object getValueAt(int row, int col) {
//		Stock stock = list.get(row);
		BoundProduct boundProduct = list.get(row);
		ProductOption productOption = boundProduct.getProductOption();
        String value = null;

        switch (col) {      
            case 0: //카테고리
                value = boundProduct.getProductOption().getProduct().getCategory().getCt_name();
                break;
            case 1: //상세카테고리
            	value = boundProduct.getProductOption().getProduct().getCategory_detail().getCt_dt_name();
                break;
            case 2: //브랜드
            	value = boundProduct.getProductOption().getProduct().getBrand().getBd_name();
                break;
            case 3: //제품명
            	value = boundProduct.getProductOption().getProduct().getProduct_name();
                break;
            case 4: //호수
//            	value = Integer.toString(stock.getProductOption().getOption_no());
            	value = boundProduct.getProductOption().getOption_name();
                break;
            case 5: //가격
            	value = Integer.toString(boundProduct.getProductOption().getPrice());
                break;
            case 6: //재고수량
            	int st_qty = stockMap.getOrDefault(productOption.getOption_id(), 0);
                value = Integer.toString(st_qty);
                
                break;
            case 7: //요청수량
            	value = Integer.toString(boundProduct.getB_count());
                break;
        }

        return value;
    }

	public BoundProduct getBoundProductAt(int row) {
	    return list.get(row); // list는 BoundProduct를 담고 있는 내부 리스트
	}
}
