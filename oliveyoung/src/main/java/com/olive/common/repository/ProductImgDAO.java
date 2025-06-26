package com.olive.common.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.olive.common.exception.ProductImgException;
import com.olive.common.model.ProductImg;
import com.olive.common.model.ProductOption;
import com.olive.common.util.DBManager;

public class ProductImgDAO {
	DBManager dbManager = DBManager.getInstance();
	
	//하나의 제품에 딸려있는 이미지 등록 
	public void insert(ProductImg productImg, Connection con) throws ProductImgException {
	    PreparedStatement pstmt = null;

	    StringBuffer sql = new StringBuffer();
	    sql.append("insert into product_img(option_id, img_filename) values(?,?)");

	    try {
	        pstmt = con.prepareStatement(sql.toString());
	        pstmt.setInt(1, productImg.getProductOption().getOption_id());
	        pstmt.setString(2, productImg.getImg_filename());
	        int result = pstmt.executeUpdate();
	        if (result == 0) {
	            throw new ProductImgException("상품 이미지 등록 실패");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw new ProductImgException("상품 이미지 등록 실패", e);
	    } finally {
	        try {
	            if (pstmt != null) pstmt.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	}
	
	public ProductImg selectByOptionId(int optionId) throws ProductImgException {
	    ProductImg productImg = null;
	    Connection con = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;

	    String sql = "SELECT option_id, img_filename FROM product_img WHERE option_id = ?";

	    try {
	        con = dbManager.getConnection();
	        pstmt = con.prepareStatement(sql);
	        pstmt.setInt(1, optionId);
	        rs = pstmt.executeQuery();

	        while (rs.next()) {
	            productImg = new ProductImg();
	            productImg.setImg_filename(rs.getString("img_filename"));

	            ProductOption option = new ProductOption();
	            option.setOption_id(rs.getInt("option_id"));
	            productImg.setProductOption(option);
	        }

	    } catch (SQLException e) {
	        // Table doesn't exist 에러는 무시
	        if (e.getMessage().contains("doesn't exist")) {
	            System.out.println("[INFO] product_img 테이블이 존재하지 않아도 무시하고 진행합니다.");
	        } else {
	            e.printStackTrace();
	            throw new ProductImgException("상품 이미지 조회 실패", e);
	        }
	    } finally {
	        dbManager.release(pstmt, rs);
	    }

	    return productImg;
	}
}