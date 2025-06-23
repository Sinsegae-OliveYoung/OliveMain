package com.olive.common.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.olive.common.exception.BranchException;
import com.olive.common.model.Branch;
import com.olive.common.model.Brand;
import com.olive.common.model.Category;
import com.olive.common.model.CategoryDetail;
import com.olive.common.model.Product;
import com.olive.common.model.ProductOption;
import com.olive.common.model.Role;
import com.olive.common.model.Stock;
import com.olive.common.model.User;
import com.olive.common.util.DBManager;

public class BranchDAO {
	DBManager dbManager = DBManager.getInstance();

	// 지점의 모든 데이터를 반환
	public List selectAll() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList list = new ArrayList<>();
		
		try {
			con = dbManager.getConnection();
			StringBuffer sql = new StringBuffer();
			
			sql.append("SELECT * FROM branch");
			
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
	
	// 한 개의 레코드 삽입 (branch, member에 insert)
	public void insert(Branch branch) throws BranchException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		// insert 대상이 둘이므로 insert문도 각각 생성
		StringBuffer branchSql = new StringBuffer();
		branchSql.append("INSERT INTO"
				+ " branch(br_name, br_address, br_tel, user_id)"
				+ " VALUES(?, ?, ?, ?)");
		StringBuffer memberSql = new StringBuffer();
		memberSql.append("INSERT INTO"
				+ " member(br_id, user_id)"
				+ " VALUES(?, ?)");
		
		try {
			con = dbManager.getConnection();
			con.setAutoCommit(false);
	        
			// Branch 테이블에 등록
	        // branch 테이블에 등록 후 그 값으로 member 테이블에 등록하기 위해서 pk값 반환 옵션 추가
			pstmt = con.prepareStatement(branchSql.toString(), PreparedStatement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, branch.getBr_name());
			pstmt.setString(2, branch.getBr_address());
			pstmt.setString(3, branch.getBr_tel());
			pstmt.setInt(4, branch.getUser().getUser_id());
			int brResult = pstmt.executeUpdate();

			// branch insert 후 그 키 값 다시 받아와 변수에 저장
			rs = pstmt.getGeneratedKeys();	
			int br_id = 0;
			if (rs.next())	
				br_id = rs.getInt(1);	// 첫번째 컬럼(br_id) 가져오기
			// branch에서 사용한 rs, pstmt 닫기
			rs.close();
			pstmt.close();
			
			// Member 테이블에 등록
			pstmt = con.prepareStatement(memberSql.toString());
			pstmt.setInt(1, br_id);
			pstmt.setInt(2, branch.getUser().getUser_id());
			int mbResult = pstmt.executeUpdate();
			
			if (brResult < 1 || mbResult < 1) throw new BranchException("지점 등록에 실패하였습니다");
			else	con.commit();
		} catch (SQLException e) {
			try { if (con != null) con.rollback();} catch (SQLException e1) {e1.printStackTrace();	}
			e.printStackTrace();
			throw new BranchException("지점 등록에 실패하였습니다", e);
		} finally {
			try { if (con != null) con.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
			dbManager.release(pstmt, rs);
		}
	}

	// 한 개의 레코드 수정 (member, branch)
	public void update(Branch branch, User user) throws BranchException {
		Connection con = null;
		PreparedStatement pstmt = null;
		
		// member, branch 데이터 수정을 위한 각각의 sql문 작성
		StringBuffer branchSql = new StringBuffer();
		branchSql.append("UPDATE branch"
				+ " SET br_name = ?,"
				+ " br_address = ?,"
				+ " br_tel = ?,"
				+ " user_id = ?"
				+ " WHERE br_id = ?");
		StringBuffer memberSql = new StringBuffer();
		memberSql.append("UPDATE member"
				+ " SET br_id = ?"
				+ " , user_id = ?"
				+ " WHERE br_id = ?"
				+ " AND user_id = ?");
		
		try {
			con = dbManager.getConnection();
			con.setAutoCommit(false);

			// Branch 테이블에서 수정
			pstmt = con.prepareStatement(branchSql.toString());
			pstmt.setString(1, branch.getBr_name());
			pstmt.setString(2, branch.getBr_address());
			pstmt.setString(3, branch.getBr_tel());
			pstmt.setInt(4, branch.getUser().getUser_id());
			pstmt.setInt(5, branch.getBr_id());	
			int brResult = pstmt.executeUpdate();
			
			// branch에서 사용한 rs, pstmt 닫기
			pstmt.close();
			
			// Member 테이블에서 수정			
			pstmt = con.prepareStatement(memberSql.toString());
			pstmt.setInt(1, branch.getBr_id());	
			pstmt.setInt(2, branch.getUser().getUser_id());
			pstmt.setInt(3, branch.getBr_id());	
			pstmt.setInt(4, user.getUser_id());
			int mbResult = pstmt.executeUpdate();
						
			if(mbResult < 1 || brResult < 1) throw new BranchException("지점 수정에 실패하였습니다");
			else con.commit();
		} catch (SQLException e) {
			try { if (con != null) con.rollback();} catch (SQLException e1) {e1.printStackTrace();	}
			e.printStackTrace();
			throw new BranchException("지점 수정에 실패하였습니다", e);
		} finally {
			try { if (con != null) con.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
			dbManager.release(pstmt);
		}
	}

	// 한 개의 레코드 삭제 (member, branch)
	public void delete(Branch branch, User user) throws BranchException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		// member, branch 데이터 삭제를 위한 각각의 sql문 작성
		StringBuffer memberSql = new StringBuffer();
		memberSql.append("DELETE"
				+ " FROM member"
				+ " WHERE br_id = ?");
		StringBuffer branchSql = new StringBuffer();
		branchSql.append("DELETE"
				+ " FROM branch"
				+ " WHERE br_id = ?");
		
		try {
			con = dbManager.getConnection();
			con.setAutoCommit(false);
			
			// Member 테이블에서 삭제
			pstmt = con.prepareStatement(memberSql.toString());
			pstmt.setInt(1, branch.getBr_id());	
			int mbResult = pstmt.executeUpdate();
			
			// Member에서 사용한 pstmt 닫기
			pstmt.close();
			
			// Branch 테이블에서 삭제
			pstmt = con.prepareStatement(branchSql.toString());
			pstmt.setInt(1, branch.getBr_id());
			int brResult = pstmt.executeUpdate();
			
			if(brResult < 1) throw new BranchException("지점 삭제에 실패하였습니다");
			else con.commit();
		} catch (SQLException e) {
			try { if (con != null) con.rollback();} catch (SQLException e1) {e1.printStackTrace();	}
			e.printStackTrace();
			throw new BranchException("지점 삭제에 실패하였습니다", e);
		} finally {
			try { if (con != null) con.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
			dbManager.release(pstmt);
		}
	}

	// 모든 지점의 정보 가져오기
		public List selectBranch() {
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			ArrayList<Branch> list = new ArrayList();
			
			con = dbManager.getConnection();
			
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT br_id AS '등록 번호',"
					+ " br_name AS '지점명',"
					+ " user_name AS '담당자',"
					+ " br_address AS '주소',"
					+ " br_tel AS '연락처'"
					+ " FROM user u"
					+ " INNER JOIN branch b"
					+ " ON u.user_id = b.user_id"
					+ " ORDER BY br_id");
			try {
				pstmt = con.prepareStatement(sql.toString());
				rs = pstmt.executeQuery();
				list = new ArrayList();
				
				while (rs.next()) {
					Branch branch = new Branch();
					branch.setBr_id(rs.getInt("등록 번호"));
					branch.setBr_name(rs.getString("지점명"));
					branch.setBr_address(rs.getString("주소"));
					branch.setBr_tel(rs.getString("연락처"));
					
					// 사원 (User) 카테고리
					User user = new User();
					user.setUser_name(rs.getString("담당자"));
					branch.setUser(user);
					
					list.add(branch);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				dbManager.release(pstmt, rs);
			}
			return list;
		}
		
	// 한 지점의 상품 재고 페이지 출력
	public List selectBranchStock(String br_name) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<Stock> list = new ArrayList();
		
		con = dbManager.getConnection();
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT br_name AS '지점명',"
				+ " bd_name AS '브랜드',"
				+ " ct_name AS '상위 카테고리',"
				+ " ct_dt_name AS '하위 카테고리',"
				+ " product_name AS '상품명',"
				+ " st_quantity AS '재고',"
				+ " st_update AS '최근 수정일'"
				+ " FROM brand b"
				+ " INNER JOIN product p"
				+ " INNER JOIN product_option o"
				+ " INNER JOIN stock s"
				+ " INNER JOIN category c"
				+ " INNER JOIN category_detail cd"
				+ " INNER JOIN branch bh"
				+ " ON bh.br_id=s.br_id"
				+ " AND b.bd_id=p.bd_id"
				+ " AND p.product_id=o.product_id"
				+ " AND o.option_id=s.option_id"
				+ " AND p.ct_dt_id=cd.ct_dt_id"
				+ " AND c.ct_id=cd.ct_id"
				+ " AND bh.br_name=?");
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, br_name);
			rs = pstmt.executeQuery();
			list = new ArrayList();
			
			while (rs.next()) {
				Branch branch = new Branch();
				branch.setBr_name(rs.getString("지점명"));
				
				Category category = new Category();
				category.setCt_name(rs.getString("상위 카테고리"));
				
				CategoryDetail categoryDetail = new CategoryDetail();
				categoryDetail.setCt_dt_name(rs.getString("하위 카테고리"));
				categoryDetail.setCategory(category);
				
				Brand brand = new Brand();
				brand.setBd_name(rs.getString("브랜드"));
				
				Product product = new Product();
				product.setProduct_name(rs.getString("상품명"));
				product.setBrand(brand);
				product.setCategory_detail(categoryDetail);
				
				ProductOption productOption = new ProductOption();
				productOption.setProduct(product);
				
				Stock stock = new Stock();
				stock.setSt_quantity(rs.getInt("재고"));
				stock.setSt_update(rs.getDate("최근 수정일"));
				stock.setBranch(branch);
				stock.setProductOption(productOption);
				
				
				list.add(stock);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbManager.release(pstmt, rs);
		}
		return list;
	}
	
	// 한 지점의 세부 정보 반환
	public List selectBranchDetail(String br_name) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<Branch> list = new ArrayList();
		
		con = dbManager.getConnection();
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT br_name AS '지점명',"
				+ " br_address AS '매장 주소',"
				+ " br_tel AS '매장 전화',"
				+ " user_name AS '담당자',"
				+ " tel AS '연락처',"
				+ " email AS '이메일'"
				+ " FROM user u INNER JOIN branch b"
				+ " ON u.user_id=b.user_id AND br_name=?");
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, br_name);
			rs = pstmt.executeQuery();
			list = new ArrayList();
			
			while (rs.next()) {
				User user = new User();
				user.setUser_name(rs.getString("담당자"));
				user.setTel(rs.getString("연락처"));
				user.setEmail(rs.getString("이메일"));
				
				Branch branch = new Branch();
				branch.setBr_name(rs.getString("지점명"));
				branch.setBr_address(rs.getString("매장 주소"));
				branch.setBr_tel(rs.getString("매장 전화"));
				branch.setUser(user);
				
				list.add(branch);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbManager.release(pstmt, rs);
		}
		return list;
	}
	


	// 로그인한 user가 관리하는 branch 목록 반환
	public List<Branch> getBranchList(int user_id){
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<Branch> list = new ArrayList();
		
		con = dbManager.getConnection();
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT b.br_id,"
				+ " br_name,"
				+ " br_address,"
				+ " br_tel,"
				+ " u.user_id,"
				+ " user_name,"
				+ " tel,"
				+ " hiredate,"
				+ " email,"
				+ " r.role_id,"
				+ " role_name,"
				+ " role_code"
				+ " FROM branch b"
				+ " INNER JOIN member m"
				+ " JOIN user u"
				+ " JOIN role r"
				+ " ON b.br_id = m.br_id"
				+ " AND u.user_id = m.user_id"
				+ " AND u.role_id = r.role_id"
				+ " WHERE m.user_id = ?");
	
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, user_id);  
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				
				Role role = new Role();
				role.setRole_id(rs.getInt("r.role_id"));
				role.setRole_code(rs.getString("r.role_code"));
				role.setRole_name(rs.getString("r.role_name"));
				
				User user = new User();
				user.setUser_id(rs.getInt("u.user_id"));
				user.setUser_name(rs.getString("user_name"));
				user.setTel(rs.getString("tel"));
				user.setHiredate(rs.getDate("hiredate"));
				user.setEmail(rs.getString("email"));
				user.setRole(role);
				
				Branch branch = new Branch();
				branch.setBr_id(rs.getInt("b.br_id"));
				branch.setBr_name(rs.getString("br_name"));
				branch.setBr_address(rs.getString("br_address"));
				branch.setBr_tel(rs.getString("br_tel"));
				branch.setUser(user);
				
				list.add(branch);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbManager.release(pstmt, rs);
		}
		
		return list;
	}
	
}
















