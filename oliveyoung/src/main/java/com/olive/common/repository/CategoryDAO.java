package com.olive.common.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.olive.common.model.Category;
import com.olive.common.util.DBManager;

public class CategoryDAO {

	DBManager dbManager = DBManager.getInstance();
	
	public List<Category> selectAll() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList list = new ArrayList<>();
		
		try {
			con = dbManager.getConnection();
			StringBuffer sql = new StringBuffer();
			sql.append("select * from category");
			pstmt=con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				Category category = new Category();
				category.setCt_id(rs.getInt("ct_id"));
				category.setCt_code(rs.getString("ct_code"));
				category.setCt_name(rs.getString("ct_name"));
				list.add(category);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbManager.release(pstmt, rs);
		}
		
		return list;
	};
	
	  public Category selectById(int ct_id) {
	        Connection con = null;
	        PreparedStatement pstmt = null;
	        ResultSet rs = null;
	        Category category = null;

	        String sql = "SELECT * FROM category WHERE ct_id = ?";

	        try {
	            con = dbManager.getConnection();
	            pstmt = con.prepareStatement(sql);
	            pstmt.setInt(1, ct_id);
	            rs = pstmt.executeQuery();

	            if (rs.next()) {
	                category = new Category();
	                category.setCt_id(rs.getInt("ct_id"));
	                category.setCt_name(rs.getString("ct_name"));
	                // 필요한 다른 컬럼이 있다면 여기 추가
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	        } finally {
	            dbManager.release(pstmt, rs);
	        }

	        return category;
	    }
}
