package com.olive.stock.model;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.olive.common.model.Category;
import com.olive.common.model.Stock;
import com.olive.common.model.User;
import com.olive.common.repository.StockDAO;
import com.olive.common.repository.StockLogDAO;
import com.olive.stock.StockPage;
import com.olive.stock.update.view.StockUpdatePanel;


public class UpdateModel extends AbstractTableModel {

    StockDAO stockDAO;
    List<Stock> list;
    StockPage stockpage; // 재고 업데이트 반영을 위함
    User user;

    String[] column = {
        "상품옵션코드", "카테고리명", "상세카테고리명",
        "상품명", "브랜드", "가격",
        "재고수량", "입고일"
    };
//    String[] column = {
//    		"option_code", "ct_code", "cd_dt_code",
//    		"product_name", "brand", "price",
//    		"st_quantity", "st_update"
//    };

    public UpdateModel(StockPage stockpage, User user) { // StockPage를 보관해야함 -> update 위함
    	this.stockpage = stockpage;
    	this.user = user;
        stockDAO = new StockDAO();	
        list = stockDAO.listNow(user); 
    }
    
    public void reload() {
    	   stockDAO = new StockDAO();	
           list = stockDAO.listNow(user); 	
           fireTableDataChanged();
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

    public Object getValueAt(int row, int col) {
        Stock stock = list.get(row);

        String value = null;

        switch (col) {
            case 0:
                value = stock.getProductOption().getOption_code();
                break;
            case 1:
                value = stock.getProductOption()
                             .getProduct()
                             .getCategory_detail()
                             .getCategory()
                             .getCt_name();
                break;
            case 2:
                value = stock.getProductOption()
                             .getProduct()
                             .getCategory_detail()
                             .getCt_dt_name();
                break;
            case 3:
                value = stock.getProductOption()
                             .getProduct()
                             .getProduct_name();
                break;
            case 4:
                value = stock.getProductOption()
                             .getProduct()
                             .getBrand()
                             .getBd_name(); 
                break;
            case 5:
                value = Integer.toString(stock.getProductOption().getPrice());
                break;
            case 6:
                value = Integer.toString(stock.getSt_quantity());
                break;
            case 7:
            	 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        	    value = sdf.format(stock.getSt_update());
                break;
        }

        return value;
    }
    

    @Override
    public void setValueAt(Object value, int row, int col) {
	  Stock stock = list.get(row);

	    // 가격 컬럼인 경우만 처리
	    if (col == 6) {
	    	System.out.println(col);
	        try {
	            int quantity = Integer.parseInt(value.toString());

	            // 확인 다이얼로그 띄우기
	            int result = javax.swing.JOptionPane.showConfirmDialog(
	                null, // parent component (null이면 화면 중앙)
	                "수정하시겠습니까?",
	                "가격 수정 확인",
	                javax.swing.JOptionPane.YES_NO_OPTION
	            );

	            // 사용자가 '예'를 누른 경우에만 반영
	            if (result == javax.swing.JOptionPane.YES_OPTION) {
	                stock.setSt_quantity(quantity);
	                stockDAO.updateProductQuantity(stock.getSt_id(), quantity); // stock id에 맞게 수량 변경
	                
	                stockpage.setDataDirty(true);;  // isDataDirty = true
	                
	                fireTableCellUpdated(row, col); // 화면 갱신
	            }

	        } catch (NumberFormatException e) {
	            javax.swing.JOptionPane.showMessageDialog(null, "숫자만 입력 가능합니다.");
	        }
	    }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
    	if(columnIndex == 6) {
    		return true;
    	}
    	else return false;
    }
    
    public void sortByDateAsc() {
        list.sort(Comparator.comparing(Stock::getSt_update));
        fireTableDataChanged();
    }

    public void sortByDateDesc() {
        list.sort(Comparator.comparing(Stock::getSt_update).reversed());
        fireTableDataChanged();
    }

    public void sortByQuantityDesc() {
        list.sort(Comparator.comparingInt(Stock::getSt_quantity).reversed());
        fireTableDataChanged();
    }

    public void sortByNameAsc() {
        list.sort(Comparator.comparing(
            s -> s.getProductOption().getProduct().getProduct_name(), String.CASE_INSENSITIVE_ORDER));
        fireTableDataChanged();
    }

    public void sortByNameDesc() {
        list.sort(Comparator.comparing(
            s -> s.getProductOption().getProduct().getProduct_name(), String.CASE_INSENSITIVE_ORDER.reversed()));
        fireTableDataChanged();
    }


}
