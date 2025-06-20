package com.olive.common.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.olive.common.exception.UserException;
import com.olive.common.model.Branch;
import com.olive.common.model.Member;
import com.olive.common.model.Role;
import com.olive.common.model.User;
import com.olive.common.util.DBManager;
import com.olive.common.util.StringUtil;

public class UserDAO {
	DBManager dbManager = DBManager.getInstance();
	
	// 한 건의 사원 데이터 삽입
	public void insert(User user) throws UserException {
		Connection con = null;
		PreparedStatement pstmt = null;
		
		con = dbManager.getConnection();
		
		StringBuffer sql = new StringBuffer();
		sql.append("insert into user(user_no, user_name, role_id, tel, email, hiredate, pwd) values(?, ?, ?, ?, ?, ?, ?)");
		
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, user.getUser_no());
			pstmt.setString(2, user.getUser_name());
			pstmt.setInt(3, user.getRole().getRole_id());
			pstmt.setString(4, user.getTel());
			pstmt.setString(5, user.getEmail());
			pstmt.setDate(6, user.getHiredate());
			pstmt.setString(7, user.getPwd());
			
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

	// 모든 사원 데이터 가져오기
	public List<User> selectAll() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<User> list = new ArrayList();
		
		con = dbManager.getConnection();
		
		StringBuffer sql = new StringBuffer();
		sql.append("select"
				+ " user_id 	as '등록 번호'"
				+ ", user_no 	as '사원 번호'"
				+ ", user_name 	as '이름'"
				+ ", role_name 	as '직급'"
				+ ", role_code 	as '직급 코드'"
				+ ", tel 		as '연락처'"
				+ ", email 		as '이메일'"
				+ ", hiredate 	as '입사일' "
				+ "from role r "
				+ "inner join user u on r.role_id = u.role_id "
				+ "order by user_no"
		);
		
		try {
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			list = new ArrayList();
			
			while (rs.next()) {
				User user = new User();
				user.setUser_id(rs.getInt("등록 번호"));
				user.setUser_no(rs.getInt("사원 번호"));
				user.setUser_name(rs.getString("이름"));
				user.setTel(rs.getString("연락처"));
				user.setEmail(rs.getString("이메일"));
				user.setHiredate(rs.getDate("입사일"));
				
				// 직무 (Role) 카테고리
				Role role = new Role();
				role.setRole_code(rs.getString("직급 코드"));
				role.setRole_name(rs.getString("직급"));
				user.setRole(role);
				
				list.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbManager.release(pstmt, rs);
		}
		return list;
	}
	
	// 한 건의 사원 데이터 수정
	public void update() {
	}

	// 한 건의 사원 데이터 삭제
	public void delete() {
	}
	
	// 로그인 체크 (
	public User checkLogin(int id, String pwd) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		User user = null;
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT"
			    + " u.user_id,"
			    + " u.user_no,"
			    + " u.user_name,"
			    + " u.tel,"
			    + " u.pwd,"
			    + " u.email,"
			    + " u.hiredate,"
			    + " u.role_id,"
			    + " r.role_code AS role_code,"
			    + " r.role_name AS role_name"
			    + " FROM user u"
			    + " INNER JOIN role r ON u.role_id = r.role_id"
			    + " WHERE u.user_no=? AND u.pwd=?");
		
		try {
			con = dbManager.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, id);
			pstmt.setString(2, StringUtil.getSecuredPass(pwd));
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				// 새로운 유저 객체에
				Role role = new Role();

				role.setRole_code(rs.getString("role_code"));   // 테이블명 제거
				role.setRole_id(rs.getInt("role_id"));          // 이건 OK
				role.setRole_name(rs.getString("role_name"));   // 테이블명 제거
				
				user = new User();
				user.setUser_name(rs.getString("u.user_name"));		// 해당되는 pk 담기
				user.setUser_id(rs.getInt("u.user_id"));		// 해당되는 pk 담기
				user.setUser_no(rs.getInt("u.user_no"));	// 해당되는 사원번호(아이디) 담기
				user.setPwd(rs.getString("pwd"));			// 해당되는 비밀번호(패스워드) 담기
				user.setRole(role);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}		
		// 넘겨받은 아이디와 패스워드의 값에 해당되는 유저 반환
		return user;
	}
	
}
