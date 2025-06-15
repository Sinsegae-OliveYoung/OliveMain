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
		
		//sql.append("select product_id, product_name from product");
		sql.append("select c.ct_name");
		sql.append(" 	 	   ,cd.ct_dt_name");
		sql.append(" 	 	   ,b.bd_name");
		sql.append(" 	 	   ,p.product_name");
		sql.append(" 	 	   ,po.option_code");
		sql.append(" 	 	   ,po.option_no");
		sql.append(" 	 	   ,po.price");
		sql.append(" 	 	   ,s.st_quantity");
		sql.append(" 	 	   ,br.br_name"); 
		sql.append(" from	product p");
		sql.append(" inner join product_option po");
		sql.append(" inner join category c");
		sql.append(" inner join category_detail cd");
		sql.append(" inner join brand b");
		sql.append(" inner join branch br");
		sql.append(" inner join stock s"); 
		sql.append(" on	1 = 1");
		sql.append(" and 	p.product_id = po.product_id");
		sql.append(" and 	p.ct_id = c.ct_id");
		sql.append(" and 	p.ct_dt_id = cd.ct_dt_id");
		sql.append(" and 	p.bd_id = b.bd_id"); 
		sql.append(" and 	s.option_id = po.option_id");
		sql.append(" and 	br.br_id = s.br_id"); // s.br_id = 지점 아이디
		sql.append(" order by br.br_name, cd.ct_dt_name");

		
		try {
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				// Prodcut 상품 객체 생성
				Product product = new Product();
				product.setProduct_id(rs.getInt("p.product_id"));
				product.setProduct_name(rs.getString("product_name"));
				
				ProductOption productOption = new ProductOption();
				productOption.setOption_code(rs.getString("option_code"));
				productOption.setOption_no(rs.getInt("option_no"));
				productOption.setPrice(rs.getInt("price"));
				productOption.setOption_id(rs.getInt("po.option_id"));
				productOption.setProduct(product);

				// Category 카테고리 객체 생성
                Category category = new Category();
//                category.setCt_id(rs.getInt("ct_id"));
                category.setCt_name(rs.getString("ct_name"));
				
                // Category Detail 카테코리 상세 객체 생성
                CategoryDetail c_detail = new CategoryDetail();
//                c_detail.setCt_dt_id(rs.getInt("ct_dt_id"));
                c_detail.setCt_dt_name(null);
				
				// Brand 브랜드
				Brand brand = new Brand();
//				brand.setBd_id(rs.getInt("bd_id")); 
				brand.setBd_name(rs.getString("bd_name")); 
				
				// Branch 지점
				Branch branch = new Branch();
//				branch.setBr_id(rs.getInt("br_id"));
				branch.setBr_name(rs.getString("br_name"));
				
				// Srock 재고
				Stock stock = new Stock();
//				stock.setSt_id(rs.getInt("st_id"));
				stock.setSt_quantity(rs.getInt("st_quantity"));
				stock.setProductOption(productOption);
				stock.setBranch(branch);
				
//				productOption.setOption_id(stock);
				
				
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
