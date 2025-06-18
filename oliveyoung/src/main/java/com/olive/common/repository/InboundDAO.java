package com.olive.common.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
    
    public List<BoundProduct> selectInbound() {
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

    public void insertInbound(int user_id, java.sql.Date ib_date, String comment, List<BoundProduct> products) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String inboundSql = "INSERT INTO inbound (user_id, ib_date, comment, bo_state_id) VALUES (?, ?, ?, 1)";
        String inboundProductSql = "INSERT INTO inbound_product (ib_id, option_id, ib_pd_count) VALUES (?, ?, ?)";

        try {
            con = dbManager.getConnection();
            con.setAutoCommit(false); // 트랜잭션 시작

            // inbound insert
            pstmt = con.prepareStatement(inboundSql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, user_id);
            pstmt.setDate(2, ib_date);
            pstmt.setString(3, comment);
            pstmt.executeUpdate();

            rs = pstmt.getGeneratedKeys();
            int ib_id = 0;
            if (rs.next()) {
                ib_id = rs.getInt(1);
            }
            rs.close();
            pstmt.close();

            // inbound_product insert
            pstmt = con.prepareStatement(inboundProductSql);
            for (BoundProduct boundProduct : products) {
                pstmt.setInt(1, ib_id);
                pstmt.setInt(2, boundProduct.getProductOption().getOption_id());
                pstmt.setInt(3, boundProduct.getB_count());
                pstmt.addBatch();
            }
            pstmt.executeBatch();

            con.commit();
        } catch (SQLException e) {
            try { if (con != null) con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
        } finally {
            dbManager.release(pstmt, rs);
            try { if (con != null) con.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
    
    public List<BoundProduct> selectBoundProductListByBoundId(int boundId) {
    	Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<BoundProduct> list = new ArrayList<>();

        StringBuffer sql = new StringBuffer();

        sql.append("SELECT bp.b_pd_id"
        		+ ", bp.b_count"
        		+ ", po.option_no"
        		+ ", po.option_code"
        		+ ", po.price"
        		+ ", po.option_name"
        		+ ", p.product_id"
        		+ ", p.product_name"
        		+ ", b.bound_id"
        		+ ", b.request_date"
        		+ ", b.comment "
        		+ "FROM bound_product bp "
        		+ "JOIN product_option po ON bp.option_id = po.option_id "
        		+ "JOIN product p ON po.product_id = p.product_id "
        		+ "JOIN bound b ON bp.bound_id = b.bound_id "
        		+ "WHERE bp.bound_id = ?"
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

                // Bound 생성 (필요한 정보만)
                Bound bound = new Bound();
                bound.setBound_id(rs.getInt("bound_id"));
                bound.setRequest_date(rs.getDate("request_date"));
                bound.setComment(rs.getString("comment"));

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
