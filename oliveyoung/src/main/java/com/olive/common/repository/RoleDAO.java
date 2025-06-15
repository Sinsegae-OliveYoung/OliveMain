package com.olive.common.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.olive.common.exception.BranchException;
import com.olive.common.model.Branch;
import com.olive.common.model.Role;
import com.olive.common.util.DBManager;

public class RoleDAO {
	DBManager dbManager = DBManager.getInstance();

	public List selectAll() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<Role> list = new ArrayList();
		
		con = dbManager.getConnection();
		
		StringBuffer sql = new StringBuffer();
		sql.append("select * from role");
		try {
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			list = new ArrayList();
			
			while (rs.next()) {
				Role role = new Role();
				role.setRole_id(rs.getInt("role_id"));
				role.setRole_code(rs.getString("role_code"));
				role.setRole_name(rs.getString("role_name"));
				list.add(role);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbManager.release(pstmt, rs);
		}
		return list;
	}
}
