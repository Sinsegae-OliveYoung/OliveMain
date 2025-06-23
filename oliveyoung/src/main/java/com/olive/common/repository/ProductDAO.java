package com.olive.common.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.olive.common.exception.ProductException;
import com.olive.common.model.Bound;
import com.olive.common.model.BoundProduct;
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

        sql.append(
        		  "SELECT"
                + "		c.ct_name"
                + "		, cd.ct_dt_name"
                + "		, b.bd_name"
                + "		, p.product_id"
                + "		, p.product_name"
                + "		, po.option_id"
                + "		, CASE 	WHEN option_no = 99"
                + "		  		THEN '-' "
                + "		  		ELSE option_name"
                + "   	  END 	AS option_name"
                + "		, po.option_code"
                + "		, po.price"
                + "		, COALESCE( ("
                + "    				SELECT 	SUM(st_quantity) "
                + "    				FROM 	stock s "
                + "    				JOIN 	branch br2 ON br2.br_id = s.br_id "
                + "    				WHERE 	s.option_id = po.option_id "
                + "      			AND 	br2.br_id = 1"
                + "		 ), 0 ) 		AS st_quantity "
                + " FROM 	   product p "
                + " INNER JOIN product_option po	ON p.product_id = po.product_id "
                + " INNER JOIN category c 			ON p.ct_id 		= c.ct_id "
                + " INNER JOIN category_detail cd 	ON p.ct_dt_id 	= cd.ct_dt_id "
                + "								   AND c.ct_id 		= cd.ct_id "
                + " INNER JOIN brand b 				ON p.bd_id 		= b.bd_id "
                + " ORDER BY c.ct_id ASC, cd.ct_dt_id asc");
        
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
                productOption.setOption_id(rs.getInt("option_id"));
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
        
        sql.append(
        		  "SELECT "
                + "		  c.ct_name"
                + "		, cd.ct_dt_name"
                + "		, b.bd_name"
                + "		, p.product_id"
                + "		, p.product_name"
                + "		, po.option_id"
                + "		, CASE WHEN option_no = 99 "
                + "			   THEN '-' "
                + "			   ELSE option_name "
                + "			   END AS option_name"
                + "		, po.option_code"
                + "		, po.price"
                + "		, COALESCE( ("
                + "    			SELECT SUM(st_quantity) "
                + "    			FROM stock s "
                + "    			JOIN branch br2 	ON br2.br_id = s.br_id "
                + "    			WHERE s.option_id = po.option_id "
                + "      		AND   br2.br_name = ?"
                + "		  ), 0 ) AS st_quantity "
                + "		, b.bd_id"
                + " FROM 	   product p "
                + " INNER JOIN product_option po 	ON p.product_id = po.product_id "
                + " INNER JOIN category c 			ON p.ct_id 		= c.ct_id "
                + " INNER JOIN category_detail cd 	ON p.ct_dt_id 	= cd.ct_dt_id"
                + "								   AND c.ct_id 		= cd.ct_id "
                + " INNER JOIN brand b 				ON p.bd_id 		= b.bd_id "
                + " ORDER BY c.ct_id ASC, cd.ct_dt_id asc"
        );
        
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
                productOption.setOption_id(rs.getInt("option_id"));
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
    
    public void insert(Product product) throws ProductException{
    	Connection con=null;
		PreparedStatement pstmt=null;
		int result=0; //쿼리 실행 성공 여부 결정짓는 변수 
		
		con=dbManager.getConnection();
		
		StringBuffer sql=new StringBuffer();
		sql.append("insert into product(product_name, ct_id, ct_dt_id, bd_id)");
		sql.append(" values(?,?,?,?)");
		
		try {
			pstmt=con.prepareStatement(sql.toString());
			
			//모델 객체에 채워진 데이터를 꺼내서, 바인드 변수에 대입하기!! 
			pstmt.setString(1, product.getProduct_name());
			pstmt.setInt(2, product.getCategory().getCt_id());
			pstmt.setInt(3, product.getCategory_detail().getCt_dt_id());
			pstmt.setInt(4, product.getBrand().getBd_id());
			
			//쿼리수행 
			result = pstmt.executeUpdate(); //DML 실행
			if(result == 0) {
				throw new ProductException("등록이 되지 않았어요");
			}
			
		} catch (SQLException e) {
			// e.printStackTrace()에서 처리만 해버리면, 바깥쪽 즉 유저가 사용하는 프로그램에서는
			// 에러의 원인을 알 수 없으므로, 신뢰성 떨어짐.. 따라서 에러가 발생하면, 이 영역에서만 처리를
			// 국한시키지 말고, 외부 영역까지 에러 원인을 전달해야 한다.
			e.printStackTrace();
			throw new ProductException("등록에 실패하였습니다.\n 이용에 불편을 드려 죄송합니다", e);
		}finally {
			dbManager.release(pstmt);
		}
    }
    
    public int selectRecentPk() {
		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		int pk=0;
		
		con=dbManager.getConnection();
		
		StringBuffer sql = new StringBuffer();
		sql.append("select last_insert_id() as product_id");
		
		try {
			pstmt=con.prepareStatement(sql.toString());
			rs=pstmt.executeQuery(); //쿼리실행 및 결과표 반환.
			
			if(rs.next()) { //조회된 결과가 있다면..
				pk=rs.getInt("product_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			dbManager.release(pstmt, rs);
		}
		return pk;
	}
    
    public void update(Product product) throws SQLException {
    	Connection con=null;
		PreparedStatement pstmt=null;
		
		con=dbManager.getConnection();
        StringBuffer sql = new StringBuffer();
        sql.append("UPDATE product SET product_name = ?, ct_id = ?, ct_dt_id = ?, bd_id = ? WHERE product_id = ?");
        try {
            pstmt = con.prepareStatement(sql.toString());
            pstmt.setString(1, product.getProduct_name());
            pstmt.setInt(2, product.getCategory().getCt_id());
            pstmt.setInt(3, product.getCategory_detail().getCt_dt_id());
            pstmt.setInt(4, product.getBrand().getBd_id());
            pstmt.setInt(5, product.getProduct_id());
            pstmt.executeUpdate();
        } catch(SQLException e) {
        	e.printStackTrace();
        } finally {
        	dbManager.release(pstmt);
        }
    }

    // 상품 삭제
    public void delete(int product_id) throws SQLException {
    	Connection con=null;
		PreparedStatement pstmt=null;
        StringBuffer sql = new StringBuffer();
        sql.append("DELETE FROM product WHERE product_id = ?");
        try {
        	pstmt = con.prepareStatement(sql.toString());
            pstmt.setInt(1, product_id);
            pstmt.executeUpdate();
        } catch(SQLException e) {
        	e.printStackTrace();
        } finally {
        	dbManager.release(pstmt);
        }
    }
}
