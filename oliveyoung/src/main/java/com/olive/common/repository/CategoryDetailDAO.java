package com.olive.common.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.olive.common.model.Brand;
import com.olive.common.model.Category;
import com.olive.common.model.CategoryDetail;
import com.olive.common.util.DBManager;

public class CategoryDetailDAO {

    DBManager dbManager = DBManager.getInstance();

    public List<CategoryDetail> selectAll() {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<CategoryDetail> list = new ArrayList<>();

        try {
            con = dbManager.getConnection();
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT cd.ct_dt_id, cd.ct_dt_code, cd.ct_dt_name, cd.ct_id, c.ct_code, c.ct_name ");
            sql.append("FROM category_detail cd ");
            sql.append("JOIN category c ON cd.ct_id = c.ct_id");
            pstmt = con.prepareStatement(sql.toString());
            rs = pstmt.executeQuery();

            while (rs.next()) {
                CategoryDetail categoryDetail = new CategoryDetail();
                categoryDetail.setCt_dt_id(rs.getInt("ct_dt_id"));
                categoryDetail.setCt_dt_code(rs.getString("ct_dt_code"));
                categoryDetail.setCt_dt_name(rs.getString("ct_dt_name"));

                Category category = new Category();
                category.setCt_id(rs.getInt("ct_id"));
                category.setCt_code(rs.getString("ct_code"));
                category.setCt_name(rs.getString("ct_name"));

                categoryDetail.setCategory(category);
                list.add(categoryDetail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbManager.release(pstmt, rs);
        }

        return list;
    }

    
    public int insert(CategoryDetail cd) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int result = 0;

        try {
            con = dbManager.getConnection();
            String sql = "INSERT INTO category_detail (ct_dt_name, ct_dt_code, ct_id) VALUES (?, ?, ?)";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, cd.getCt_dt_name());
            pstmt.setString(2, cd.getCt_dt_code());
            pstmt.setInt(3, cd.getCategory().getCt_id());

            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbManager.release(pstmt, rs);
        }

        return result;
    }
    
    public List<CategoryDetail> selectByCategoryId(int ct_id) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<CategoryDetail> list = new ArrayList<>();

        try {
            con = dbManager.getConnection();
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT cd.ct_dt_id, cd.ct_dt_code, cd.ct_dt_name, cd.ct_id, c.ct_code, c.ct_name ");
            sql.append("FROM category_detail cd ");
            sql.append("JOIN category c ON cd.ct_id = c.ct_id ");
            sql.append("WHERE cd.ct_id = ?");
            pstmt = con.prepareStatement(sql.toString());
            pstmt.setInt(1, ct_id);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                CategoryDetail categoryDetail = new CategoryDetail();
                categoryDetail.setCt_dt_id(rs.getInt("ct_dt_id"));
                categoryDetail.setCt_dt_code(rs.getString("ct_dt_code"));
                categoryDetail.setCt_dt_name(rs.getString("ct_dt_name"));

                Category category = new Category();
                category.setCt_id(rs.getInt("ct_id"));
                category.setCt_code(rs.getString("ct_code"));
                category.setCt_name(rs.getString("ct_name"));

                categoryDetail.setCategory(category);
                list.add(categoryDetail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbManager.release(pstmt, rs);
        }

        return list;
    }
    
    public CategoryDetail selectByCategoryDetailId(int ct_dt_id) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<CategoryDetail> list = new ArrayList<>();
        CategoryDetail categoryDetail = null;

        try {
            con = dbManager.getConnection();
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT cd.ct_dt_id, cd.ct_dt_code, cd.ct_dt_name, cd.ct_id, c.ct_code, c.ct_name ");
            sql.append("FROM category_detail cd ");
            sql.append("JOIN category c ON cd.ct_id = c.ct_id ");
            sql.append("WHERE cd.ct_dt_id = ?");
            pstmt = con.prepareStatement(sql.toString());
            pstmt.setInt(1, ct_dt_id);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                categoryDetail = new CategoryDetail();
                categoryDetail.setCt_dt_id(rs.getInt("ct_dt_id"));
                categoryDetail.setCt_dt_code(rs.getString("ct_dt_code"));
                categoryDetail.setCt_dt_name(rs.getString("ct_dt_name"));

                Category category = new Category();
                category.setCt_id(rs.getInt("ct_id"));
                category.setCt_code(rs.getString("ct_code"));
                category.setCt_name(rs.getString("ct_name"));

                categoryDetail.setCategory(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbManager.release(pstmt, rs);
        }

        return categoryDetail;
    }
    
    public List<CategoryDetail> load() {
	    return selectAll(); // selectAll()을 통해 전체 목록을 반환
	}

} 