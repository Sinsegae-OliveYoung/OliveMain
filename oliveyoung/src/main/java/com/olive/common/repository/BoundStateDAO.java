package com.olive.common.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.olive.common.model.BoundState;
import com.olive.common.util.DBManager;

public class BoundStateDAO {
	
DBManager dbManager = DBManager.getInstance();
	
	public List<BoundState> selectAll() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<BoundState> list = new ArrayList<>();
		
		con = dbManager.getConnection();
		
		try {
			String sql = "select * from bound_state";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				BoundState bs = new BoundState();
				bs.setBo_state_id(rs.getInt("bo_state_id"));
				bs.setBo_state_name(rs.getString("bo_state_name"));
				
				list.add(bs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbManager.release(pstmt, rs);
		}
		
		return list;
	};
	
}
