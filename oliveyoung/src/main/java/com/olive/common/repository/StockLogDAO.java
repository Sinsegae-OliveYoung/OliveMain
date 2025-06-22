package com.olive.common.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.olive.common.model.Branch;
import com.olive.common.model.Brand;
import com.olive.common.model.Category;
import com.olive.common.model.CategoryDetail;
import com.olive.common.model.Product;
import com.olive.common.model.ProductOption;
import com.olive.common.model.Stock;
import com.olive.common.model.StockHistory;
import com.olive.common.model.User;
import com.olive.common.util.DBManager;

public class StockLogDAO {
	
	DBManager dbManager = DBManager.getInstance();
	
	public List<StockHistory> listBound(String flag){
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<StockHistory> list = new ArrayList<>();
		
		StringBuffer sql = new StringBuffer();
		sql.append("select po.option_code, ct.ct_name, cd.ct_dt_name, p.product_name, b.bd_name, po.price, bp.b_count, bd.request_date, u.user_name, bd.approve_date"
				+ " from product_option po join product p on p.product_id = po.product_id"
				+ " join category_detail cd on cd.ct_dt_id = p.ct_dt_id"
				+ " join category ct on ct.ct_id = cd.ct_id"
				+ " join brand b on b.bd_id = p.bd_id"
				+ " join bound_product bp on bp.option_id = po.option_id"
				+ " join bound bd on bd.bound_id = bp.bound_id"
				+ " join user u on u.user_id = bd.approver_id "
				+ " join branch br on br.br_id = bd.br_id"
				+ " where br.br_id = 1"
				+ " and bd.bound_flag = ?");

		try {
			con = dbManager.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, flag); // "in" 또는 "out" 값 세팅
			rs = pstmt.executeQuery();

			while (rs.next()) {
				StockHistory his = new StockHistory();

				// Category
				Category category = new Category();
				category.setCt_name(rs.getString("ct_name"));

				// CategoryDetail
				CategoryDetail categoryDetail = new CategoryDetail();
				categoryDetail.setCt_dt_name(rs.getString("ct_dt_name"));
				categoryDetail.setCategory(category);

				// Brand
				Brand brand = new Brand();
				brand.setBd_name(rs.getString("bd_name"));

				// Product
				Product product = new Product();
				product.setProduct_name(rs.getString("product_name"));
				product.setBrand(brand);
				product.setCategory_detail(categoryDetail);

				// ProductOption
				ProductOption option = new ProductOption();
				option.setOption_code(rs.getString("option_code"));
				option.setPrice(rs.getInt("price"));
				option.setProduct(product);

				// User (승인자)
				User user = new User();
				user.setUser_name(rs.getString("user_name"));

				// StockHistory
				his.setProductOption(option);
				his.setQuantity(rs.getInt("b_count")); // b_count로 수정
				his.setRequestDate(rs.getDate("request_date")); // 컬럼명 수정
				his.setApprovalDate(rs.getDate("approve_date")); // 컬럼명 수정
				his.setManager(user);

				list.add(his);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbManager.release(pstmt, rs);
		}

		return list;
	}
	public List<StockHistory> listBoundDate(String state, String start, String end){
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<StockHistory> list = new ArrayList<>();

		// 문자열을 java.sql.Date로 변환
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
		LocalDate localStart = LocalDate.parse(start, formatter);
		LocalDate localEnd = LocalDate.parse(end, formatter);
		java.sql.Date sqlStart = java.sql.Date.valueOf(localStart);
		java.sql.Date sqlEnd = java.sql.Date.valueOf(localEnd);
		
		StringBuffer sql = new StringBuffer();
		sql.append("select po.option_code, ct.ct_name, cd.ct_dt_name, p.product_name, b.bd_name, po.price, bp.b_count, bd.request_date, u.user_name, bd.approve_date"
				+ " from product_option po join product p on p.product_id = po.product_id"
				+ " join category_detail cd on cd.ct_dt_id = p.ct_dt_id"
				+ " join category ct on ct.ct_id = cd.ct_id"
				+ " join brand b on b.bd_id = p.bd_id"
				+ " join bound_product bp on bp.option_id = po.option_id"
				+ " join bound bd on bd.bound_id = bp.bound_id"
				+ " join user u on u.user_id = bd.approver_id "
				+ " join branch br on br.br_id = bd.br_id"
				+ " where br.br_id = 1"
				+ " and bd.bound_flag = ?"
				+ " and bd.request_date between ? and ?");
		
		try {
			con = dbManager.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, state);
			pstmt.setDate(2, sqlStart);
			pstmt.setDate(3, sqlEnd);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				StockHistory his = new StockHistory();
				
				// Category
				Category category = new Category();
				category.setCt_name(rs.getString("ct_name"));
				
				// CategoryDetail
				CategoryDetail categoryDetail = new CategoryDetail();
				categoryDetail.setCt_dt_name(rs.getString("ct_dt_name"));
				categoryDetail.setCategory(category);
				
				// Brand
				Brand brand = new Brand();
				brand.setBd_name(rs.getString("bd_name"));
				
				// Product
				Product product = new Product();
				product.setProduct_name(rs.getString("product_name"));
				product.setBrand(brand);
				product.setCategory_detail(categoryDetail);
				
				// ProductOption
				ProductOption option = new ProductOption();
				option.setOption_code(rs.getString("option_code"));
				option.setPrice(rs.getInt("price"));
				option.setProduct(product);
				
				// User (승인자)
				User user = new User();
				user.setUser_name(rs.getString("user_name"));
				
				// StockHistory
				his.setProductOption(option);
				his.setQuantity(rs.getInt("b_count")); // b_count로 수정
				his.setRequestDate(rs.getDate("request_date")); // 컬럼명 수정
				his.setApprovalDate(rs.getDate("approve_date")); // 컬럼명 수정
				his.setManager(user);
				
				list.add(his);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbManager.release(pstmt, rs);
		}
		
		return list;
	}
}
