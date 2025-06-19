package com.olive.login.security.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.management.relation.Role;

import com.olive.common.model.User;
import com.olive.common.util.DBManager;
import com.olive.login.security.model.Admin;

public class AdminDAO {
	DBManager dbManager = DBManager.getInstance();
	
	// 모든 로그인 정보 가져오기
	public List selectAll() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<Admin> list = new ArrayList();
		
		con = dbManager.getConnection();
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM admin");
		
		try {
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				Admin admin = new Admin();
				admin.setAdmin_id(rs.getInt("admin_id"));
				admin.setPwd(rs.getString("pwd"));
				
				User user = new User();
				user.setUser_no(rs.getInt("user_no"));
				admin.setUser(user);
				
				list.add(admin);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbManager.release(pstmt, rs);
		}
		return list;
	}
	
}
