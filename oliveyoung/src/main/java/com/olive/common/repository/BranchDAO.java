package com.olive.common.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.olive.common.model.Branch;
import com.olive.common.util.DBManager;

public class BranchDAO {
	DBManager dbManager = DBManager.getInstance();
	
	public List selectAll() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList list = new ArrayList<>();
		
		try {
			con = dbManager.getConnection();
			StringBuffer sql = new StringBuffer();
			
			sql.append("select * from branch");
			
			pstmt=con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				Branch branch = new Branch();
				branch.setBr_id(rs.getInt("br_id"));
				branch.setBr_name(rs.getString("br_name"));
//				branch.setBr_address(rs.getString("br_address"));
//				branch.setBr_tel(rs.getString("br_address"));
//				branch.setBr_tel(rs.getString("br_tel"));
//				branch(setUser(user));
				
				list.add(branch);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbManager.release(pstmt, rs);
		}
		
		return list;
	};
}
