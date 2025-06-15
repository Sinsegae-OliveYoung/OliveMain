package com.olive.common.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.olive.common.model.BoundState;
import com.olive.common.model.Inbound;
import com.olive.common.model.InboundProduct;
import com.olive.common.util.DBManager;

public class InboundDAO {

    DBManager dbManager = DBManager.getInstance();
    
    public List<Inbound> selectInbound(Inbound inbo) {
    	Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Inbound> list = new ArrayList<>();

        StringBuffer sql = new StringBuffer();
        sql.append("select  i.ib_date, i.ib_id, bo.bo_state_name from inbound i inner join Bound_state bo on bo.bo_state_id = i.bo_state_id");
        
        try {
            con = dbManager.getConnection();
            pstmt = con.prepareStatement(sql.toString());
            rs = pstmt.executeQuery();

            while (rs.next()) {
            	// BoundState 객체 생성
            	BoundState boundState = new BoundState();
            	boundState.setBo_state_name(rs.getString("bo_state_name"));
            	
                // Inbound 객체 생성
                Inbound inbound = new Inbound();
                inbound.setIb_id(rs.getInt("ib_id"));
                inbound.setIb_date(rs.getDate("ib_date"));
                inbound.setBoundState(boundState);
                
                // InboundProduct 객체 생성
                InboundProduct inboundProduct = new InboundProduct();
//                inboundProduct.setIb_pd_id(rs.getInt("ib_pd_id"));
                inboundProduct.setInbound(inbound);

                // Category 객체 생성
//                Category category = new Category();
//                category.setCt_code(rs.getString("ct_code"));
//                category.setCt_name(rs.getString("ct_name"));
//
//                // CategoryDetail 객체 생성 및 연결
//                CategoryDetail categoryDetail = new CategoryDetail();
//                categoryDetail.setCt_dt_name(rs.getString("ct_dt_name"));
//                categoryDetail.setCategory(category);
//
//                // Brand 객체 생성
//                Brand brand = new Brand();
//                brand.setBd_name(rs.getString("bd_name")); 
//
//                // Product 객체 생성 및 연결
//                Product product = new Product();
//                product.setProduct_name(rs.getString("product_name"));
//                product.setCategory(category);
//                product.setCategory_detail(categoryDetail);
//                product.setBrand(brand);
//
//                // ProductOption 객체 생성 및 연결
//                ProductOption productOption = new ProductOption();
//                productOption.setOption_id(rs.getInt("option_id"));
//                productOption.setOption_code(rs.getString("option_code"));
//                productOption.setOption_no(rs.getInt("option_no"));
//                productOption.setPrice(rs.getInt("price"));
//                productOption.setProduct(product);
//
//                // Branch 객체 생성 및 연결
//                Branch branch = new Branch();
//                branch.setBr_id(rs.getInt("br_id"));



                list.add(inbound);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbManager.release(pstmt, rs);
        }

        return list;
    }

    public void insertInbound(int user_id, java.sql.Date ib_date, String comment, List<InboundProduct> products) {
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
            for (InboundProduct inboundProduct : products) {
                pstmt.setInt(1, ib_id);
                pstmt.setInt(2, inboundProduct.getProductOption().getOption_id());
                pstmt.setInt(3, inboundProduct.getIb_pd_count());
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
