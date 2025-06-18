package com.olive.common.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.olive.common.exception.BranchException;
import com.olive.common.model.Branch;
import com.olive.common.model.Brand;
import com.olive.common.model.Category;
import com.olive.common.model.CategoryDetail;
import com.olive.common.model.Product;
import com.olive.common.model.ProductOption;
import com.olive.common.model.Stock;
import com.olive.common.model.User;
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
	
	// 한 개의 레코드 삽입
	public void insert(Branch branch) throws BranchException {
		Connection con = null;
		PreparedStatement pstmt = null;
		
		con = dbManager.getConnection();
		
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO"
				+ " branch(br_name, br_address, br_tel, user_id)"
				+ " values(?, ?, ?, ?)");
		
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, branch.getBr_name());
			pstmt.setString(2, branch.getBr_address());
			pstmt.setString(3, branch.getBr_tel());
			pstmt.setInt(4, branch.getUser().getUser_id());
			
			int result = pstmt.executeUpdate();
			if(result < 1)
				throw new BranchException("지점 등록에 실패하였습니다");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new BranchException("지점 등록에 실패하였습니다", e);
		} finally {
			dbManager.release(pstmt);
		}
	}

	// 한 개의 레코드 수정
	public void update(Branch branch) throws BranchException {
		Connection con = null;
		PreparedStatement pstmt = null;
		
		con = dbManager.getConnection();
		
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE branch"
				+ " SET br_name = ?,"
				+ " br_address = ?,"
				+ " br_tel = ?,"
				+ " user_id = ?"
				+ " WHERE br_id = ?");
		
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, branch.getBr_name());
			pstmt.setString(2, branch.getBr_address());
			pstmt.setString(3, branch.getBr_tel());
			pstmt.setInt(4, branch.getUser().getUser_id());
			pstmt.setInt(5, branch.getBr_id());	
			
			int result = pstmt.executeUpdate();
			if(result < 1)
				throw new BranchException("지점 수정에 실패하였습니다");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new BranchException("지점 수정에 실패하였습니다", e);
		} finally {
			dbManager.release(pstmt);
		}
	}

	// 한 개의 레코드 삭제
	public void delete(Branch branch) throws BranchException {
		Connection con = null;
		PreparedStatement pstmt = null;
		
		con = dbManager.getConnection();
		
		StringBuffer sql = new StringBuffer();
		sql.append("DELETE"
				+ " FROM branch"
				+ " WHERE br_id = ?");
		
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, branch.getBr_id());	
			
			int result = pstmt.executeUpdate();
			if(result < 1)
				throw new BranchException("지점 삭제에 실패하였습니다");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new BranchException("지점 삭제에 실패하였습니다", e);
		} finally {
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
//			sql.append("select br_id as '등록 번호', br_name as '지점명', user_name as '담당자', br_address as '주소', br_tel as '연락처' from user u inner join branch b on u.user_id = b.user_id order by br_id");
			sql.append("SELECT br_id AS '등록 번호',"
					+ " br_name AS '지점명',"
					+ " user_name AS '담당자',"
					+ " br_address AS '주소',"
					+ " br_tel AS '연락처'"
					+ " FROM user u INNER JOIN branch b"
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
		sql.append("select br_name as '지점명', bd_name as '브랜드', ct_name as '상위 카테고리', ct_dt_name as '하위 카테고리', product_name as '상품명', st_quantity as '재고', st_update as '최근 수정일' from brand b inner join product p inner join product_option o inner join stock s inner join category c inner join category_detail cd inner join branch bh on bh.br_id=s.br_id and b.bd_id=p.bd_id and p.product_id=o.product_id and o.option_id=s.option_id and p.ct_dt_id=cd.ct_dt_id and c.ct_id=cd.ct_id and bh.br_name=?");
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
}
















