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
			sql.append("select * from brand");
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
}
