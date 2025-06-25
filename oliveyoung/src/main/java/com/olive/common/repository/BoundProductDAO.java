package com.olive.common.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.olive.common.model.Bound;
import com.olive.common.model.BoundProduct;
import com.olive.common.model.Product;
import com.olive.common.model.ProductOption;
import com.olive.common.util.DBManager;

public class BoundProductDAO {

	DBManager dbManager = DBManager.getInstance();
	
	public List<BoundProduct> select(int bound_id){
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		List<BoundProduct> list = new ArrayList<>();
		
		con = dbManager.getConnection();
		
		StringBuffer sql = new StringBuffer();	
		
		sql.append("select b_pd_id, b_count,"
				+ " bo.bound_id, bound_flag,"
				+ " po.option_id, option_no, option_name, option_code, price, option_active,"
				+ " p.product_id, product_name");
		sql.append(" from bound_product bp");
		sql.append(" inner join bound bo");
		sql.append(" inner join product_option po");
		sql.append(" inner join product p");
		sql.append(" on bp.bound_id = bo.bound_id");
		sql.append(" and bp.option_id = po.option_id ");
		sql.append(" and po.product_id = p.product_id");
		sql.append(" where bo.bound_id = ?");
		
		System.out.println(sql.toString());
		
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, bound_id);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				Product p = new Product();
				p.setProduct_id(rs.getInt("p.product_id"));
				p.setProduct_name(rs.getString("product_name"));
				
				ProductOption po = new ProductOption();
				po.setOption_id(rs.getInt("po.option_id"));
				po.setOption_no(rs.getInt("option_no"));
				po.setOption_name(rs.getString("option_name"));
				po.setOption_code(rs.getString("option_code"));
				po.setPrice(rs.getInt("price"));
				po.setOption_active(rs.getString("option_active"));
				po.setProduct(p);
				
				Bound b = new Bound();
				b.setBound_id(rs.getInt("bo.bound_id"));
				
				BoundProduct bp = new BoundProduct();
				bp.setB_pd_id(rs.getInt("b_pd_id"));
				bp.setB_count(rs.getInt("b_count"));
				bp.setBound(b);
				bp.setProductOption(po);
				
				list.add(bp);				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbManager.release(pstmt, rs);
		}
		
		return list;
	}
}
