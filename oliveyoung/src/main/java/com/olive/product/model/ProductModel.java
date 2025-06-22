package com.olive.product.model;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.olive.common.model.ProductOption;
import com.olive.common.model.User;
import com.olive.common.repository.ProductDAO;
import com.olive.common.repository.ProductOptionDAO;

public class ProductModel extends AbstractTableModel {

    String[] column = {"상품코드", "브랜드명", "상품명", "상품분류", "가격", "활성화"};
    List<ProductOption> list;
    User user;

    public ProductModel(User user) {
        ProductOptionDAO dao = new ProductOptionDAO();
        list = dao.selectAllWithDetails(user); // 브랜드명, 분류 포함하여 조인된 리스트
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
        ProductOption option = list.get(row);
        switch (col) {
            case 0: return option.getOption_code();
            case 1: return option.getProduct().getBrand().getBd_name();
            case 2: return option.getProduct().getProduct_name();
            case 3: return option.getProduct().getCategory_detail().getCt_dt_name();
            case 4: return option.getPrice();
            case 5: return option.getOption_active().equals("y") ? "활성" : "비활성";
        }
        return null;
    }
}
