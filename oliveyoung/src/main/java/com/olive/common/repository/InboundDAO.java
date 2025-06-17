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
import com.olive.common.model.User;
import com.olive.common.util.DBManager;

public class InboundDAO {

    DBManager dbManager = DBManager.getInstance();
    
    public List<Bound> selectInbound() {
    	Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Bound> list = new ArrayList<>();

        StringBuffer sql = new StringBuffer();
        
        sql.append("select"
        		+ "		bd.request_date"
        		+ "	   ,bd.user_id"
        		+ "	   ,u.user_name"
        		+ "	   ,bo.bo_state_id"
        		+ "	   ,bo.bo_state_name"
        		+ "	   ,br.br_name"
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
            	bound.setRequest_date(rs.getDate("request_date"));
            	bound.setUser(user);
            	bound.setBranch(branch);
            	bound.setBoundState(boundState);

                list.add(bound);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbManager.release(pstmt, rs);
        }

        return list;
    }

    
    public List<Bound> selectInbound(Bound inbo) {
    	Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Bound> list = new ArrayList<>();

        StringBuffer sql = new StringBuffer();
        
        sql.append("select"
        		+ "		bd.request_date"
        		+ "	   ,bd.user_id"
        		+ "	   ,u.user_name"
        		+ "	   ,bo.bo_state_id"
        		+ "	   ,bo.bo_state_name"
        		+ "	   ,br.br_name"
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
            	bound.setRequest_date(rs.getDate("request_date"));
            	bound.setUser(user);
            	bound.setBranch(branch);
            	bound.setBoundState(boundState);

                list.add(bound);
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
}
