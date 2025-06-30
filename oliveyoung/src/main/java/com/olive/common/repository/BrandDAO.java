package com.olive.common.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.olive.common.model.Brand;
import com.olive.common.model.Category;
import com.olive.common.util.DBManager;

public class BrandDAO {

	DBManager dbManager = DBManager.getInstance();
	
	public List<Brand> selectAll() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList list = new ArrayList<>();
		
		try {
			con = dbManager.getConnection();
			StringBuffer sql = new StringBuffer();
			sql.append("select"
					+ "		 bd_id"
					+ "		,bd_code"
					+ "		,bd_name"
					+ " from brand");
			pstmt=con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				Brand brand = new Brand();
				brand.setBd_id(rs.getInt("bd_id"));
				brand.setBd_code(rs.getString("bd_code"));
				brand.setBd_name(rs.getString("bd_name"));
				list.add(brand);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbManager.release(pstmt, rs);
		}
		
		return list;
	};
	
	public int insert(Brand brand) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
	    int result = 0;

	    try {
	        con = dbManager.getConnection();
	        String sql = "INSERT INTO brand (bd_code, bd_name) VALUES (?, ?)";
	        pstmt = con.prepareStatement(sql);
	        pstmt.setString(1, brand.getBd_code());
	        pstmt.setString(2, brand.getBd_name());

	        result = pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        dbManager.release(pstmt, rs);
	    }

	    return result;
	}
	
	
	public int delete(int bd_id) {
	    Connection con = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    
	    int result = 0;
	    try {
	        con = dbManager.getConnection();
	        String sql = "DELETE FROM brand WHERE bd_id = ?";
	        pstmt = con.prepareStatement(sql);
	        pstmt.setInt(1, bd_id);
	        result = pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        dbManager.release(pstmt, rs);
	    }
	    return result;
	}
	
	public List<Brand> load() {
	    return selectAll(); // selectAll()을 통해 전체 목록을 반환
	}
}
