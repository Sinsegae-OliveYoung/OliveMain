package com.olive.product.model;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.olive.common.model.Brand;
import com.olive.common.repository.BrandDAO;

public class BrandModel extends AbstractTableModel{
	
	BrandDAO brandDAO;
	public List<Brand> list;
	
	String[] column = {"브랜드명", "브랜드 코드"};
	
	public BrandModel () {
		brandDAO = new BrandDAO();
		
        list = brandDAO.selectAll();             	
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
	
	public Brand getRow(int row) {
	    return list.get(row);
	}
	
	public List<Brand> getBrandList() {
	    return list; // 브랜드 리스트 전체 반환
	}
	
	public void setList(List<Brand> list) {
        this.list = list;
        fireTableDataChanged();
    }

	@Override
	public Object getValueAt(int row, int col) {
		Brand brand = list.get(row);
		
        String value = null;

        switch (col) {      
            case 0: // 브랜드명
                value = brand.getBd_name();
                break;
            case 1: // 브랜드코드
            	value =  brand.getBd_code();
                break;
        }

        return value;
    }

	public void clear() {
		list.clear();
        fireTableDataChanged();
    }
}
