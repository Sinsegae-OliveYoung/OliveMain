package com.olive.common.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.olive.common.model.Branch;
import com.olive.common.model.Brand;
import com.olive.common.model.Category;
import com.olive.common.model.CategoryDetail;
import com.olive.common.model.Product;
import com.olive.common.model.ProductOption;
import com.olive.common.model.Stock;
import com.olive.common.model.StockIBLog;
import com.olive.common.util.DBManager;

public class StockLogDAO {
	
	DBManager dbManager = DBManager.getInstance();
	
	public List<StockIBLog> listIB(){
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<StockIBLog> list = new ArrayList<>();
		
		StringBuffer sql = new StringBuffer();
		sql.append("select po.option_code, ct.ct_code, ct_dt.ct_dt_code, p.product_name, bd.bd_name,");
		sql.append(" po.price, ib_pd.ib_pd_count, ib.ib_date, u.user_name, ia.ia_date");
		sql.append(" from inbound ib");
		sql.append(" join inbound_product ib_pd on ib.ib_id = ib_pd.ib_id");
		sql.append(" join inbound_approve ia on ia.ib_id = ib.ib_id");
		sql.append(" join product_option po on ib_pd.option_id = po.option_id");
		sql.append(" join product p on p.product_id = po.product_id");
		sql.append(" join category_detail ct_dt on ct_dt.ct_id = p.ct_id");
		sql.append(" join category ct on ct.ct_id = ct_dt.ct_id");
		sql.append(" join brand bd on bd.bd_id = p.bd_id");
		sql.append(" join user u on u.user_id = ia.user_id");
		
		try {
            con = dbManager.getConnection();
            pstmt = con.prepareStatement(sql.toString());
            rs = pstmt.executeQuery();

            while (rs.next()) {
                // Stock 객체 생성
                Stock stock = new Stock();
                stock.setSt_id(rs.getInt("st_id"));
                stock.setSt_quantity(rs.getInt("st_quantity"));
                stock.setSt_update(rs.getDate("st_update"));

//                list.add(stock);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbManager.release(pstmt, rs);
        }

        return list;
	}

}
