package com.olive.common.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.olive.common.util.DBManager;
import com.olive.common.exception.ProductOptionException;
import com.olive.common.model.*;

public class ProductOptionDAO {

    DBManager dbManager = DBManager.getInstance();

    public List<ProductOption> selectAllWithDetails(User user) {
        List<ProductOption> list = new ArrayList<>();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql =
                "SELECT c.ct_name, c.ct_code, c.ct_id, cd.ct_dt_name, cd.ct_dt_code, cd.ct_dt_id, b.bd_name, b.bd_id, b.bd_code, p.product_id, p.product_name, " +
                "       CASE WHEN po.option_no = 99 THEN '-' ELSE po.option_name END AS option_name, " +
                "       po.option_id, po.option_no, po.option_code, po.price, po.option_active, " +
                "       COALESCE((SELECT SUM(s.st_quantity) " +
                "                 FROM stock s " +
                "                 WHERE s.option_id = po.option_id AND s.br_id = ?), 0) AS st_quantity " +
                "FROM product p " +
                "INNER JOIN product_option po ON p.product_id = po.product_id " +
                "INNER JOIN category c ON p.ct_id = c.ct_id " +
                "INNER JOIN category_detail cd ON p.ct_dt_id = cd.ct_dt_id AND c.ct_id = cd.ct_id " +
                "INNER JOIN brand b ON p.bd_id = b.bd_id " +
                "ORDER BY p.product_name";

        try {
            con = dbManager.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, getBranchID(user));
            rs = pstmt.executeQuery();

            while (rs.next()) {
                // 조립
                Category category = new Category();
                category.setCt_code(rs.getString("ct_code"));
                category.setCt_name(rs.getString("ct_name"));
                category.setCt_id(rs.getInt("ct_id"));

                CategoryDetail categoryDetail = new CategoryDetail();
                categoryDetail.setCt_dt_code(rs.getString("ct_dt_code"));
                categoryDetail.setCt_dt_name(rs.getString("ct_dt_name"));
                categoryDetail.setCt_dt_id(rs.getInt("ct_dt_id"));
                categoryDetail.setCategory(category);

                Brand brand = new Brand();
                brand.setBd_code(rs.getString("bd_code"));
                brand.setBd_name(rs.getString("bd_name"));
                brand.setBd_id(rs.getInt("bd_id"));

                Product product = new Product();
                product.setProduct_id(rs.getInt("product_id"));
                product.setProduct_name(rs.getString("product_name"));
                product.setCategory(category);
                product.setCategory_detail(categoryDetail);
                product.setBrand(brand);

                ProductOption option = new ProductOption();
                option.setOption_id(rs.getInt("option_id"));
                option.setOption_no(rs.getInt("option_no"));
                option.setOption_name(rs.getString("option_name"));
                option.setOption_code(rs.getString("option_code"));
                option.setPrice(rs.getInt("price"));
                option.setOption_active(rs.getString("option_active"));
                option.setProduct(product);

                list.add(option);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbManager.release(pstmt, rs);
        }

        return list;
    }
    
    // 로그인한 user가 관리하는 branch 목록 반환
 	public int getBranchID(User user){
 		
 		Connection con = null;
 		PreparedStatement pstmt = null;
 		ResultSet rs = null;
 		int resultID = -1;
 		
 		con = dbManager.getConnection();
 		
 		StringBuffer sql = new StringBuffer();
 		sql.append("select b.br_id");
 		sql.append(" from branch b");
 		sql.append(" inner join member m");
 		sql.append(" join user u");
 		sql.append(" join role r");
 		sql.append(" on b.br_id = m.br_id");
 		sql.append(" and u.user_id = m.user_id");
 		sql.append(" and u.role_id = r.role_id");
 		sql.append(" where m.user_id = ?");
 	
 		try {
 			pstmt = con.prepareStatement(sql.toString());
 			pstmt.setInt(1, user.getUser_id());  
 			rs = pstmt.executeQuery();
 			
 			while(rs.next()) {
 				resultID = rs.getInt("b.br_id");
 			}
 		} catch (SQLException e) {
 			e.printStackTrace();
 		} finally {
 			dbManager.release(pstmt, rs);
 		}
 		
 		return resultID;
 	}
 	
 	public int selectRecentPk() {
 		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		int pk=0;
		
		con=dbManager.getConnection();
		
		StringBuffer sql = new StringBuffer();
		sql.append("select last_insert_id() as option_id");
		
		try {
			pstmt=con.prepareStatement(sql.toString());
			rs=pstmt.executeQuery(); //쿼리실행 및 결과표 반환.
			
			if(rs.next()) { //조회된 결과가 있다면..
				pk=rs.getInt("option_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			dbManager.release(pstmt, rs);
		}
		return pk;
 	}
 	
 	public int selectMaxOptionNo(int product_id) {
 	    int max = 0;
 	    Connection con = null;
 	    PreparedStatement pstmt = null;
 	    ResultSet rs = null;

 	    String sql = "SELECT MAX(option_no) FROM product_option WHERE product_id = ? AND option_active = 'y'";

 	    try {
 	        con = dbManager.getConnection();
 	        pstmt = con.prepareStatement(sql);
 	        pstmt.setInt(1, product_id);
 	        rs = pstmt.executeQuery();
 	        if (rs.next()) {
 	            max = rs.getInt(1); // 없으면 0 반환
 	        }
 	    } catch (SQLException e) {
 	        e.printStackTrace();
 	    } finally {
 	        dbManager.release(pstmt, rs);
 	    }

 	    return max;
 	}
 	
 	public void insert(ProductOption productOption, Connection con) throws ProductOptionException {
		PreparedStatement pstmt=null;
		
		StringBuffer sql = new StringBuffer();
		sql.append("insert into product_option(product_id, option_no, option_name,"
				+ " option_code, price, option_active) values(?,?,?,?,?,?)");
	
		try {
			pstmt=con.prepareStatement(sql.toString());
			pstmt.setInt(1, productOption.getProduct().getProduct_id());
			pstmt.setInt(2, productOption.getOption_no());
			pstmt.setString(3, productOption.getOption_name());
			pstmt.setString(4, productOption.getOption_code());
			pstmt.setInt(5, productOption.getPrice());
			pstmt.setString(6, productOption.getOption_active());
			int result=pstmt.executeUpdate();//DML 실행
			if (result == 0) {
				throw new ProductOptionException("상품의 색상이 등록되지 않았어요");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ProductOptionException("상품의 색상 등록시 문제가 발생하였습니다", e);
		}finally {
			dbManager.release(pstmt);
		}
 	}


	 // 상품 옵션 수정
	 public void update(ProductOption option, Connection con){
		PreparedStatement pstmt=null;
		StringBuffer sql = new StringBuffer();
	     sql.append("UPDATE product_option SET option_name = ?, price = ?, option_active = ? WHERE option_id = ?");
	     try {
    		 pstmt = con.prepareStatement(sql.toString()); 
	         pstmt.setString(1, option.getOption_name());
	         pstmt.setInt(2, option.getPrice());
	         pstmt.setString(3, option.getOption_active());
	         pstmt.setInt(4, option.getOption_id());
	         pstmt.executeUpdate();
	     } catch (SQLException e) {
	    	 e.printStackTrace();
	     } finally {
	    	 dbManager.release(pstmt);
	     }
	 }
	
	 // 상품 옵션 삭제
	 public void delete(int optionId ,Connection con) throws ProductOptionException, SQLException {
	    PreparedStatement pstmt1 = null;
	    PreparedStatement pstmt2 = null;
	    PreparedStatement pstmt3 = null;
	    try {
	        
	        // 1. 자식 테이블 먼저 삭제
	        String sql1 = "DELETE FROM bound_product WHERE option_id = ?";
	        pstmt1 = con.prepareStatement(sql1);
	        pstmt1.setInt(1, optionId);
	        pstmt1.executeUpdate();
	        
	        // 2. 부모 테이블 삭제
	        String sql2 = "DELETE FROM stock WHERE option_id = ?";
	        pstmt2 = con.prepareStatement(sql2);
	        pstmt2.setInt(1, optionId);
	        pstmt2.executeUpdate();
	        
	        // 3. 부모 테이블 삭제
	        String sql3 = "DELETE FROM product_option WHERE option_id = ?";
	        pstmt3 = con.prepareStatement(sql3);
	        pstmt3.setInt(1, optionId);
	        pstmt3.executeUpdate();
	        
	        con.commit();
	    } catch (SQLException e) {
	        con.rollback();
	        throw new ProductOptionException("상품 옵션이 삭제되지 않았어요");
	    } finally {
	        dbManager.release(pstmt1);
	        dbManager.release(pstmt2);
	        dbManager.release(pstmt3);
	    }
	 }
} 
