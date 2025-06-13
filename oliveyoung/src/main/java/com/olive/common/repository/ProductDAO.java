package com.olive.common.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.olive.common.model.Brand;
import com.olive.common.model.Category;
import com.olive.common.model.Product;
import com.olive.common.util.DBManager;

public class ProductDAO {
	DBManager dbManager = DBManager.getInstance();
	
	// 모든 상품 목록 가져오기
	public List<Product> selectAll() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		List<Product> list = new ArrayList<>();
		
		con = dbManager.getConnection();
		StringBuffer sql = new StringBuffer();
		
//		sql.append("select 	c.ct_name");
//		sql.append("	   ,cd.ct_dt_name");
//		sql.append("	   ,p.product_name");
//		sql.append("	   ,po.option_code");
//		sql.append("	   ,b.bd_name");
//		sql.append("from	product p ");
//		sql.append("	   ,product_option po ");
//		sql.append("	   ,category c ");
//		sql.append("	   ,category_detail cd ");
//		sql.append("	   ,brand b");
//		sql.append("where	 1 = 1");
//		sql.append("and 	p.product_id = po.product_id");
//		sql.append("and 	p.ct_id = c.ct_id");
//		sql.append("and 	p.ct_dt_id = cd.ct_dt_id");
//		sql.append("and 	p.bd_id = b.bd_id ");
		sql.append("select product_name from product");
		
		try {
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				Product product = new Product();
				product.setProduct_name(rs.getString("product_name"));

//				// Category 객체 생성
//                Category category = new Category();
//                category.setCt_name(rs.getString("ct_name"));
//				
//				
//				// 브랜드
//				Brand brand = new Brand();
//				brand.setBd_name(rs.getString("bd_name")); 
				
				
				list.add(product);
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbManager.release(pstmt, rs);
		}
		
		
		return list;
	}
}
