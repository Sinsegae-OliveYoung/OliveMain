package com.olive.common.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.olive.common.exception.UserException;
import com.olive.common.model.Branch;
import com.olive.common.model.Role;
import com.olive.common.model.User;
import com.olive.common.util.DBManager;

public class UserDAO {
	DBManager dbManager = DBManager.getInstance();
	
	// 한 개의 레코드 삽입
	public void insert(User user) throws UserException {
		Connection con = null;
		PreparedStatement pstmt = null;
		
		con = dbManager.getConnection();
		
		StringBuffer sql = new StringBuffer();
		sql.append("insert into user(user_no, user_name, role_id, tel, email, hiredate) values(?, ?, ?, ?, ?, ?)");
		
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, user.getUser_no());
			pstmt.setString(2, user.getUser_name());
			pstmt.setInt(3, user.getRole().getRole_id());
			pstmt.setString(4, user.getTel());
			pstmt.setString(5, user.getEmail());
			pstmt.setDate(6, user.getHiredate());
			
			int result = pstmt.executeUpdate();
			if(result < 1)
				throw new UserException("사원 등록에 실패하였습니다");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserException("사원 등록에 실패하였습니다", e);
		} finally {
			dbManager.release(pstmt);
		}
	}
	
	// 유저 모든 정보 가져오기
	public List selectAll() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<User> list = new ArrayList();
		
		con = dbManager.getConnection();
		
		StringBuffer sql = new StringBuffer();
		sql.append("select user_no as '사원 번호', user_name as '이름', br_name as '소속 지점', role_name a '직급', role_code as '직급 코드', tel as '연락처', email as '이메일', hiredate as '입사일' from role r inner join user u inner join branch b on r.role_id = u.role_id and u.user_id = b.user_id");
		
		try {
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			list = new ArrayList();
			
			while (rs.next()) {
				User user = new User();
				user.setUser_no(rs.getInt("user_no"));
				user.setUser_name(rs.getString("user_name"));
				user.setTel(rs.getString("tel"));
				user.setEmail(rs.getString("email"));
				user.setHiredate(rs.getDate("hiredate"));
				
				// 직무 (Role) 카테고리
				Role role = new Role();
				role.setRole_code(rs.getString("role_code"));
				role.setRole_name(rs.getString("role_name"));
				user.setRole(role);
				
				// 지점 (Branch) 카테고리
				Branch branch = new Branch();
				branch.setBr_name(rs.getString("br_name"));

				list.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbManager.release(pstmt, rs);
		}
		return list;
	}
	
	// 한 개의 레코드 가져오기
	public void select() {
	}

	// 한 개의 레코드 수정
	public void update() {
	}

	// 한 개의 레코드 삭제
	public void delete() {
	}
}
