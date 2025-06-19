package com.olive.common.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.olive.common.model.Branch;
import com.olive.common.model.Member;
import com.olive.common.model.Role;
import com.olive.common.model.User;
import com.olive.common.util.DBManager;
import com.olive.manage.MemberFilterDTO;

public class MemberDAO {

	DBManager dbManager = DBManager.getInstance();
	
	//로그인한 사용자가 관리하는 지점에 속한 member 조회 
	//동적 쿼리: UserListPanel에서 조건 걸고 검색 
	public List<Member> select(MemberFilterDTO filter){
		
		System.out.println("MemberDAO.select()");
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<Member> list = new ArrayList();
		List<Object> params = new ArrayList<>();
		
		con = dbManager.getConnection();
		
		StringBuffer sql = new StringBuffer();
		sql.append("select mem_id, u.user_id, user_no, user_name, b.br_id, br_name, br_address, br_tel, r.role_id, role_name, role_code, tel, email, hiredate");
		sql.append(" from member m inner join user u inner join branch b inner join role r");
		sql.append(" on m.user_id = u.user_id");
		sql.append(" and m.br_id = b.br_id");
		sql.append(" and u.role_id = r.role_id");
		sql.append(" where m.br_id in (");
		sql.append(" select br_id from member where user_id = ?)");
		params.add(filter.getUser_id());
		
		// 조건이 선택되었다면 sql where절에 추가  
		if(filter.getBr_id() != 0) {
			sql.append(" and b.br_id = ?");
			params.add(filter.getBr_id());
		}
		
		if(filter.getRole_id() != 0) {
			sql.append(" and r.role_id = ?");
			params.add(filter.getRole_id());
		}
		
		if(!filter.getUser_name().equals("이름")) {
			sql.append(" and user_name = ?");
			params.add(filter.getUser_name());
		}
		
		if(filter.getStart_date() != null) {
			sql.append(" and hiredate >= ?");
			params.add(filter.getStart_date());
		}
		
		if(filter.getEnd_date() != null) {
			sql.append(" and hiredate <= ?");
			params.add(filter.getEnd_date());
		}
		
		System.out.println(sql.toString());
		try {
			pstmt = con.prepareStatement(sql.toString());
			for(int i = 0; i < params.size(); i++) {
				pstmt.setObject(i+1, params.get(i));
			}
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				Role role = new Role();
				role.setRole_id(rs.getInt("r.role_id"));
				role.setRole_code(rs.getString("role_code"));
				role.setRole_name(rs.getString("role_name"));
				
				User user = new User();
				user.setUser_id(rs.getInt("u.user_id"));
				user.setUser_no(rs.getInt("user_no"));
				user.setUser_name(rs.getString("user_name"));
				user.setEmail(rs.getString("email"));
				user.setHiredate(rs.getDate("hiredate"));
				user.setTel(rs.getString("tel"));
				user.setRole(role);
				
				Branch branch = new Branch();
				branch.setBr_id(rs.getInt("b.br_id"));
				branch.setBr_name(rs.getString("br_name"));
				branch.setBr_address(rs.getString("br_address"));
				branch.setBr_tel(rs.getString("br_tel"));
				
				Member member = new Member();
				member.setMem_id(rs.getInt("mem_id"));
				member.setBranch(branch);
				member.setUser(user);
				
				list.add(member);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbManager.release(pstmt, rs);
		}
		
		return list;
	}
	
}
