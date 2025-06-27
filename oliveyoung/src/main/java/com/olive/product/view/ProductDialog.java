package com.olive.product.view;

//기존 ProductListPanel.java에 있던 등록/수정 다이얼로그 관련 코드 분리

import javax.swing.*;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.olive.common.config.Config;
import com.olive.common.model.Brand;
import com.olive.common.model.Category;
import com.olive.common.model.CategoryDetail;
import com.olive.common.model.Product;
import com.olive.common.model.ProductImg;
import com.olive.common.model.ProductOption;
import com.olive.common.repository.BrandDAO;
import com.olive.common.repository.CategoryDAO;
import com.olive.common.repository.CategoryDetailDAO;
import com.olive.common.repository.ProductDAO;
import com.olive.common.repository.ProductImgDAO;
import com.olive.common.repository.ProductOptionDAO;
import com.olive.common.util.DBManager;
import com.olive.common.util.style.ButtonUtil;
import com.olive.common.util.style.ComboBoxUtil;
import com.olive.mainlayout.MainLayout;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class ProductDialog extends JDialog {

	private ProductForm form;
	private boolean isEditMode;
	// imageCloud 웹서버 가동시 ip + 톰캣포트번호
	String imgServerIp = "http://192.168.60.36:8282"; 

	public ProductDialog(MainLayout mainLayout, ProductOption option, Product product) {
		setTitle(option == null ? "상품 등록" : "상품 수정");
		setSize(600, 500);
		setLocationRelativeTo(null);
		setModal(true);

		this.isEditMode = option != null;
		this.form = new ProductForm(option, product, mainLayout);
		add(form);

		JPanel btnPanel = new JPanel();
		JButton btnSave = ButtonUtil.greenButtonUtil("저장");
		JButton btnCancel = ButtonUtil.pinkButtonUtil("취소");
		
		btnSave.setPreferredSize(new Dimension(100, 30));
		btnCancel.setPreferredSize(new Dimension(100, 30));
		btnPanel.setBackground(Config.WHITE);
		
		btnPanel.add(btnSave);
		btnPanel.add(btnCancel);

		form.addButtons(btnPanel);

		btnSave.addActionListener(e -> {
	        if (mainLayout.user.getRole().getRole_id() == 3) {
	            JOptionPane.showMessageDialog(ProductDialog.this, "상품 수정 권한 없음");
	            return;
	        }
			if (isEditMode) {
				if (form.updateProduct()) { // 성공 시에만 닫기
					mainLayout.setDataDirty(true);
					mainLayout.refreshIfDirty();
					dispose();
				}
			} else {
				if (form.insertProduct()) { // 성공 시에만 닫기
					mainLayout.setDataDirty(true);
					mainLayout.refreshIfDirty();
					dispose();
				}
			}
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
		JComboBox<String> cbActive = new JComboBox<>(new String[] { "y", "n" });
		JPanel p_preview = new JPanel();
		JButton bt_open = ButtonUtil.grayButtonUtil("사진 선택");
		JFileChooser chooser = new JFileChooser("C:/public");
		File file = null;

		MainLayout mainLayout;
		ProductOption option;
		Product product;
		ProductDAO productDAO;
		ProductOptionDAO productOptionDAO;
		ProductImgDAO productImgDAO;

		public ProductForm(ProductOption option, Product product, MainLayout mainLayout) {
			this.mainLayout = mainLayout;
			this.option = option;
			this.product = product != null ? product : new Product();
			setLayout(new GridBagLayout());
			setBackground(Config.WHITE);
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets = new Insets(10, 10, 10, 10);
			gbc.fill = GridBagConstraints.HORIZONTAL;

			productDAO = new ProductDAO();
			productOptionDAO = new ProductOptionDAO();
			productImgDAO = new ProductImgDAO();

			tfName.setPreferredSize(new Dimension(200, 30));
			tfPrice.setPreferredSize(new Dimension(200, 30));
			tfOptionName.setPreferredSize(new Dimension(200, 30));
			cbBrand.setPreferredSize(new Dimension(200, 30));
			cbBrand.setUI(new ComboBoxUtil());
			ComboBoxUtil.applyDefaultStyle(cbBrand);
			cbCategory.setPreferredSize(new Dimension(200, 30));
			cbCategory.setUI(new ComboBoxUtil());
			ComboBoxUtil.applyDefaultStyle(cbCategory);
			cbCategoryDetail.setPreferredSize(new Dimension(200, 30));
			cbCategoryDetail.setUI(new ComboBoxUtil());
			ComboBoxUtil.applyDefaultStyle(cbCategoryDetail);
			cbActive.setPreferredSize(new Dimension(200, 30));
			cbActive.setUI(new ComboBoxUtil());
			ComboBoxUtil.applyDefaultStyle(cbActive);

			// 브랜드, 카테고리 채우기
			for (Brand b : new BrandDAO().selectAll())
				cbBrand.addItem(b);
			for (Category c : new CategoryDAO().selectAll())
				cbCategory.addItem(c);
			for (CategoryDetail c : new CategoryDetailDAO().selectAll())
				cbCategoryDetail.addItem(c);

			// 수정 시 기존 값 세팅
			if (option != null) {
				tfName.setText(product.getProduct_name());
				tfPrice.setText(String.valueOf(option.getPrice()));
				tfOptionName.setText(option.getOption_name());
				cbBrand.setSelectedItem(product.getBrand());
				cbCategory.setSelectedItem(product.getCategory());

				ProductImg img = new ProductImgDAO().selectByOptionId(option.getOption_id());

				if (img != null) {
					try {
						String filename = img.getImg_filename();
						String imageUrl = imgServerIp + "/public/" + filename;
						System.out.println(imageUrl);

						// URL로 이미지 읽기
						BufferedImage bufferedImage = ImageIO.read(new URL(imageUrl));
						Image scaled = bufferedImage.getScaledInstance(160, 160, Image.SCALE_SMOOTH);
						JLabel imgLabel = new JLabel(new ImageIcon(scaled));

						JPanel imgPanel = new JPanel();
						imgPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // 위쪽 여백
						imgPanel.setOpaque(false);
						imgPanel.add(imgLabel);

						p_preview.removeAll();
						p_preview.add(imgPanel);
						p_preview.revalidate();
						p_preview.repaint();
					} catch (IOException e) {
						System.err.println("이미지 로딩 실패: " + e.getMessage());
					}
				}

				loadCategoryDetails(product.getCategory()); // 이게 먼저
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

			// 사진 버튼은 등록일 때만 보이게
			if (option == null) { // 등록 모드
				bt_open.addActionListener(e -> {
					if (chooser.showOpenDialog(ProductForm.this) == JFileChooser.APPROVE_OPTION) {
						file = chooser.getSelectedFile();
						previewImage(file);
					}
				});
				addRow(this, gbc, row++, "사진", bt_open);
			} else {
				addRow(this, gbc, row++, "사진", new JLabel("※ 사진은 수정할 수 없습니다"));
			}

			p_preview.setPreferredSize(new Dimension(180, 200));
			gbc.gridx = 2;
			gbc.gridy = 0;
			gbc.gridheight = row;
			add(p_preview, gbc);

			// 카테고리 선택 시 상세 카테고리 목록 갱신
			cbCategory.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						Category selectedCategory = (Category) cbCategory.getSelectedItem();
						loadCategoryDetails(selectedCategory);
					}
				}
			});
		}

		private void addRow(JPanel panel, GridBagConstraints gbc, int y, String label, JComponent comp) {
			gbc.gridx = 0;
			gbc.gridy = y;
			gbc.gridwidth = 1;
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

		public boolean insertProduct() {
			if (!validateInputs())
				return false;

			DBManager dbManager = DBManager.getInstance();
			Connection con = dbManager.getConnection();
			try {
				con.setAutoCommit(false);

				product.setProduct_name(tfName.getText());
				product.setBrand((Brand) cbBrand.getSelectedItem());
				product.setCategory((Category) cbCategory.getSelectedItem());
				product.setCategory_detail((CategoryDetail) cbCategoryDetail.getSelectedItem());

				productDAO.insert(product, con);
				int productId = new ProductDAO().selectRecentPk();
				product.setProduct_id(productId);

				ProductOption po = new ProductOption();
				po.setOption_name(tfOptionName.getText());
				po.setPrice(Integer.parseInt(tfPrice.getText()));
				po.setOption_active((String) cbActive.getSelectedItem());
				po.setProduct(product);
				
				String active = (String) cbActive.getSelectedItem();
				int optionNum = 0;
				
				if (active.equals("y")) {
					int maxOptionNo = productOptionDAO.selectMaxOptionNo(product.getProduct_id());
					optionNum = maxOptionNo + 1;
				} else if (active.equals("n")) {
					optionNum = 99;
				}
				po.setOption_no(optionNum);
				
				StringBuffer codeMaker = new StringBuffer();
				codeMaker.append(product.getCategory().getCt_code());
				codeMaker.append("-");
				codeMaker.append(product.getCategory_detail().getCt_dt_code());
				codeMaker.append("-");
				codeMaker.append(product.getBrand().getBd_code());
				codeMaker.append("-");
				codeMaker.append(product.getProduct_id() + optionNum);

				po.setOption_code(codeMaker.toString());

				productOptionDAO.insert(po, con);

				// 생성된 PK 가져와서 설정
				int optionId = productOptionDAO.selectRecentPk();
				po.setOption_id(optionId);

				// 이미지가 선택되었을 경우만 등록
				if (file != null) {
					ProductImg img = new ProductImg(); // ❗ 직접 생성해야 함
					img.setProductOption(po);
					img.setImg_filename(file.getName());
					productImgDAO.insert(img, con);

					String fileName = img.getImg_filename();
					String imgUrl = imgServerIp + "/public/" + fileName;

					// 로컬 public 폴더에서 가져오는 방식
//						File imgFile = new File(Config.IMG_PATH + File.separator + filename); // 경로는 환경에 맞게
//						BufferedImage bufferedImage = ImageIO.read(imgFile);
					upload();

					p_preview.revalidate();
					p_preview.repaint();
				}

				con.commit();
				mainLayout.setDataDirty(true);
				mainLayout.refreshIfDirty();
				
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				try {
					con.rollback();
					System.out.println("rollback() 완료");
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				return false;
			} finally {
				try {
					con.setAutoCommit(true);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		public boolean updateProduct() {
			if (!validateInputs())
				return false;
			
			DBManager dbManager = DBManager.getInstance();
			Connection con = dbManager.getConnection();
			try {
				con.setAutoCommit(false);
				
				product.setProduct_name(tfName.getText());
				product.setBrand((Brand) cbBrand.getSelectedItem());
				product.setCategory((Category) cbCategory.getSelectedItem());
				product.setCategory_detail((CategoryDetail) cbCategoryDetail.getSelectedItem());
				
				option.setOption_name(tfOptionName.getText());
				
				String active = (String) cbActive.getSelectedItem();
				int optionNum = 0;
				if (active.equals("y")) {
					int maxOptionNo = productOptionDAO.selectMaxOptionNo(product.getProduct_id());
					optionNum = maxOptionNo + 1;
				} else if (active.equals("n")) {
					optionNum = 99;
				}
				option.setOption_no(optionNum);

				StringBuffer codeMaker = new StringBuffer();
				codeMaker.append(product.getCategory().getCt_code());
				codeMaker.append("-");
				codeMaker.append(product.getCategory_detail().getCt_dt_code());
				codeMaker.append("-");
				codeMaker.append(product.getBrand().getBd_code());
				codeMaker.append("-");
				codeMaker.append(product.getProduct_id() + optionNum);

				option.setOption_code(codeMaker.toString());
				option.setPrice(Integer.parseInt(tfPrice.getText()));
				option.setOption_active((String) cbActive.getSelectedItem());
				
				new ProductDAO().update(product, con);
				new ProductOptionDAO().update(option, con);
				
				con.commit();
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			} finally {
				try {
					con.setAutoCommit(true);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		public void upload() {
			// Http 통신이 가능한 api를 이용해야 한다. 2가지 객체가 있다.
			// 1) 고전적인 방식 HttpURLConnection 객체 이용
			// 2) 최신 방식 HttpClient 객체 이용 javase 미포함, apache
			CloseableHttpClient httpClient = HttpClients.createDefault();

			// Post
			HttpPost post = new HttpPost(imgServerIp + "/upload/regist");
			
			/* 서버로 전송할 데이터 구성하기 */
			StringBody titleBody = new StringBody("post", ContentType.create("text/plain", Consts.UTF_8));

			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.addPart("title", titleBody); // 텍스트 파라미터 만들기 title은 html에서의 name값에 부여한 것과 같은 것

			FileBody filebody = new FileBody(file);
			builder.addPart("photo", filebody); // <input type="file" name="photo1">

			// 다 완성된 HTTP 파라미터와 그 값을, post객체에 담기 (body에 담겨짐)
			HttpEntity entity = builder.build(); // 빌더가 전송 직전에 엔터티로 변환
			post.setEntity(entity);

			// 서버에 요청 시도!
			// 서버에서 전송한 Http Status Code를 반환받자
			CloseableHttpResponse response = null; // 서버가 전송한 상태 코드를 보유한 객체
			try {
				response = httpClient.execute(post); // 웹 브라우저로 요청하는 행동과 동일!

				int status = response.getStatusLine().getStatusCode(); // int형으로된 응답 코드
				if (status == 200) {
					JOptionPane.showMessageDialog(this, "업로드 성공");
				} else {
					JOptionPane.showMessageDialog(this, "업로드 실패");
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					response.close();
					httpClient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		private boolean validateInputs() {
			if (tfName.getText().trim().isEmpty()) {
				JOptionPane.showMessageDialog(this, "상품명을 입력하세요.");
				tfName.requestFocus();
				return false;
			}
			if (cbBrand.getSelectedItem() == null) {
				JOptionPane.showMessageDialog(this, "브랜드를 선택하세요.");
				cbBrand.requestFocus();
				return false;
			}
			if (cbCategory.getSelectedItem() == null) {
				JOptionPane.showMessageDialog(this, "카테고리를 선택하세요.");
				cbCategory.requestFocus();
				return false;
			}
			if (cbCategoryDetail.getSelectedItem() == null) {
				JOptionPane.showMessageDialog(this, "상세 카테고리를 선택하세요.");
				cbCategoryDetail.requestFocus();
				return false;
			}
			if (tfOptionName.getText().trim().isEmpty()) {
				JOptionPane.showMessageDialog(this, "옵션명을 입력하세요.");
				tfOptionName.requestFocus();
				return false;
			}
			if (tfPrice.getText().trim().isEmpty()) {
				JOptionPane.showMessageDialog(this, "가격을 입력하세요.");
				tfPrice.requestFocus();
				return false;
			}
			try {
				Integer.parseInt(tfPrice.getText().trim()); // 숫자여부 확인
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(this, "가격은 숫자만 입력 가능합니다.");
				tfPrice.requestFocus();
				return false;
			}
			return true;
		}


		private void loadCategoryDetails(Category category) {
			cbCategoryDetail.removeAllItems();
			if (category == null)
				return;

			for (CategoryDetail cd : new com.olive.common.repository.CategoryDetailDAO()
					.selectByCategoryId(category.getCt_id())) {
				cbCategoryDetail.addItem(cd);
			}
		}
	}
}
