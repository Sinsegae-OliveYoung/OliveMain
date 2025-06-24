package com.olive.common.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.olive.common.model.Branch;
import com.olive.common.model.Brand;
import com.olive.common.model.Category;
import com.olive.common.model.CategoryDetail;
import com.olive.common.model.Product;
import com.olive.common.model.ProductOption;
import com.olive.common.model.Stock;
import com.olive.common.model.User;
import com.olive.common.util.DBManager;

public class StockDAO {

    DBManager dbManager = DBManager.getInstance();

    public List<Stock> listNow(User user) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Stock> list = new ArrayList<>();

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT po.option_code, ct.ct_name, cd.ct_dt_name, p.product_name, b.bd_name, po.price, " +
        	    "s.st_id, s.st_quantity, s.st_update, po.option_id, br.br_id " +
        	    "FROM stock s " +
        	    "JOIN product_option po ON s.option_id = po.option_id " +
        	    "JOIN product p ON po.product_id = p.product_id " +
        	    "JOIN brand b ON p.bd_id = b.bd_id " +
        	    "JOIN branch br ON s.br_id = br.br_id " +
        	    "JOIN category_detail cd ON p.ct_dt_id = cd.ct_dt_id " +
        	    "JOIN category ct ON cd.ct_id = ct.ct_id " +
        	    "WHERE br.br_id = ? " + // 로그인한 유저의 br_id로 대체
        	    "ORDER BY s.st_update");
        
        try {
            con = dbManager.getConnection();
            pstmt = con.prepareStatement(sql.toString());
            pstmt.setInt(1, getBranchID(user));
            rs = pstmt.executeQuery();

            while (rs.next()) {
                // Stock 객체 생성
                Stock stock = new Stock();
                stock.setSt_id(rs.getInt("st_id"));
                stock.setSt_quantity(rs.getInt("st_quantity"));
                stock.setSt_update(rs.getDate("st_update"));

                // Category 객체 생성
                Category category = new Category();
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
   
    public List<Stock> listCat(Category category, User user) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Stock> list = new ArrayList<>();

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT po.option_code, ct.ct_name, cd.ct_dt_name, p.product_name, b.bd_name, po.price, s.st_id, s.st_quantity, s.st_update,");
        sql.append(" po.option_id, br.br_id FROM stock s "); // 예시 필드
        sql.append("JOIN product_option po ON s.option_id = po.option_id ");
        sql.append("JOIN product p ON po.product_id = p.product_id ");
        sql.append("JOIN brand b ON p.bd_id = b.bd_id ");
        sql.append("JOIN branch br ON s.br_id = br.br_id ");
        sql.append("JOIN category_detail cd ON p.ct_dt_id = cd.ct_dt_id ");
        sql.append("JOIN category ct ON cd.ct_id = ct.ct_id ");
        sql.append("where br.br_id = ? ");// 로그인 후 1 대신 => 접속한 유저의 소속 브랜치(br_id)와 stock에 br_id가 일치하는지 여부 작성
        sql.append("and ct.ct_name = ?");
        sql.append("order by s.st_update ");
        
        try {
            con = dbManager.getConnection();
            pstmt = con.prepareStatement(sql.toString());
            pstmt.setInt(1, getBranchID(user));
            pstmt.setString(2, category.getCt_name());
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
                categoryDetail.setCt_dt_name(rs.getString("ct_dt_name"));
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
    
    public List<Stock> listCountAlert(User user) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Stock> list = new ArrayList<>();

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT po.option_code, ct.ct_name, cd.ct_dt_name, p.product_name, b.bd_name, po.price, s.st_id, s.st_quantity, s.st_update,");
        sql.append(" po.option_id, br.br_id FROM stock s "); // 예시 필드
        sql.append("JOIN product_option po ON s.option_id = po.option_id ");
        sql.append("JOIN product p ON po.product_id = p.product_id ");
        sql.append("JOIN brand b ON p.bd_id = b.bd_id ");
        sql.append("JOIN branch br ON s.br_id = br.br_id ");
        sql.append("JOIN category_detail cd ON p.ct_dt_id = cd.ct_dt_id ");
        sql.append("JOIN category ct ON cd.ct_id = ct.ct_id ");
        sql.append("where br.br_id = ? ");// 로그인 후 1 대신 => 접속한 유저의 소속 브랜치(br_id)와 stock에 br_id가 일치하는지 여부 작성
        sql.append("and s.st_quantity between 1 and 30 ");
        sql.append("order by s.st_update ");
        
        try {
            con = dbManager.getConnection();
            pstmt = con.prepareStatement(sql.toString());
            pstmt.setInt(1, getBranchID(user));
            rs = pstmt.executeQuery();

            while (rs.next()) {
                // Stock 객체 생성
                Stock stock = new Stock();
                stock.setSt_id(rs.getInt("st_id"));
                stock.setSt_quantity(rs.getInt("st_quantity"));
                stock.setSt_update(rs.getDate("st_update"));

            	// Category
				Category category = new Category();
				category.setCt_name(rs.getString("ct_name"));

				// CategoryDetail
				CategoryDetail categoryDetail = new CategoryDetail();
				categoryDetail.setCt_dt_name(rs.getString("ct_dt_name"));
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
    
    public List<Stock> listOldAlert(User user) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Stock> list = new ArrayList<>();

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT po.option_code, ct.ct_name, cd.ct_dt_name, p.product_name, b.bd_name, po.price, s.st_id, s.st_quantity, s.st_update,");
        sql.append(" po.option_id, br.br_id FROM stock s "); 
        sql.append("JOIN product_option po ON s.option_id = po.option_id ");
        sql.append("JOIN product p ON po.product_id = p.product_id ");
        sql.append("JOIN brand b ON p.bd_id = b.bd_id ");
        sql.append("JOIN branch br ON s.br_id = br.br_id ");
        sql.append("JOIN category_detail cd ON p.ct_dt_id = cd.ct_dt_id ");
        sql.append("JOIN category ct ON cd.ct_id = ct.ct_id ");
        sql.append("where br.br_id = ? ");// 로그인 후 1 대신 => 접속한 유저의 소속 브랜치(br_id)와 stock에 br_id가 일치하는지 여부 작성
        sql.append("and s.st_update < DATE_SUB(CURDATE(), INTERVAL 1 YEAR) ");
        sql.append("order by s.st_update ");
        
        try {
            con = dbManager.getConnection();
            pstmt = con.prepareStatement(sql.toString());
            pstmt.setInt(1, getBranchID(user));
            rs = pstmt.executeQuery();

            while (rs.next()) {
                // Stock 객체 생성
                Stock stock = new Stock();
                stock.setSt_id(rs.getInt("st_id"));
                stock.setSt_quantity(rs.getInt("st_quantity"));
                stock.setSt_update(rs.getDate("st_update"));

                // Category 객체 생성
                Category category = new Category();
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
      

    
    public void updateProductQuantity(int st_id, int st_quantity) {
    	  Connection con = null;
          PreparedStatement pstmt = null;

          StringBuffer sql = new StringBuffer();
          sql.append("UPDATE stock set st_quantity = ? where st_id = ?");
          
          try {
              con = dbManager.getConnection();
              pstmt = con.prepareStatement(sql.toString());
              pstmt.setInt(1, st_quantity);
              pstmt.setInt(2, st_id);
              pstmt.execute();
              
          } catch ( SQLException e) {
        	  e.printStackTrace();
          } finally {
        	  dbManager.release(pstmt);
          }
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
 	
    // 월별 매출 - 소속 지점 합산
    public List<Map<String, Integer>> selectSales(int br_id) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Map<String, Integer>> list = new ArrayList<>();

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT DATE_FORMAT(approve_date, '%Y') AS Year,"
        		+ " DATE_FORMAT(approve_date, '%m') AS Month,"
        		+ " SUM(b_count*price) AS Sales"
        		+ " FROM bound b"
        		+ " INNER JOIN bound_product bp"
        		+ " ON b.bound_id=bp.bound_id"
        		+ " INNER JOIN product_option po"
        		+ " ON bp.option_id=po.option_id"
        		+ " WHERE bo_state_id=3"
        		+ " AND br_id=?"
        		+ " GROUP BY Year, month"
        		+ " ORDER BY Year, Month");

        try {
            con = dbManager.getConnection();
            pstmt = con.prepareStatement(sql.toString());
            pstmt.setInt(1, br_id);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Map<String, Integer> map = new HashMap<>();
            	map.put("Year", rs.getInt("Year"));
            	map.put("Month", rs.getInt("Month"));
            	map.put("Sales", rs.getInt("Sales"));

            	list.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbManager.release(pstmt, rs);
        }

        return list;
    }
    
    
    public Stock select(int option_id, int br_id) {
    	 Connection con = null;
         PreparedStatement pstmt = null;
         ResultSet rs = null;
         Stock st = null;
         
         StringBuffer sql = new StringBuffer();
         sql.append("select * from stock where option_id = ? and br_id = ?");

         try {
             con = dbManager.getConnection();
             pstmt = con.prepareStatement(sql.toString());
             pstmt.setInt(1, option_id);
             pstmt.setInt(2, br_id);
             rs = pstmt.executeQuery();
             st = new Stock();
             while (rs.next()) {
            	 st.setSt_id(rs.getInt("st_id"));
            	 
            	 ProductOption po = new ProductOption();
            	 po.setOption_id(rs.getInt("option_id"));
            	 st.setProductOption(po);
            	 
            	 Branch br = new Branch();
            	 br.setBr_id(rs.getInt("br_id"));
            	 st.setBranch(br);
            	 
            	 st.setSt_quantity(rs.getInt("st_quantity"));
            	 st.setSt_update(rs.getDate("st_update"));
             }
         } catch (SQLException e) {
             e.printStackTrace();
         } finally {
             dbManager.release(pstmt, rs);
         }

         return st;
    }

}











