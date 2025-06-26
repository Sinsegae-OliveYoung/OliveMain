package com.olive.product.view;

//기존 ProductListPanel.java에 있던 등록/수정 다이얼로그 관련 코드 분리

import javax.swing.*;

import com.olive.common.config.Config;
import com.olive.common.model.Brand;
import com.olive.common.model.Category;
import com.olive.common.model.CategoryDetail;
import com.olive.common.model.Product;
import com.olive.common.model.ProductImg;
import com.olive.common.model.ProductOption;
import com.olive.common.repository.BrandDAO;
import com.olive.common.repository.CategoryDAO;
import com.olive.common.repository.ProductDAO;
import com.olive.common.repository.ProductImgDAO;
import com.olive.common.repository.ProductOptionDAO;
import com.olive.common.util.DBManager;
import com.olive.mainlayout.MainLayout;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class ProductDialog extends JDialog {
 private ProductForm form;
 private boolean isEditMode;

 public ProductDialog(MainLayout mainLayout, ProductOption option, Product product) {
     setTitle(option == null ? "상품 등록" : "상품 수정");
     setSize(600, 500);
     setLocationRelativeTo(null);
     setModal(true);

     this.isEditMode = option != null;
     this.form = new ProductForm(option, product);
     add(form);

     JPanel btnPanel = new JPanel();
     JButton btnSave = new JButton("저장");
     JButton btnCancel = new JButton("취소");
     btnPanel.add(btnSave);
     btnPanel.add(btnCancel);

     form.addButtons(btnPanel);

     btnSave.addActionListener(e -> {
         if (isEditMode) {
             form.updateProduct();
         } else {
             form.insertProduct();
         }
         mainLayout.setDataDirty(true);
         mainLayout.refreshIfDirty();
         dispose();
     });

     btnCancel.addActionListener(e -> dispose());
 }

 // 내부 클래스: 등록/수정 공통 Form 구성
 class ProductForm extends JPanel {
     JTextField tfName = new JTextField();
     JTextField tfPrice = new JTextField();
     JTextField tfOptionName = new JTextField();
     JComboBox<Brand> cbBrand = new JComboBox<>();
     JComboBox<Category> cbCategory = new JComboBox<>();
     JComboBox<CategoryDetail> cbCategoryDetail = new JComboBox<>();
     JComboBox<String> cbActive = new JComboBox<>(new String[] {"y", "n"});
     JPanel p_preview = new JPanel();
     JButton bt_open = new JButton("사진 선택");
     JFileChooser chooser = new JFileChooser("C:/public");
     File file = null;

     ProductOption option;
     Product product;

     public ProductForm(ProductOption option, Product product) {
         this.option = option;
         this.product = product != null ? product : new Product();
         setLayout(new GridBagLayout());
         setBackground(Config.WHITE);
         GridBagConstraints gbc = new GridBagConstraints();
         gbc.insets = new Insets(10, 10, 10, 10);
         gbc.fill = GridBagConstraints.HORIZONTAL;

         tfName.setPreferredSize(new Dimension(200, 30));
         tfPrice.setPreferredSize(new Dimension(200, 30));
         tfOptionName.setPreferredSize(new Dimension(200, 30));
         cbBrand.setPreferredSize(new Dimension(200, 30));
         cbCategory.setPreferredSize(new Dimension(200, 30));
         cbCategoryDetail.setPreferredSize(new Dimension(200, 30));
         cbActive.setPreferredSize(new Dimension(200, 30));

         // 브랜드, 카테고리 채우기
         for (Brand b : new BrandDAO().selectAll()) cbBrand.addItem(b);
         for (Category c : new CategoryDAO().selectAll()) cbCategory.addItem(c);

         // 수정 시 기존 값 세팅
         if (option != null) {
             tfName.setText(product.getProduct_name());
             tfPrice.setText(String.valueOf(option.getPrice()));
             tfOptionName.setText(option.getOption_name());
             cbBrand.setSelectedItem(product.getBrand());
             cbCategory.setSelectedItem(product.getCategory());
             cbCategoryDetail.setSelectedItem(product.getCategory_detail());
             cbActive.setSelectedItem(option.getOption_active());
         }

         // 구성
         int row = 0;
         addRow(this, gbc, row++, "브랜드", cbBrand);
         addRow(this, gbc, row++, "상품명", tfName);
         addRow(this, gbc, row++, "카테고리", cbCategory);
         addRow(this, gbc, row++, "상세 카테고리", cbCategoryDetail);
         addRow(this, gbc, row++, "옵션명", tfOptionName);
         addRow(this, gbc, row++, "가격", tfPrice);
         addRow(this, gbc, row++, "활성화", cbActive);
         addRow(this, gbc, row++, "사진", bt_open);

         bt_open.addActionListener(e -> {
             if (chooser.showOpenDialog(ProductForm.this) == JFileChooser.APPROVE_OPTION) {
                 file = chooser.getSelectedFile();
                 previewImage(file);
             }
         });

         p_preview.setPreferredSize(new Dimension(180, 200));
         gbc.gridx = 2;
         gbc.gridy = 0;
         gbc.gridheight = row;
         add(p_preview, gbc);
     }

     private void addRow(JPanel panel, GridBagConstraints gbc, int y, String label, JComponent comp) {
         gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 1;
         panel.add(new JLabel(label), gbc);
         gbc.gridx = 1;
         panel.add(comp, gbc);
     }

     public void addButtons(JPanel btnPanel) {
         GridBagConstraints gbc = new GridBagConstraints();
         gbc.gridx = 0;
         gbc.gridy = 9;
         gbc.gridwidth = 2;
         add(btnPanel, gbc);
     }

     private void previewImage(File file) {
         try {
             BufferedImage img = ImageIO.read(file);
             Image scaled = img.getScaledInstance(160, 160, Image.SCALE_SMOOTH);
             p_preview.removeAll();
             p_preview.add(new JLabel(new ImageIcon(scaled)));
             p_preview.revalidate();
             p_preview.repaint();
         } catch (IOException e) {
             e.printStackTrace();
         }
     }

     public void insertProduct() {
         try (Connection con = DBManager.getInstance().getConnection()) {
             con.setAutoCommit(false);

             product.setProduct_name(tfName.getText());
             product.setBrand((Brand) cbBrand.getSelectedItem());
             product.setCategory((Category) cbCategory.getSelectedItem());
             product.setCategory_detail((CategoryDetail) cbCategoryDetail.getSelectedItem());

             new ProductDAO().insert(product);
             int productId = new ProductDAO().selectRecentPk();
             product.setProduct_id(productId);

             ProductOption po = new ProductOption();
             po.setOption_name(tfOptionName.getText());
             po.setPrice(Integer.parseInt(tfPrice.getText()));
             po.setOption_active((String) cbActive.getSelectedItem());
             po.setProduct(product);

             new ProductOptionDAO().insert(po);
             int optionId = new ProductOptionDAO().selectRecentPk();
             po.setOption_id(optionId);

             if (file != null) {
                 ProductImg img = new ProductImg();
                 img.setProductOption(po);
                 img.setImg_filename(file.getName());
                 new ProductImgDAO().insert(img, con);
             }

             con.commit();
         } catch (Exception e) {
             e.printStackTrace();
         }
     }

     public void updateProduct() {
         try (Connection con = DBManager.getInstance().getConnection()) {
             con.setAutoCommit(false);

             product.setProduct_name(tfName.getText());
             product.setBrand((Brand) cbBrand.getSelectedItem());
             product.setCategory((Category) cbCategory.getSelectedItem());
             product.setCategory_detail((CategoryDetail) cbCategoryDetail.getSelectedItem());

             option.setOption_name(tfOptionName.getText());
             option.setPrice(Integer.parseInt(tfPrice.getText()));
             option.setOption_active((String) cbActive.getSelectedItem());

             new ProductDAO().update(product, con);
             new ProductOptionDAO().update(option, con);

             con.commit();
         } catch (Exception e) {
             e.printStackTrace();
         }
     }
 }
}
