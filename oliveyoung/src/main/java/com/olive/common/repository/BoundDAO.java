package com.olive.common.repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.olive.common.model.Bound;
import com.olive.common.model.BoundState;
import com.olive.common.model.Branch;
import com.olive.common.model.Member;
import com.olive.common.model.User;
import com.olive.common.util.DBManager;
import com.olive.manage.approval.BoundFilterDTO;

public class BoundDAO {
	
	DBManager dbManager = DBManager.getInstance();
	
	public List<Bound> select(BoundFilterDTO filter){
		System.out.println("BoundDAO.select()");
		
		Connection con = dbManager.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<Bound> list = new ArrayList<>();
		List<Object> params = new ArrayList<>();
		
		StringBuffer sql = new StringBuffer();
		sql.append("select bound_id, bo.user_id, bo.approver_id, bo.br_id, bo.bo_state_id, request_date, approve_date, comment, bound_flag,");
		sql.append(" user_name, user_no, u.role_id, tel, email, hiredate,");
		sql.append(" br_name, br_tel, br_address,");
		sql.append(" bo_state_name,");
		sql.append(" role_name, role_code");		
		sql.append(" from bound bo");
		sql.append(" inner join user u");
		sql.append(" inner join branch br");
		sql.append(" inner join bound_state bs");
		sql.append(" inner join role r");
		sql.append(" on bo.user_id = u.user_id");
		sql.append(" and bo.bo_state_id = bs.bo_state_id");
		sql.append(" and bo.br_id = br.br_id");
		sql.append(" and r.role_id = u.role_id");
		sql.append(" where bo.br_id in (");
		sql.append(" select br_id from member");
		sql.append(" where user_id = ?)");
		params.add(filter.getUser_id());
		
		if(filter.getBr_id() != 0) {
			sql.append(" and b.br_id = ?");
			params.add(filter.getBr_id());
		}
		
		if(!filter.getSubmitter_name().equals("이름")) {
			sql.append(" and user_name = ?");
			params.add(filter.getSubmitter_name());
		}
		
		if(filter.getBoundstate_id() != 0) {
			sql.append(" and bo.bo_state_id = ?");
			params.add(filter.getBoundstate_id());
		}
		
		if(filter.getStart_date() != null) {
			sql.append(" and request_date >= ?");
			params.add(filter.getStart_date());
		}
		
		if(filter.getEnd_date() != null) {
			sql.append(" and request_date <= ?");
			params.add(filter.getEnd_date());
		}
		
		sql.append(" order by request_date desc");   //최신순 
		
		System.out.println("BoundDAO.select(): " + sql.toString());
		
		try {
			pstmt = con.prepareStatement(sql.toString());
			for(int i = 0; i < params.size(); i++) {
				pstmt.setObject(i+1, params.get(i));
			}
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				// 화면 상에서는 bo_id/요청자/요청일/승인상태 정보만 필요한데 이걸 다 채워야 하는지..
				// 심지어 approver는 채우려면..  
				
				User user = new User();
				user.setUser_id(rs.getInt("bo.user_id"));
				user.setUser_no(rs.getInt("user_no"));
				user.setUser_name(rs.getString("user_name"));
				user.setEmail(rs.getString("email"));
				user.setTel(rs.getString("tel"));
				user.setHiredate(rs.getDate("hiredate"));

				User approver = new User();
				approver.setUser_id(rs.getInt("bo.approver_id"));
				
				Branch br = new Branch();
				br.setBr_id(rs.getInt("bo.br_id"));
				br.setBr_name(rs.getString("br_name"));
				br.setBr_tel(rs.getString("br_tel"));
				br.setBr_address(rs.getString("br_address"));
				
				BoundState bs = new BoundState();
				bs.setBo_state_id(rs.getInt("bo.bo_state_id"));
				bs.setBo_state_name(rs.getString("bo_state_name"));
				
				Bound bo = new Bound();
				bo.setBound_id(rs.getInt("bound_id"));
				bo.setUser(user);
				bo.setApprover(approver);
				bo.setBranch(br);
				bo.setBoundState(bs);
				bo.setRequest_date(rs.getDate("request_date"));
				bo.setApprove_date(rs.getDate("approve_date"));
				bo.setComment(rs.getString("comment"));
				bo.setBound_flag(rs.getString("bound_flag"));
				
				list.add(bo);
			}
			
			System.out.println("BoundDAO.list.size(): "+ list.size());
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
		
	}

}

