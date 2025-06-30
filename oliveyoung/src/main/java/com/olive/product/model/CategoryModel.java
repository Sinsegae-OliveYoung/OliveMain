package com.olive.product.model;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.olive.common.model.CategoryDetail;
import com.olive.common.repository.CategoryDetailDAO;

public class CategoryModel extends AbstractTableModel{
	
	CategoryDetailDAO categoryDetailDAO;
	public List<CategoryDetail> list;
	
	String[] column = {"카테고리 코드", "카테고리", "상세 카테고리 코드", "상세카테고리"};
	
	public CategoryModel () {
		categoryDetailDAO = new CategoryDetailDAO();
		
		list = categoryDetailDAO.selectAll();             	
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


	@Override
	public Object getValueAt(int row, int col) {
		CategoryDetail categoryDetail = list.get(row);
		
//        String value = null;

        switch (col) {      
            case 0: //카테고리
                return categoryDetail.getCategory().getCt_code();
            case 1: //상세카테고리
            	return categoryDetail.getCategory().getCt_name();
            case 2: //브랜드
            	return categoryDetail.getCt_dt_code();
            case 3: //제품명
            	return categoryDetail.getCt_dt_name();

        }

        return null;
    }
	
	
	public void setList(List<CategoryDetail> list) {
        this.list = list;
        fireTableDataChanged(); // 테이블 갱신
    }

    public List<CategoryDetail> getCategoryDetailList() {
        return list;
    }

    public CategoryDetail getCategoryDetail(int index) {
        return list.get(index);
    }
	
	public void clear() {
		list.clear();
        fireTableDataChanged();
    }

}
