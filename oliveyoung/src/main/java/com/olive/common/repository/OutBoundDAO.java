package com.olive.common.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.olive.common.model.Bound;
import com.olive.common.util.DBManager;

public class OutBoundDAO {
	DBManager dbManager = DBManager.getInstance();
	
	public List selectSales(int year, int month, int br_id) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList list = new ArrayList<>();

		con = dbManager.getConnection();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT SUM(price*b_count)"
				+ " FROM bound b"
				+ " INNER JOIN bound_product bp"
				+ " ON b.bound_id = bp.bound_id"
				+ " INNER JOIN product_option po"
				+ " ON bp.option_id = po.option_id"
				+ " WHERE bo_state_id=3"
				+ " AND YEAR(approve_date)=?"
				+ " AND MONTH(approve_date)=?"
				+ " AND br_id=?");
		
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, year);
			pstmt.setInt(2, month);
			pstmt.setInt(3, br_id);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				Bound bound = new Bound();
				bound.setBound_id(rs.getInt("br_id"));
				
				
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
		return list;
	}
	
	// 최근 3개월 상품 판매율 TOP 10
	public List<Map<String, String>> getTopProduct(int month){
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Map<String, String>> list = new ArrayList<>();
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT"
				+ " 		product_name AS Name,"
				+ " 		SUM(b_count) AS Quantity"
				+ " FROM branch br"
				+ " 		INNER JOIN bound b"
				+ " 			ON br.br_id=b.br_id"
				+ " 		INNER JOIN bound_product bp"
				+ " 			ON b.bound_id=bp.bound_id"
				+ " 		INNER JOIN product_option po"
				+ " 			ON bp.option_id=po.option_id"
				+ " 		INNER JOIN product p"
				+ " 			ON po.product_id=p.product_id"
				+ " WHERE bo_state_id=3"
				+ " 		AND DATE_FORMAT(approve_date, '%Y-%m') >= DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL ? MONTH), '%Y-%m')"
				+ " GROUP BY p.product_id"
				+ " ORDER BY Quantity DESC"
				+ " LIMIT 10;");
	
		try {
			con = dbManager.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, month-1);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				Map<String, String> map = new HashMap<>();
				map.put("Name", rs.getString("Name"));
				map.put("Quantity", rs.getString("Quantity"));

				list.add(map);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbManager.release(pstmt, rs);
		}
		
		return list;
	}
}
