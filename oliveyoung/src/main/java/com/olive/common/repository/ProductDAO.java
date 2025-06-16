package com.olive.common.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.olive.common.model.Branch;
import com.olive.common.model.Brand;
import com.olive.common.model.Category;
import com.olive.common.model.CategoryDetail;
import com.olive.common.model.Product;
import com.olive.common.model.ProductOption;
import com.olive.common.model.Stock;
import com.olive.common.util.DBManager;

public class ProductDAO {

    DBManager dbManager = DBManager.getInstance();

    public List<Stock> selectNow() {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Stock> list = new ArrayList<>();

        StringBuffer sql = new StringBuffer();
//        sql.append("select  c.ct_name");
//		sql.append(" ,cd.ct_dt_name");
//		sql.append(" ,b.bd_name");
//		sql.append(" ,p.product_name");
//		sql.append(" ,po.option_code");
//		sql.append(" ,po.option_no");
//		sql.append(" ,po.price");
//		sql.append(" ,s.st_quantity");
//		sql.append(" ,br.br_name");
//		
//		sql.append(" ,s.st_id");
//		sql.append(" ,c.ct_code");
//		sql.append(" ,cd.ct_dt_code");
//		sql.append(" ,po.option_id");
//		sql.append(" ,br.br_id");
//		
//		sql.append(" from	product p");
//		sql.append(" inner join product_option po on p.product_id = po.product_id");
//		sql.append(" inner join category c on p.ct_id = c.ct_id");
//		sql.append(" inner join category_detail cd on p.ct_dt_id = cd.ct_dt_id and c.ct_id = cd.ct_id");
//		sql.append(" inner join brand b ON p.bd_id = b.bd_id");
//		sql.append(" inner join stock s on s.option_id = po.option_id");
//		sql.append(" inner join branch br on s.option_id = po.option_id");
//		sql.append(" and br.br_id = s.br_id");
//		sql.append(" where s.br_id = 1");
        sql.append("SELECT "
                + "c.ct_name, "
                + "cd.ct_dt_name, "
                + "b.bd_name, "
                + "p.product_name, "
//                + "po.option_name, "
                + "COALESCE(po.option_name, '-') AS option_name, "
                + "po.option_code, "
                + "po.price, "
                + "COALESCE( ("
                + "    SELECT SUM(st_quantity) "
                + "    FROM stock s "
                + "    JOIN branch br2 ON br2.br_id = s.br_id "
                + "    WHERE s.option_id = po.option_id "
                + "      AND br2.br_id = 1"
                + "), 0 ) AS st_quantity "
                + "FROM product p "
                + "INNER JOIN product_option po ON p.product_id = po.product_id "
                + "INNER JOIN category c ON p.ct_id = c.ct_id "
                + "INNER JOIN category_detail cd ON p.ct_dt_id = cd.ct_dt_id AND c.ct_id = cd.ct_id "
                + "INNER JOIN brand b ON p.bd_id = b.bd_id "
                + "ORDER BY p.product_name");
        
        try {
            con = dbManager.getConnection();
            pstmt = con.prepareStatement(sql.toString());
            rs = pstmt.executeQuery();

            while (rs.next()) {
                // Stock 객체 생성
                Stock stock = new Stock();
//                stock.setSt_id(rs.getInt("st_id"));
                stock.setSt_quantity(rs.getInt("st_quantity"));

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
//                productOption.setOption_id(rs.getInt("option_id"));
                productOption.setOption_code(rs.getString("option_code"));
//                productOption.setOption_no(rs.getInt("option_no"));
                productOption.setOption_name(rs.getString("option_name"));
                productOption.setPrice(rs.getInt("price"));
                productOption.setProduct(product);

                // Branch 객체 생성 및 연결
                Branch branch = new Branch();
//                branch.setBr_name(rs.getString("br_name"));

                // Stock 객체에 하위 객체 연결
                stock.setProductOption(productOption);
                stock.setBranch(branch);

                list.add(stock);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbManager.release(pstmt, rs);
        }

        return list;
    }
    
    public List<Stock> listNewBranch(Branch branch) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Stock> list = new ArrayList<>();

        StringBuffer sql = new StringBuffer();
        
        sql.append("SELECT "
                + "c.ct_name, "
                + "cd.ct_dt_name, "
                + "b.bd_name, "
                + "p.product_name, "
                + "COALESCE(po.option_name, '-') AS option_name, "
                + "po.option_code, "
                + "po.price, "
                + "COALESCE( ("
                + "    SELECT SUM(st_quantity) "
                + "    FROM stock s "
                + "    JOIN branch br2 ON br2.br_id = s.br_id "
                + "    WHERE s.option_id = po.option_id "
                + "      AND br2.br_name = ?"
                + "), 0 ) AS st_quantity "
                + "FROM product p "
                + "INNER JOIN product_option po ON p.product_id = po.product_id "
                + "INNER JOIN category c ON p.ct_id = c.ct_id "
                + "INNER JOIN category_detail cd ON p.ct_dt_id = cd.ct_dt_id AND c.ct_id = cd.ct_id "
                + "INNER JOIN brand b ON p.bd_id = b.bd_id "
                + "ORDER BY p.product_name");
        
        try {
            con = dbManager.getConnection();
            pstmt = con.prepareStatement(sql.toString());
            pstmt.setString(1, branch.getBr_name());
            rs = pstmt.executeQuery();

            while (rs.next()) {
                // Stock 객체 생성
                Stock stock = new Stock();
//                stock.setSt_id(rs.getInt("st_id"));
                stock.setSt_quantity(rs.getInt("st_quantity"));

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
//                productOption.setOption_id(rs.getInt("option_id"));
                productOption.setOption_code(rs.getString("option_code"));
//                productOption.setOption_no(rs.getInt("option_no"));
                productOption.setOption_name(rs.getString("option_name"));
                productOption.setPrice(rs.getInt("price"));
                productOption.setProduct(product);

                // Branch 객체 생성 및 연결
                branch = new Branch();
//                branch.setBr_name(rs.getString("br_name"));

                // Stock 객체에 하위 객체 연결
                stock.setProductOption(productOption);
                stock.setBranch(branch);

                list.add(stock);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbManager.release(pstmt, rs);
        }

        return list;
    }
}
