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
import com.olive.common.model.Product;
import com.olive.common.model.ProductOption;
import com.olive.common.model.User;
import com.olive.common.util.DBManager;

public class InboundDAO {

    DBManager dbManager = DBManager.getInstance();
    
//    public List<BoundProduct> selectInbound() {
//    	Connection con = null;
//        PreparedStatement pstmt = null;
//        ResultSet rs = null;
//        List<BoundProduct> list = new ArrayList<>();
//
//        StringBuffer sql = new StringBuffer();
//        
//        sql.append("select"
//        		+ "		bd.request_date"
//        		+ "	   ,bd.user_id"
//        		+ "	   ,bd.approver_id"
//        		+ "	   ,au.user_name 	as approver_name"
//        		+ "	   ,u.user_id"
//        		+ "	   ,u.user_name"
//        		+ "	   ,bo.bo_state_id"
//        		+ "	   ,bo.bo_state_name"
//        		+ "	   ,br.br_name"
//        		+ "	   ,bd.bound_id"
//        		+ "	   ,bd.comment"
//        		+ " from 	bound bd"
//        		+ " inner join Bound_state bo 	on bo.bo_state_id = bd.bo_state_id"
//        		+ " inner join user u			on u.user_id = bd.user_id"
//        		+ " inner join branch br 		on br.br_id  = bd.br_id"
//        		+ " LEFT JOIN user au 			ON au.user_id = bd.approver_id" // 결재자(user) 테이블 다시 조인
//        		+ " where	1 = 1"
//        		+ " and 	bd.bound_flag = \"in\""
//        		+ " and		bd.br_id in (1, 2, 3, 4, 5)"
//        		+ " order by bd.request_date desc"
//        );
//        
//        try {
//            con = dbManager.getConnection();
//            pstmt = con.prepareStatement(sql.toString());
//            rs = pstmt.executeQuery();
//
//            while (rs.next()) {
//            	// User 객체 생성
//            	User user = new User();
//            	user.setUser_id(rs.getInt("user_id"));
//            	user.setUser_name(rs.getString("user_name"));
//            	
//            	User approver = new User();
//            	approver.setUser_id(rs.getInt("approver_id"));
//            	approver.setUser_name(rs.getString("approver_name"));
//            	
//            	
//            	// Branch 객체 생성
//            	Branch branch = new Branch();
//            	branch.setBr_name(rs.getString("br_name"));
//            	
//            	// BoundState 객체 생성
//            	BoundState boundState = new BoundState();
//            	boundState.setBo_state_name(rs.getString("bo_state_name"));
//            	
//                // Inbound 객체 생성
//            	Bound bound = new Bound();
//            	bound.setBound_id(rs.getInt("bound_id"));
//            	bound.setRequest_date(rs.getDate("request_date"));
//            	bound.setComment(rs.getString("comment"));
//            	bound.setUser(user);
//            	bound.setApprover(approver);
//            	bound.setBranch(branch);
//            	bound.setBoundState(boundState);
//            	
//            	BoundProduct boundProduct = new BoundProduct();
//            	boundProduct.setBound(bound);
//
//                list.add(boundProduct);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            dbManager.release(pstmt, rs);
//        }
//
//        return list;
//    }
    
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
        		+ " order by bd.request_date desc"
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

    
    public List<BoundProduct> selectInbound(BoundProduct inbo) {
    	Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<BoundProduct> list = new ArrayList<>();

        StringBuffer sql = new StringBuffer();
        
        sql.append("select"
        		+ "		bd.request_date"
        		+ "	   ,bd.user_id"
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
        		+ " where	1 = 1"
        		+ " and 	bd.bound_flag = \"in\""
        		+ " and		bd.br_id in (1, 2, 3, 4, 5)"
        		+ " order by bd.request_date desc")
        ;
        
        try {
            con = dbManager.getConnection();
            pstmt = con.prepareStatement(sql.toString());
            rs = pstmt.executeQuery();

            while (rs.next()) {
            	// User 객체 생성
            	User user = new User();
            	user.setUser_name(rs.getString("user_name"));
            	
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

            
//            pstmt = con.prepareStatement(boundSql.toString());
            pstmt.setInt(1, user_id);
            pstmt.setInt(2, managerId);
            pstmt.setInt(3, br_id);
            pstmt.setDate(4, requestDate);
            pstmt.setString(5, comment);
            
            int result = pstmt.executeUpdate();
			if(result < 1) {
				throw new BoundException("입고 요청서 등록에 실패하였습니다");				
			}

//            rs = pstmt.getGeneratedKeys();
//            int bound_id = 0;
//            if (rs.next()) {
//                bound_id = rs.getInt(1);
//            }
            
            // bound_product insert
//            pstmt = con.prepareStatement(boundProductSql);
//            for (BoundProduct bp : products) {
//                pstmt.setInt(1, bound_id);
//                pstmt.setInt(2, bp.getProductOption().getOption_id());
//                pstmt.setInt(3, bp.getB_count());
//                pstmt.addBatch();
//            }
//            pstmt.executeBatch();

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
}
