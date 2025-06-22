package com.olive.common.repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.olive.common.exception.BoundException;
import com.olive.common.model.Bound;
import com.olive.common.model.BoundProduct;
import com.olive.common.model.BoundState;
import com.olive.common.model.Branch;
import com.olive.common.model.Brand;
import com.olive.common.model.Category;
import com.olive.common.model.CategoryDetail;
import com.olive.common.model.Product;
import com.olive.common.model.ProductOption;
import com.olive.common.model.Stock;
import com.olive.common.model.User;
import com.olive.common.util.DBManager;

public class InboundDAO {

    DBManager dbManager = DBManager.getInstance();
    
    
    public List<BoundProduct> selectInboundByBranches(List<Branch> branchList) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<BoundProduct> list = new ArrayList<>();

        // br_id 리스트를 쿼리 IN 절에 넣기 위한 처리
        StringBuilder branchIds = new StringBuilder();
        for (int i = 0; i < branchList.size(); i++) {
            branchIds.append(branchList.get(i).getBr_id());
            if (i < branchList.size() - 1) branchIds.append(", ");
        }

        StringBuffer sql = new StringBuffer();
        sql.append("select"
        		+ "		bd.request_date"
        		+ "	   ,bd.user_id"
        		+ "	   ,bd.approver_id"
        		+ "	   ,au.user_name 	as approver_name"
        		+ "	   ,u.user_id"
        		+ "	   ,u.user_name"
        		+ "	   ,bo.bo_state_id"
        		+ "	   ,bo.bo_state_name"
        		+ "	   ,br.br_name"
        		+ "	   ,bd.bound_id"
        		+ "	   ,bd.comment"
        		+ " from 	bound bd"
        		+ " inner join Bound_state bo 	on bo.bo_state_id = bd.bo_state_id"
        		+ " inner join user u			on u.user_id = bd.user_id"
        		+ " inner join branch br 		on br.br_id  = bd.br_id"
        		+ " LEFT JOIN user au 			ON au.user_id = bd.approver_id" // 결재자(user) 테이블 다시 조인
        		+ " WHERE bd.bound_flag = 'in'"
                + " AND bd.br_id IN (" + branchIds.toString() + ")"
        		+ " order by bd.request_date desc, bd.bound_id DESC"
        );
        

        try {
            con = dbManager.getConnection();
            pstmt = con.prepareStatement(sql.toString());
            rs = pstmt.executeQuery();

            while (rs.next()) {
            	// User 객체 생성
            	User user = new User();
            	user.setUser_id(rs.getInt("user_id"));
            	user.setUser_name(rs.getString("user_name"));
            	
            	User approver = new User();
            	approver.setUser_id(rs.getInt("approver_id"));
            	approver.setUser_name(rs.getString("approver_name"));
            	
            	
            	// Branch 객체 생성
            	Branch branch = new Branch();
            	branch.setBr_name(rs.getString("br_name"));
            	
            	// BoundState 객체 생성
            	BoundState boundState = new BoundState();
            	boundState.setBo_state_name(rs.getString("bo_state_name"));
            	
                // Inbound 객체 생성
            	Bound bound = new Bound();
            	bound.setBound_id(rs.getInt("bound_id"));
            	bound.setRequest_date(rs.getDate("request_date"));
            	bound.setComment(rs.getString("comment"));
            	bound.setUser(user);
            	bound.setApprover(approver);
            	bound.setBranch(branch);
            	bound.setBoundState(boundState);
            	
            	BoundProduct boundProduct = new BoundProduct();
            	boundProduct.setBound(bound);

                list.add(boundProduct);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbManager.release(pstmt, rs);
        }

        return list;
    }

    public List<BoundProduct> selectBoundProductListByBoundId(int boundId) {
    	Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<BoundProduct> list = new ArrayList<>();

        StringBuffer sql = new StringBuffer();

        sql.append("SELECT "
        		+ "		  bp.b_pd_id"
        		+ "		, bp.b_count"
        		+ "		, po.option_no"
        		+ "		, po.option_code"
        		+ "		, po.price"
        		+ "		, po.option_name"
        		+ "		, p.product_id"
        		+ "		, p.product_name"
        		+ "		, b.bound_id"
        		+ "		, b.request_date"
        		+ "		, b.comment "
        		+ "		, br.br_id"
        		+ "		, br.br_name "
        		+ "FROM   	  bound_product bp "
        		+ "INNER JOIN product_option po ON bp.option_id = po.option_id "
        		+ "INNER JOIN product p 		ON po.product_id = p.product_id "
        		+ "INNER JOIN bound b 			ON bp.bound_id = b.bound_id "
        		+ "INNER JOIN branch br			ON br.br_id  = b.br_id "
        		+ "WHERE  bp.bound_id = ?"
        );
        
        
        try {
        	con = dbManager.getConnection();
            pstmt = con.prepareStatement(sql.toString());
            pstmt.setInt(1, boundId);
            rs = pstmt.executeQuery();

            while (rs.next()) {

                // Product 생성
                Product product = new Product();
                product.setProduct_id(rs.getInt("product_id"));
                product.setProduct_name(rs.getString("product_name"));
                // 필요한 필드 추가

                // ProductOption 생성
                ProductOption option = new ProductOption();
                option.setOption_no(rs.getInt("option_no"));
                option.setOption_code(rs.getString("option_code"));
                option.setOption_name(rs.getString("option_name"));
                option.setPrice(rs.getInt("price"));
                option.setProduct(product);

                Branch branch = new Branch();
                branch.setBr_id(rs.getInt("br_id"));
                branch.setBr_name(rs.getString("br_name"));
                
                // Bound 생성 (필요한 정보만)
                Bound bound = new Bound();
                bound.setBound_id(rs.getInt("bound_id"));
                bound.setRequest_date(rs.getDate("request_date"));
                bound.setComment(rs.getString("comment"));
                bound.setBranch(branch);

                // BoundProduct 생성
                BoundProduct bp = new BoundProduct();
                bp.setB_pd_id(rs.getInt("b_pd_id"));
                bp.setB_count(rs.getInt("b_count"));
                bp.setProductOption(option);
                bp.setBound(bound);
                

                list.add(bp);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbManager.release(pstmt, rs);
        }

        return list;
    }
    
 // 제품 리스트 & 선택된 요청서의 가져오기
    public List<BoundProduct> boundEditProduct(int bound_id) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<BoundProduct> list = new ArrayList<>();

        StringBuffer sql = new StringBuffer();
        
        sql.append(
        		  "SELECT"
        		  + "    c.ct_name,"
        		  + "    cd.ct_dt_name,"
        		  + "    b.bd_name,"
        		  + "    p.product_id,"
        		  + "    p.product_name,"
        		  + "    po.option_id,"
        		  + "    CASE WHEN po.option_no = 99 THEN '-' ELSE po.option_name END AS option_name,"
        		  + "    po.option_code,"
        		  + "    po.price,"
        		  + "    COALESCE(("
        		  + "        SELECT SUM(s.st_quantity)"
        		  + "        FROM stock s"
        		  + "        WHERE s.option_id = po.option_id"
        		  + "          AND s.br_id = bo.br_id"
        		  + "    ), 0) AS st_quantity,"
        		  + "    COALESCE(bp.b_count, 0) AS b_count"
        		  + " FROM product p"
        		  + " INNER JOIN product_option po ON p.product_id = po.product_id"
        		  + " INNER JOIN category c ON p.ct_id = c.ct_id"
        		  + " INNER JOIN category_detail cd ON p.ct_dt_id = cd.ct_dt_id AND c.ct_id = cd.ct_id"
        		  + " INNER JOIN brand b ON p.bd_id = b.bd_id"
        		  + " LEFT JOIN bound_product bp ON bp.option_id = po.option_id AND bp.bound_id = ?"
        		  + " JOIN bound bo ON bo.bound_id = ?"
        		  + " ORDER BY c.ct_id ASC, cd.ct_dt_id ASC"
        );
        
        try {
            con = dbManager.getConnection();
            pstmt = con.prepareStatement(sql.toString());
            pstmt.setInt(1, bound_id);
            pstmt.setInt(2, bound_id);
            rs = pstmt.executeQuery();

            while (rs.next()) {

                // Category 객체 생성
                Category category = new Category();
//                category.setCt_code(rs.getString("ct_code"));
                category.setCt_name(rs.getString("ct_name"));

                // CategoryDetail 객체 생성 및 연결
                CategoryDetail categoryDetail = new CategoryDetail();
                categoryDetail.setCt_dt_name(rs.getString("ct_dt_name"));
                categoryDetail.setCategory(category);

                // Brand 객체 생성
                Brand brand = new Brand();
                brand.setBd_name(rs.getString("bd_name"));

                // Product 객체 생성 및 연결
                Product product = new Product();
                product.setProduct_name(rs.getString("product_name"));
                product.setCategory(category);
                product.setCategory_detail(categoryDetail);
                product.setBrand(brand);

                // ProductOption 객체 생성 및 연결
                ProductOption productOption = new ProductOption();
                productOption.setOption_id(rs.getInt("option_id"));
                productOption.setOption_code(rs.getString("option_code"));
//                productOption.setOption_no(rs.getInt("option_no"));
                productOption.setOption_name(rs.getString("option_name"));
                productOption.setPrice(rs.getInt("price"));
                productOption.setProduct(product);

                // Branch 객체 생성 및 연결
                Branch branch = new Branch();
                branch = new Branch();
//                branch.setBr_name(rs.getString("br_name"));
                
                Bound bound = new Bound();
//                bound.setBound_id(rs.getInt("bound_id"));
                
                BoundProduct boundProduct = new BoundProduct();
                boundProduct.setBound(bound);
                boundProduct.setProductOption(productOption);
                boundProduct.setB_count(rs.getInt("b_count")); // ✅ 추가: 요청 수량 설정

                // Stock 객체 생성
                Stock stock = new Stock();
//                stock.setSt_id(rs.getInt("st_id"));
                stock.setSt_quantity(rs.getInt("st_quantity"));
                stock.setProductOption(productOption);
                stock.setBranch(branch);

                list.add(boundProduct);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbManager.release(pstmt, rs);
        }

        return list;
    }
    
    // 선택된 요청서의 지점의 재고 가져오기
    public List<Stock> selectStockForBound(int bound_id) {
        List<Stock> stockList = new ArrayList<>();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT s.option_id, SUM(s.st_quantity) AS st_quantity ");
        sql.append("FROM stock s ");
        sql.append("WHERE s.br_id = ( ");
        sql.append("    SELECT br_id ");
        sql.append("    FROM bound ");
        sql.append("    WHERE bound_id = ? ");
        sql.append(") ");
        sql.append("GROUP BY s.option_id");

        try {
            con = dbManager.getConnection();
            pstmt = con.prepareStatement(sql.toString());
            pstmt.setInt(1, bound_id);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Stock stock = new Stock();
                ProductOption po = new ProductOption();
                po.setOption_id(rs.getInt("option_id"));
                stock.setProductOption(po);
                stock.setSt_quantity(rs.getInt("st_quantity"));

                stockList.add(stock);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbManager.release(pstmt, rs);
        }

        return stockList;
    }
    
    // 요청서 신규 등록 - InboundRequestPanel
    public void insertInbound(int user_id, int managerId, int br_id, Date requestDate, String comment, List<BoundProduct> products) throws BoundException{
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        con = dbManager.getConnection();

        StringBuffer boundSql = new StringBuffer();
        StringBuffer boundProductSql = new StringBuffer();
        
        
        // Bound Table
        boundSql.append("INSERT INTO bound (user_id, approver_id, br_id, request_date, comment, bo_state_id, bound_flag) "
        		+ "VALUES (?, ?, ?, ?, ?, 1, 'in')");
        
        // BoundProduct Table
        boundProductSql.append("INSERT INTO bound_product (bound_id, option_id, b_count) VALUES (?, ?, ?)");

        try {
            con.setAutoCommit(false);
            // bound insert
            
            pstmt = con.prepareStatement(boundSql.toString(), Statement.RETURN_GENERATED_KEYS);

            pstmt.setInt(1, user_id);
            pstmt.setInt(2, managerId);
            pstmt.setInt(3, br_id);
            pstmt.setDate(4, requestDate);
            pstmt.setString(5, comment);
            
            int result = pstmt.executeUpdate();
			if(result < 1) {
				throw new BoundException("입고 요청서 등록에 실패하였습니다");				
			}

			// 생성된 bound_id 가져오기
	        rs = pstmt.getGeneratedKeys();
	        int bound_id = 0;
	        if (rs.next()) {
	            bound_id = rs.getInt(1);
	        } else {
	            throw new BoundException("입고 요청서 ID를 가져오지 못했습니다");
	        }

	        // 3. bound_product 테이블에 insert
	        pstmt = con.prepareStatement(boundProductSql.toString());
	        for (BoundProduct bp : products) {
	            pstmt.setInt(1, bound_id);
	            pstmt.setInt(2, bp.getProductOption().getOption_id());
	            pstmt.setInt(3, bp.getB_count());
	            pstmt.addBatch();
	        }
	        pstmt.executeBatch();

            con.commit();
        } catch (SQLException e) {
        	e.printStackTrace();
            try {
                if (con != null) con.rollback();
                throw new BoundException("입고 요청서 등록에 실패하였습니다");
            } catch (SQLException ex) {
                ex.printStackTrace();
                throw new BoundException("입고 요청서 등록에 실패하였습니다");
            }
        } finally {
            dbManager.release(pstmt, rs);
            try {
                if (con != null) con.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
                throw new BoundException("입고 요청서 등록에 실패하였습니다");
            }
        }
    }

    // 요청서 삭제 - InboundShowPanel
    public void deleteInbound(int boundId) {
    	Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = dbManager.getConnection();

            // 1. bound_product 테이블 먼저 삭제
            String sqlDeleteProduct = "DELETE FROM bound_product WHERE bound_id = ?";
            pstmt = con.prepareStatement(sqlDeleteProduct);
            pstmt.setInt(1, boundId);
            pstmt.executeUpdate();
            pstmt.close(); // 기존 pstmt 닫고 재사용

            // 2. bound 테이블 삭제
            String sqlDeleteBound = "DELETE FROM bound WHERE bound_id = ?";
            pstmt = con.prepareStatement(sqlDeleteBound);
            pstmt.setInt(1, boundId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbManager.release(pstmt);
        }    }
}
