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

public class StockDAO {

    DBManager dbManager = DBManager.getInstance();

    public List<Stock> listNow() {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Stock> list = new ArrayList<>();

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT po.option_code, ct.ct_code, cd.ct_dt_code, p.product_name, b.bd_name, po.price, s.st_id, s.st_quantity, s.st_update,");
        sql.append(" po.option_id, br.br_id FROM stock s "); // 예시 필드
        sql.append("JOIN product_option po ON s.option_id = po.option_id ");
        sql.append("JOIN product p ON po.product_id = p.product_id ");
        sql.append("JOIN brand b ON p.bd_id = b.bd_id ");
        sql.append("JOIN branch br ON s.br_id = br.br_id ");
        sql.append("JOIN category_detail cd ON p.ct_dt_id = cd.ct_dt_id ");
        sql.append("JOIN category ct ON cd.ct_id = ct.ct_id ");
        sql.append("order by s.st_update ");
        
        try {
            con = dbManager.getConnection();
            pstmt = con.prepareStatement(sql.toString());
            rs = pstmt.executeQuery();

            while (rs.next()) {
                // Stock 객체 생성
                Stock stock = new Stock();
                stock.setSt_id(rs.getInt("st_id"));
                stock.setSt_quantity(rs.getInt("st_quantity"));
                stock.setSt_update(rs.getDate("st_update"));

                // Category 객체 생성
                Category category = new Category();
                category.setCt_code(rs.getString("ct_code"));

                // CategoryDetail 객체 생성 및 연결
                CategoryDetail categoryDetail = new CategoryDetail();
                categoryDetail.setCt_dt_code(rs.getString("ct_dt_code"));
                categoryDetail.setCategory(category);

                // Brand 객체 생성
                Brand brand = new Brand();
                brand.setBd_name(rs.getString("bd_name")); 

                // Product 객체 생성 및 연결
                Product product = new Product();
                product.setProduct_name(rs.getString("product_name"));
                product.setCategory_detail(categoryDetail);
                product.setBrand(brand);

                // ProductOption 객체 생성 및 연결
                ProductOption productOption = new ProductOption();
                productOption.setOption_id(rs.getInt("option_id"));
                productOption.setOption_code(rs.getString("option_code"));
                productOption.setPrice(rs.getInt("price"));
                productOption.setProduct(product);

                // Branch 객체 생성 및 연결
                Branch branch = new Branch();
                branch.setBr_id(rs.getInt("br_id"));

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
   
    public List<Stock> listCat(Category category) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Stock> list = new ArrayList<>();

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT po.option_code, ct.ct_code, cd.ct_dt_code, p.product_name, b.bd_name, po.price, s.st_id, s.st_quantity, s.st_update,");
        sql.append(" po.option_id, br.br_id FROM stock s "); // 예시 필드
        sql.append("JOIN product_option po ON s.option_id = po.option_id ");
        sql.append("JOIN product p ON po.product_id = p.product_id ");
        sql.append("JOIN brand b ON p.bd_id = b.bd_id ");
        sql.append("JOIN branch br ON s.br_id = br.br_id ");
        sql.append("JOIN category_detail cd ON p.ct_dt_id = cd.ct_dt_id ");
        sql.append("JOIN category ct ON cd.ct_id = ct.ct_id ");
        sql.append("where ct.ct_name = ?");
        sql.append("order by s.st_update ");
        
        try {
            con = dbManager.getConnection();
            pstmt = con.prepareStatement(sql.toString());
            pstmt.setString(1, category.getCt_name());
            rs = pstmt.executeQuery();

            while (rs.next()) {
                // Stock 객체 생성
                Stock stock = new Stock();
                stock.setSt_id(rs.getInt("st_id"));
                stock.setSt_quantity(rs.getInt("st_quantity"));
                stock.setSt_update(rs.getDate("st_update"));

                // Category 객체 주입
//                Category category = new Category();
//                category.setCt_code(rs.getString("ct_code"));

                // CategoryDetail 객체 생성 및 연결
                CategoryDetail categoryDetail = new CategoryDetail();
                categoryDetail.setCt_dt_code(rs.getString("ct_dt_code"));
                categoryDetail.setCategory(category);

                // Brand 객체 생성
                Brand brand = new Brand();
                brand.setBd_name(rs.getString("bd_name")); 

                // Product 객체 생성 및 연결
                Product product = new Product();
                product.setProduct_name(rs.getString("product_name"));
                product.setCategory_detail(categoryDetail);
                product.setBrand(brand);

                // ProductOption 객체 생성 및 연결
                ProductOption productOption = new ProductOption();
                productOption.setOption_id(rs.getInt("option_id"));
                productOption.setOption_code(rs.getString("option_code"));
                productOption.setPrice(rs.getInt("price"));
                productOption.setProduct(product);

                // Branch 객체 생성 및 연결
                Branch branch = new Branch();
                branch.setBr_id(rs.getInt("br_id"));

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
    
    public List<Stock> listCountAlert() {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Stock> list = new ArrayList<>();

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT po.option_code, ct.ct_code, cd.ct_dt_code, p.product_name, b.bd_name, po.price, s.st_id, s.st_quantity, s.st_update,");
        sql.append(" po.option_id, br.br_id FROM stock s "); // 예시 필드
        sql.append("JOIN product_option po ON s.option_id = po.option_id ");
        sql.append("JOIN product p ON po.product_id = p.product_id ");
        sql.append("JOIN brand b ON p.bd_id = b.bd_id ");
        sql.append("JOIN branch br ON s.br_id = br.br_id ");
        sql.append("JOIN category_detail cd ON p.ct_dt_id = cd.ct_dt_id ");
        sql.append("JOIN category ct ON cd.ct_id = ct.ct_id ");
        sql.append("where s.st_quantity between 1 and 10 ");
        sql.append("order by s.st_update ");
        
        try {
            con = dbManager.getConnection();
            pstmt = con.prepareStatement(sql.toString());
            rs = pstmt.executeQuery();

            while (rs.next()) {
                // Stock 객체 생성
                Stock stock = new Stock();
                stock.setSt_id(rs.getInt("st_id"));
                stock.setSt_quantity(rs.getInt("st_quantity"));
                stock.setSt_update(rs.getDate("st_update"));

                // Category 객체 생성
                Category category = new Category();
                category.setCt_code(rs.getString("ct_code"));

                // CategoryDetail 객체 생성 및 연결
                CategoryDetail categoryDetail = new CategoryDetail();
                categoryDetail.setCt_dt_code(rs.getString("ct_dt_code"));
                categoryDetail.setCategory(category);

                // Brand 객체 생성
                Brand brand = new Brand();
                brand.setBd_name(rs.getString("bd_name")); 

                // Product 객체 생성 및 연결
                Product product = new Product();
                product.setProduct_name(rs.getString("product_name"));
                product.setCategory_detail(categoryDetail);
                product.setBrand(brand);

                // ProductOption 객체 생성 및 연결
                ProductOption productOption = new ProductOption();
                productOption.setOption_id(rs.getInt("option_id"));
                productOption.setOption_code(rs.getString("option_code"));
                productOption.setPrice(rs.getInt("price"));
                productOption.setProduct(product);

                // Branch 객체 생성 및 연결
                Branch branch = new Branch();
                branch.setBr_id(rs.getInt("br_id"));

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
    
    public List<Stock> listOldAlert() {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Stock> list = new ArrayList<>();

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT po.option_code, ct.ct_code, cd.ct_dt_code, p.product_name, b.bd_name, po.price, s.st_id, s.st_quantity, s.st_update,");
        sql.append(" po.option_id, br.br_id FROM stock s "); 
        sql.append("JOIN product_option po ON s.option_id = po.option_id ");
        sql.append("JOIN product p ON po.product_id = p.product_id ");
        sql.append("JOIN brand b ON p.bd_id = b.bd_id ");
        sql.append("JOIN branch br ON s.br_id = br.br_id ");
        sql.append("JOIN category_detail cd ON p.ct_dt_id = cd.ct_dt_id ");
        sql.append("JOIN category ct ON cd.ct_id = ct.ct_id ");
        sql.append("where s.st_update < DATE_SUB(CURDATE(), INTERVAL 1 YEAR) ");
        sql.append("order by s.st_update ");
        
        try {
            con = dbManager.getConnection();
            pstmt = con.prepareStatement(sql.toString());
            rs = pstmt.executeQuery();

            while (rs.next()) {
                // Stock 객체 생성
                Stock stock = new Stock();
                stock.setSt_id(rs.getInt("st_id"));
                stock.setSt_quantity(rs.getInt("st_quantity"));
                stock.setSt_update(rs.getDate("st_update"));

                // Category 객체 생성
                Category category = new Category();
                category.setCt_code(rs.getString("ct_code"));

                // CategoryDetail 객체 생성 및 연결
                CategoryDetail categoryDetail = new CategoryDetail();
                categoryDetail.setCt_dt_code(rs.getString("ct_dt_code"));
                categoryDetail.setCategory(category);

                // Brand 객체 생성
                Brand brand = new Brand();
                brand.setBd_name(rs.getString("bd_name")); 

                // Product 객체 생성 및 연결
                Product product = new Product();
                product.setProduct_name(rs.getString("product_name"));
                product.setCategory_detail(categoryDetail);
                product.setBrand(brand);

                // ProductOption 객체 생성 및 연결
                ProductOption productOption = new ProductOption();
                productOption.setOption_id(rs.getInt("option_id"));
                productOption.setOption_code(rs.getString("option_code"));
                productOption.setPrice(rs.getInt("price"));
                productOption.setProduct(product);

                // Branch 객체 생성 및 연결
                Branch branch = new Branch();
                branch.setBr_id(rs.getInt("br_id"));

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