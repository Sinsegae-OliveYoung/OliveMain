package com.olive.product.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.olive.common.config.Config;
import com.olive.common.exception.ProductException;
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
import com.olive.common.util.TableUtil;
import com.olive.common.util.style.ButtonUtil;
import com.olive.common.util.style.ComboBoxUtil;
import com.olive.common.util.style.LabelUtil;
import com.olive.common.view.Panel;
import com.olive.mainlayout.MainLayout;
import com.olive.product.ProductPage;
import com.olive.product.model.ProductModel;
import com.olive.stock.StockConfig;

public class ProductListPanel extends Panel {

	JTable table;
	ProductModel model;

	// 업로드 이미지
	JButton bt_open;
	JPanel p_preview;
	JFileChooser chooser;
	File file;
	Image img;

	JTextField tfName;
	JTextField tfPrice;
	JTextField tfOptionName;

	JComboBox<String> cbActive;
	JComboBox<Brand> cbBrand;

	JComboBox<Category> cbCategory;
	JComboBox<CategoryDetail> cbCategoryDetail;

	ProductDAO productDAO;
	ProductOptionDAO productOptionDAO;
	CategoryDetailDAO categoryDetailDAO;
	ProductImgDAO productImgDAO;

	DBManager dbManager = DBManager.getInstance();
	JScrollPane scroll;

	@Override
	public void refresh() {
		model.reload(); // ListModel에서 최신 데이터 로드
		TableUtil.tableStyleUtil(table, scroll, 700, true);
	}

	public ProductListPanel(MainLayout mainLayout) {
		super(mainLayout);
		setLayout(new BorderLayout());

		productDAO = new ProductDAO();
		productOptionDAO = new ProductOptionDAO();
		categoryDetailDAO = new CategoryDetailDAO();
		productImgDAO = new ProductImgDAO();

		Color bgColor = Config.WHITE;
		Font defaultFont = new Font("SansSerif", Font.PLAIN, 13);

		setBackground(bgColor);

		// 상단 제목 패널
		JPanel titlePanel = new JPanel(new BorderLayout());
		StockConfig.panelStyle(titlePanel);
		titlePanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 10, 20));

		JLabel titleLabel = new JLabel("상품 리스트 관리");
		LabelUtil.applyTitleStyle(titleLabel);
		titleLabel.setForeground(new Color(40, 40, 40));
		titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
		titlePanel.add(titleLabel, BorderLayout.WEST);

		// 버튼 패널 (세로 정렬)
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		buttonPanel.setOpaque(false);
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(35, 10, 20, 30));

		Font buttonFont = new Font("SansSerif", Font.PLAIN, 13);
		Dimension buttonSize = new Dimension(100, 30);
		Color buttonText = new Color(40, 40, 40);

		JButton btnAdd = ButtonUtil.greenButtonUtil("상품 등록");
		JButton btnEdit = ButtonUtil.greenButtonUtil("상품 수정");
		JButton btnDelete = ButtonUtil.pinkButtonUtil("상품 삭제");

		JButton[] buttons = { btnAdd, btnEdit, btnDelete };

		for (JButton btn : buttons) {
			buttonPanel.add(btn);
			btn.setPreferredSize(buttonSize);
			btn.setMaximumSize(buttonSize);
			buttonPanel.add(Box.createRigidArea(new Dimension(0, 30)));
		}

		btnEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if (mainLayout.user.getRole().getRole_id() == 3) {
					JOptionPane.showMessageDialog(ProductListPanel.this, "상품 수정 권한 없음");
				} else {
					int row = table.getSelectedRow();
					if (row == -1) {
						JOptionPane.showMessageDialog(ProductListPanel.this, "수정할 상품을 선택하세요.");
						return;
					}

					// 선택된 행의 Product 객체 얻기
					ProductOption selectedOption = model.getProductOptionAt(table.getSelectedRow());
					Product selectedProduct = selectedOption.getProduct();

					JDialog dialog = new JDialog();
					dialog.setTitle("상품 수정");
					dialog.setSize(600, 500);
					dialog.setLocationRelativeTo(null);
					dialog.setModal(true);

					// 내용 패널 재활용
					JPanel contentPanel = new JPanel(new GridBagLayout());
					GridBagConstraints gbc = new GridBagConstraints();
					gbc.insets = new Insets(10, 10, 10, 10);
					gbc.fill = GridBagConstraints.HORIZONTAL;

					JLabel lblBrand = new JLabel("브랜드명:");
					cbBrand = new JComboBox<>();
					cbBrand.setPreferredSize(new Dimension(200, 30));
					cbBrand.setUI(new ComboBoxUtil()); 
					ComboBoxUtil.applyDefaultStyle(cbBrand); 
					for (Brand b : new BrandDAO().selectAll())
						cbBrand.addItem(b);
					cbBrand.setSelectedItem(selectedProduct.getBrand());

					JLabel lblName = new JLabel("상품명:");
					tfName = new JTextField(selectedProduct.getProduct_name());
					tfName.setPreferredSize(new Dimension(200, 30));

					JLabel lblCategory = new JLabel("카테고리:");
					JComboBox<Category> editCbCategory = new JComboBox<>();
					editCbCategory.setPreferredSize(new Dimension(200, 30));
					editCbCategory.setUI(new ComboBoxUtil());
					ComboBoxUtil.applyDefaultStyle(editCbCategory);
					
					for (Category c : new CategoryDAO().selectAll())
						editCbCategory.addItem(c);

					Category category = new CategoryDAO().selectById(selectedProduct.getCategory().getCt_id());
					editCbCategory.setSelectedItem(category);

					JLabel lblCategoryDetail = new JLabel("상세 카테고리:");
					JComboBox<CategoryDetail> editCbCategoryDetail = new JComboBox<>();
					editCbCategoryDetail.setPreferredSize(new Dimension(200, 30));
					editCbCategoryDetail.setUI(new ComboBoxUtil());
					ComboBoxUtil.applyDefaultStyle(editCbCategoryDetail);

					// 1. 해당 카테고리의 상세 카테고리들 불러오기
					Category editcategory = new CategoryDAO().selectById(selectedProduct.getCategory().getCt_id());
					List<CategoryDetail> details = new CategoryDetailDAO().selectByCategoryId(editcategory.getCt_id());
					for (CategoryDetail d : details) {
						editCbCategoryDetail.addItem(d);
					}

					// 2. 선택 상태 지정
					CategoryDetail categoryDetail = new CategoryDetailDAO()
							.selectByCategoryDetailId(selectedProduct.getCategory_detail().getCt_dt_id());
					editCbCategoryDetail.setSelectedItem(categoryDetail);

					JLabel lblOptionName = new JLabel("옵션명:");
					tfOptionName = new JTextField(selectedOption.getOption_name());
					tfOptionName.setPreferredSize(new Dimension(200, 30));

					JLabel lblPrice = new JLabel("가격:");
					tfPrice = new JTextField(String.valueOf(selectedOption.getPrice()));
					tfPrice.setPreferredSize(new Dimension(200, 30));

					JLabel lblActive = new JLabel("활성화:");
					cbActive = new JComboBox<>(new String[] { "y", "n" });
					cbActive.setSelectedItem(selectedOption.getOption_active());
					cbActive.setPreferredSize(new Dimension(200, 30));
					cbActive.setUI(new ComboBoxUtil());
					ComboBoxUtil.applyDefaultStyle(cbActive);

					// 카테고리 변경 시 상세 카테고리 동기화
					editCbCategory.addItemListener(new ItemListener() {
						@Override
						public void itemStateChanged(ItemEvent e) {
							if (e.getStateChange() == ItemEvent.SELECTED) {
								Category selected = (Category) e.getItem();
								fillCategoryDetailForEdit(selected, editCbCategoryDetail);
							}
						}
					});

					JButton btnCancel = new JButton("취소");
					btnCancel.addActionListener(ev -> dialog.dispose());

					// 레이아웃 배치
					// 콤보박스 가운데 정렬 렌더러
					DefaultListCellRenderer centerRenderer = new DefaultListCellRenderer();
					centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

					cbBrand.setRenderer(centerRenderer);
					editCbCategory.setRenderer(centerRenderer);
					editCbCategoryDetail.setRenderer(centerRenderer);
					cbActive.setRenderer(centerRenderer);

					// 스타일
					Color labelColor = new Color(60, 60, 60);

					contentPanel.setBackground(Config.WHITE);
					lblBrand.setForeground(labelColor);
					lblName.setForeground(labelColor);
					lblCategory.setForeground(labelColor);
					lblCategoryDetail.setForeground(labelColor);
					lblOptionName.setForeground(labelColor);
					lblPrice.setForeground(labelColor);
					lblActive.setForeground(labelColor);

					// 텍스트 필드 가운데 정렬
					tfName.setHorizontalAlignment(SwingConstants.CENTER);
					tfPrice.setHorizontalAlignment(SwingConstants.CENTER);
					tfOptionName.setHorizontalAlignment(SwingConstants.CENTER);

					gbc.gridx = 0;
					gbc.gridy = 0;
					contentPanel.add(lblBrand, gbc);
					gbc.gridx = 1;
					contentPanel.add(cbBrand, gbc);

					gbc.gridx = 0;
					gbc.gridy++;
					contentPanel.add(lblName, gbc);
					gbc.gridx = 1;
					contentPanel.add(tfName, gbc);

					gbc.gridx = 0;
					gbc.gridy++;
					contentPanel.add(lblCategory, gbc);
					gbc.gridx = 1;
					contentPanel.add(editCbCategory, gbc);

					gbc.gridx = 0;
					gbc.gridy++;
					contentPanel.add(lblCategoryDetail, gbc);
					gbc.gridx = 1;
					contentPanel.add(editCbCategoryDetail, gbc);

					gbc.gridx = 0;
					gbc.gridy++;
					contentPanel.add(lblOptionName, gbc);
					gbc.gridx = 1;
					contentPanel.add(tfOptionName, gbc);

					gbc.gridx = 0;
					gbc.gridy++;
					contentPanel.add(lblPrice, gbc);
					gbc.gridx = 1;
					contentPanel.add(tfPrice, gbc);

					gbc.gridx = 0;
					gbc.gridy++;
					contentPanel.add(lblActive, gbc);
					gbc.gridx = 1;
					contentPanel.add(cbActive, gbc);

					JPanel p_preview = new JPanel(); // 이미 선언되어 있다고 가정
					p_preview.setPreferredSize(new Dimension(180, 200));
					p_preview.setBorder(BorderFactory.createEmptyBorder());

					// 이미지 미리보기 구성
					ProductImg productImg = new ProductImgDAO().selectByOptionId(selectedOption.getOption_id());

					if (productImg != null) {
						try {
							p_preview.removeAll(); // 기존 내용 제거
							String filename = productImg.getImg_filename();
							File imgFile = new File(Config.IMG_PATH + File.separator + filename); // 경로는 환경에 맞게
							BufferedImage bufferedImage = ImageIO.read(imgFile);
							Image scaled = bufferedImage.getScaledInstance(160, 160, Image.SCALE_SMOOTH);
							JLabel imgLabel = new JLabel(new ImageIcon(scaled));

							// 이미지 패널 생성 및 여백 주기
							JPanel imgPanel = new JPanel();
							imgPanel.setBorder(BorderFactory.createEmptyBorder(100, 10, 0, 0)); // 위쪽 여백 100px
							imgPanel.add(imgLabel);
							imgPanel.setOpaque(false); // 배경 투명

							p_preview.add(imgPanel);

						} catch (IOException e1) {
							e1.printStackTrace();
						}
						p_preview.revalidate();
						p_preview.repaint();
					}

					JPanel btnPanel = new JPanel();

					JButton btnSave =ButtonUtil.blueButtonUtil("수정");
					gbc.gridx = 0;
					gbc.gridy++;
					gbc.gridwidth = 2;

					// 저장 버튼 스타일
					btnSave.setPreferredSize(new Dimension(100, 35));
					btnSave.setFont(new Font("SansSerif", Font.BOLD, 13));
					btnSave.setBackground(new Color(130, 180, 250));
					btnSave.setForeground(Color.WHITE);
					btnSave.setFocusPainted(false);
					btnSave.setBorder(BorderFactory.createLineBorder(new Color(100, 150, 220)));
					btnSave.setCursor(new Cursor(Cursor.HAND_CURSOR));

					btnSave.addActionListener(ev -> {
						selectedProduct.setProduct_name(tfName.getText());
						selectedProduct.setBrand((Brand) cbBrand.getSelectedItem());
						selectedProduct.setCategory((Category) editCbCategory.getSelectedItem());
						selectedProduct.setCategory_detail((CategoryDetail) editCbCategoryDetail.getSelectedItem());

						selectedOption.setOption_name(tfOptionName.getText());
						selectedOption.setPrice(Integer.parseInt(tfPrice.getText()));
						selectedOption.setOption_active((String) cbActive.getSelectedItem());

						Connection con = null;
						DBManager dbManager = DBManager.getInstance();

						try {
							con = dbManager.getConnection();
							con.setAutoCommit(false);
							productDAO.update(selectedProduct, con);
							productOptionDAO.update(selectedOption, con);
							con.commit();
							refresh();
							dialog.dispose();
							JOptionPane.showMessageDialog(ProductListPanel.this, "수정이 완료되었습니다.");
							mainLayout.setDataDirty(true);
							mainLayout.refreshIfDirty();
						} catch (Exception ex) {
							ex.printStackTrace();
							JOptionPane.showMessageDialog(ProductListPanel.this, "수정 중 오류가 발생했습니다.");
						} finally {
							try {
								con.setAutoCommit(true);
							} catch (SQLException e1) {
								e1.printStackTrace();
							}
						}
					});

					// 취소 버튼 스타일
					btnCancel.setPreferredSize(new Dimension(100, 35));
					btnCancel.setFont(new Font("SansSerif", Font.BOLD, 13));
					btnCancel.setBackground(Color.gray);
					btnCancel.setForeground(Color.WHITE);
					btnCancel.setFocusPainted(false);
					btnCancel.setBorder(BorderFactory.createLineBorder(new Color(100, 150, 220)));
					btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));

					btnCancel.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							dialog.dispose();
						}
					});

					// 저장 버튼 위치 설정 (기존 설정 변경)
					gbc.anchor = GridBagConstraints.WEST;
					gbc.fill = GridBagConstraints.NONE; // 이걸로 크기 강제
					gbc.weightx = 0; // 공간 분배 없음
					btnPanel.add(btnSave, gbc);

					// 취소 버튼 위치 설정 (기존 설정 변경)
					gbc.anchor = GridBagConstraints.EAST;
					gbc.fill = GridBagConstraints.NONE; // 이걸로 크기 강제
					gbc.weightx = 0; // 공간 분배 없음
					btnPanel.add(btnCancel, gbc);
					
					btnPanel.setBackground(Config.WHITE); // 다이얼로그 배경색과 맞춤

					gbc.anchor = GridBagConstraints.SOUTH;
					contentPanel.add(btnPanel, gbc);

					// 외부 패널로 전체 레이아웃 감싸기 (왼쪽 미리보기, 오른쪽 입력)
					JPanel dialogPanel = new JPanel(new BorderLayout());
					dialogPanel.setBackground(Config.WHITE);
					dialogPanel.add(p_preview, BorderLayout.WEST);
					dialogPanel.add(contentPanel, BorderLayout.CENTER);

					// 최종 다이얼로그에 세팅
					dialog.add(dialogPanel);
					dialog.setVisible(true);
				}
			}
		});

		btnDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (mainLayout.user.getRole().getRole_id() == 3) {
					JOptionPane.showMessageDialog(ProductListPanel.this, "상품 수정 권한 없음");
				} else {

					Connection con1 = null;
					int row = table.getSelectedRow();
					if (row == -1) {
						JOptionPane.showMessageDialog(ProductListPanel.this, "삭제할 상품을 선택하세요.");
						return;
					}

					int result = JOptionPane.showConfirmDialog(ProductListPanel.this, "선택한 상품을 삭제하시겠습니까?", "확인",
							JOptionPane.YES_NO_OPTION);
					if (result != JOptionPane.YES_OPTION)
						return;

					ProductOption selectedOption = model.getProductOptionAt(table.getSelectedRow());
					Product selectedProduct = selectedOption.getProduct();

					try {
						con1 = dbManager.getConnection();
						con1.setAutoCommit(false); // 수동 트랜잭션 시작
						System.out.println("AutoCommit: " + con1.getAutoCommit());

						productOptionDAO.delete(selectedOption.getOption_id(), con1);
						productDAO.delete(selectedProduct.getProduct_id(), con1);

						con1.commit(); // 모든 delete가 성공하면 커밋
						refresh();
						JOptionPane.showMessageDialog(ProductListPanel.this, "삭제가 완료되었습니다.");
					} catch (Exception ex) {
						ex.printStackTrace();
						try {
							con1.rollback(); // 하나라도 실패하면 롤백
						} catch (SQLException rollbackEx) {
							rollbackEx.printStackTrace();
						}
						JOptionPane.showMessageDialog(ProductListPanel.this, "삭제 중 오류가 발생했습니다.");
					} finally {
						try {
							con1.setAutoCommit(true); // 다시 자동 커밋 모드로 돌려놓기
						} catch (SQLException setAutoCommitEx) {
							setAutoCommitEx.printStackTrace();
						}
					}
				}
			}
		});

		// 테이블 생성
		model = new ProductModel(mainLayout.user);
		table = new JTable(model);

		// 테이블 header 스타일 추가적으로 적용 가능
		JTableHeader header = table.getTableHeader();
		header.setBackground(Config.LIGHT_GREEN);
		header.setFont(new Font("Noto Sans KR", Font.BOLD, 15));
		header.setPreferredSize(new Dimension(Integer.MIN_VALUE, 33));
		header.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

		int[] columnWidths = { 110, 110, 200, 85, 80, 80, 70, 70 };



		scroll = new JScrollPane(table);
		scroll.setBackground(Config.WHITE); // scroll 자체도 같은 배경색으로
		scroll.getViewport().setBackground(Config.WHITE);

		// scroll을 감싸는 패널 생성 (여백 + 테두리 적용)
		JPanel scrollWrapper = new JPanel(new BorderLayout());
		scrollWrapper.setBackground(Config.WHITE);
		scrollWrapper.setBorder(BorderFactory.createEmptyBorder(15, 25, 10, 15));

		scrollWrapper.add(scroll, BorderLayout.CENTER);

		TableUtil.tableStyleUtil(table, scroll, 700, true);
		SwingUtilities.invokeLater(() -> {
			for (int i = 0; i < table.getColumnCount(); i++) {
				table.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
				table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
			}
		});

		add(titlePanel, BorderLayout.NORTH);
		add(buttonPanel, BorderLayout.EAST);
		add(scrollWrapper, BorderLayout.CENTER);

		btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (mainLayout.user.getRole().getRole_id() == 3) {
					JOptionPane.showMessageDialog(ProductListPanel.this, "상품 수정 권한 없음");
				} else {
					JDialog dialog = new JDialog();
					dialog.setTitle("상품 등록");
					dialog.setSize(600, 500);
					dialog.setLocationRelativeTo(null);
					dialog.setModal(true);

					JPanel contentPanel = new JPanel(new GridBagLayout());
					GridBagConstraints gbc = new GridBagConstraints();
					gbc.insets = new Insets(10, 10, 10, 10);
					gbc.fill = GridBagConstraints.HORIZONTAL;

					JLabel lblBrand = new JLabel("브랜드명:");
					cbBrand = new JComboBox<>();
					cbBrand.setPreferredSize(new Dimension(200, 30));
					cbBrand.setUI(new ComboBoxUtil());  
					ComboBoxUtil.applyDefaultStyle(cbBrand); 
					for (Brand b : new BrandDAO().selectAll())
						cbBrand.addItem(b);

					JLabel lblName = new JLabel("상품명:");
					tfName = new JTextField();
					tfName.setPreferredSize(new Dimension(200, 30));

					JLabel lblCategory = new JLabel("카테고리:");
					cbCategory = new JComboBox<>();
					cbCategory.setPreferredSize(new Dimension(200, 30));
					cbCategory.setUI(new ComboBoxUtil());
					ComboBoxUtil.applyDefaultStyle(cbCategory);

					Category dummy = new Category();
					dummy.setCt_name("카테고리를 선택하세요");
					dummy.setCt_id(0);
					cbCategory.addItem(dummy);

					List<Category> categories = new CategoryDAO().selectAll();
					for (Category c : categories)
						cbCategory.addItem(c);

					// 최상위 카테고리에 이벤트 연결
					cbCategory.addItemListener(new ItemListener() {
						public void itemStateChanged(ItemEvent e) {
							if (e.getStateChange() == ItemEvent.SELECTED) {
								Category category = (Category) cbCategory.getSelectedItem();

								getCategoryDetail(category);
							}
						}
					});

					JLabel lblCategoryDetail = new JLabel("상세 카테고리:");
					cbCategoryDetail = new JComboBox<>();
					cbCategoryDetail.setPreferredSize(new Dimension(200, 30));
					cbCategoryDetail.setUI(new ComboBoxUtil());
					ComboBoxUtil.applyDefaultStyle(cbCategoryDetail);

					JLabel lblOptionName = new JLabel("상품 옵션명:");
					tfOptionName = new JTextField();
					tfOptionName.setPreferredSize(new Dimension(200, 30));

					JLabel lblPrice = new JLabel("가격:");
					tfPrice = new JTextField();
					tfPrice.setPreferredSize(new Dimension(200, 30));

					JLabel lblActive = new JLabel("활성화:");
					cbActive = new JComboBox<>(new String[] { "y", "n" });
					cbActive.setPreferredSize(new Dimension(200, 30));
					cbActive.setUI(new ComboBoxUtil());
					ComboBoxUtil.applyDefaultStyle(cbActive);

					bt_open = ButtonUtil.greenButtonUtil("상품 사진 등록");
//					bt_open = new JButton("상품사진 등록");

					p_preview = new JPanel() {
						protected void paintComponent(Graphics g) {
							super.paintComponent(g);

							g.drawImage(img, 20, 100, 150, 150, this);

						}
					}; // 추후 익명 내부 클래스로 전환

					chooser = new JFileChooser("C:\\public");

					// 파일 탐색기 띄우기
					bt_open.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							int result = chooser.showOpenDialog(ProductListPanel.this);

							if (result == JFileChooser.APPROVE_OPTION)
								preview();
						}
					});

					// 콤보박스 가운데 정렬 렌더러
					DefaultListCellRenderer centerRenderer = new DefaultListCellRenderer();
					centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

					cbBrand.setRenderer(centerRenderer);
					cbCategory.setRenderer(centerRenderer);
					cbCategoryDetail.setRenderer(centerRenderer);
					cbActive.setRenderer(centerRenderer);

					// 스타일
					Color dialogBgColor = Config.WHITE;
					Color labelColor = new Color(60, 60, 60);

					contentPanel.setBackground(dialogBgColor);
					lblBrand.setForeground(labelColor);
					lblName.setForeground(labelColor);
					lblCategory.setForeground(labelColor);
					lblCategoryDetail.setForeground(labelColor);
					lblOptionName.setForeground(labelColor);
					lblPrice.setForeground(labelColor);
					lblActive.setForeground(labelColor);

					// 텍스트 필드 가운데 정렬
					tfName.setHorizontalAlignment(SwingConstants.CENTER);
					tfPrice.setHorizontalAlignment(SwingConstants.CENTER);
					tfOptionName.setHorizontalAlignment(SwingConstants.CENTER);

					gbc.gridx = 0;
					gbc.gridy = 0;
					contentPanel.add(lblBrand, gbc);
					gbc.gridx = 1;
					contentPanel.add(cbBrand, gbc);

					gbc.gridx = 0;
					gbc.gridy++;
					contentPanel.add(lblName, gbc);
					gbc.gridx = 1;
					contentPanel.add(tfName, gbc);

					gbc.gridx = 0;
					gbc.gridy++;
					contentPanel.add(lblCategory, gbc);
					gbc.gridx = 1;
					contentPanel.add(cbCategory, gbc);

					gbc.gridx = 0;
					gbc.gridy++;
					contentPanel.add(lblCategoryDetail, gbc);
					gbc.gridx = 1;
					contentPanel.add(cbCategoryDetail, gbc);

					gbc.gridx = 0;
					gbc.gridy++;
					contentPanel.add(lblOptionName, gbc);
					gbc.gridx = 1;
					contentPanel.add(tfOptionName, gbc);

					gbc.gridx = 0;
					gbc.gridy++;
					contentPanel.add(lblPrice, gbc);
					gbc.gridx = 1;
					contentPanel.add(tfPrice, gbc);

					gbc.gridx = 0;
					gbc.gridy++;
					contentPanel.add(lblActive, gbc);
					gbc.gridx = 1;
					contentPanel.add(cbActive, gbc);

					bt_open.setPreferredSize(new Dimension(130, 25)); // 가로 80px, 세로 25px
					gbc.gridx = 1;
					gbc.gridy++;
					gbc.anchor = GridBagConstraints.EAST; // 버튼을 오른쪽 정렬 (선택사항)
					gbc.fill = GridBagConstraints.NONE;    // 버튼 크기 강제
					gbc.weightx = 0;
					contentPanel.add(bt_open, gbc);

					JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));

//					JButton btnSave = new JButton("저장");
					JButton btnSave = ButtonUtil.blueButtonUtil("저장");
					gbc.gridx = 0;
					gbc.gridy++;
					gbc.gridwidth = 2;
					// 저장 버튼 스타일
					btnSave.setPreferredSize(new Dimension(100, 35));
					btnSave.setFont(new Font("SansSerif", Font.BOLD, 13));
					btnSave.setBackground(new Color(130, 180, 250));
					btnSave.setForeground(Color.WHITE);
					btnSave.setFocusPainted(false);
//                btnSave.setBorder(BorderFactory.createLineBorder(new Color(100, 150, 220)));
					btnSave.setCursor(new Cursor(Cursor.HAND_CURSOR));
					
					// 저장 버튼 위치 설정 (기존 설정 변경)
					gbc.anchor = GridBagConstraints.WEST;
					gbc.fill = GridBagConstraints.NONE; // 이걸로 크기 강제
					gbc.weightx = 0; // 공간 분배 없음
					btnPanel.add(btnSave, gbc);

					JButton btnCancel = new JButton("취소");

					// 취소 버튼 스타일
					btnCancel.setPreferredSize(new Dimension(100, 35));
					btnCancel.setFont(new Font("SansSerif", Font.BOLD, 13));
					btnCancel.setBackground(Color.gray);
					btnCancel.setForeground(Color.WHITE);
					btnCancel.setFocusPainted(false);
//					btnCancel.setBorder(BorderFactory.createLineBorder(new Color(100, 150, 220)));
					btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
					// 취소 버튼 위치 설정 (기존 설정 변경)
					gbc.anchor = GridBagConstraints.EAST;
					gbc.fill = GridBagConstraints.NONE; // 이걸로 크기 강제
					gbc.weightx = 0; // 공간 분배 없음
					btnPanel.add(btnCancel, gbc);

					btnSave.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							insert();
							
							mainLayout.setDataDirty(true);
							mainLayout.refreshIfDirty();
							
							JOptionPane.showMessageDialog(dialog, "상품이 등록되었습니다.");
							dialog.dispose();
						}
					});
					btnCancel.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							dialog.dispose();
						}
					});


					gbc.anchor = GridBagConstraints.SOUTH;
					contentPanel.add(btnPanel, gbc);

					btnPanel.setBackground(Config.WHITE); // 다이얼로그 배경색과 맞춤
					// 미리보기 설정중
					p_preview.setPreferredSize(new Dimension(180, 200)); // 원하는 크기로 설정

					// 기존 contentPanel은 그대로 유지
					JPanel dialogPanel = new JPanel(new BorderLayout());
					dialogPanel.add(p_preview, BorderLayout.WEST);
					dialogPanel.add(contentPanel, BorderLayout.CENTER);

					// 마지막에 Dialog에 붙이기
					dialog.add(dialogPanel);
					// 미리보기 설정중
//					dialog.add(contentPanel);
					dialog.setVisible(true);
				}
			}
		});

		// 1. 정렬 기능 설정
		TableRowSorter<TableModel> sorter_list = new TableRowSorter<>(table.getModel());
		table.setRowSorter(sorter_list);

		// 2. 헤더 클릭 이벤트로 정렬 상태 출력
		JTableHeader header_list = table.getTableHeader();
		header_list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int columnIndex = header_list.columnAtPoint(e.getPoint());
				String columnName = table.getColumnName(columnIndex);
				SortOrder order = getSortOrder(sorter_list, columnIndex);

//                System.out.println("헤더 클릭됨: " + columnName + " (인덱스: " + columnIndex + ")");
//                if (order == SortOrder.ASCENDING) {
//                    System.out.println("정렬 방향: 오름차순");
//                } else if (order == SortOrder.DESCENDING) {
//                    System.out.println("정렬 방향: 내림차순");
//                } else {
//                    System.out.println("정렬 방향 없음");
//                }
			}

			private SortOrder getSortOrder(TableRowSorter<?> sorter, int columnIndex) {
				for (RowSorter.SortKey key : sorter.getSortKeys()) {
					if (key.getColumn() == columnIndex) {
						return key.getSortOrder();
					}
				}
				return SortOrder.UNSORTED;
			}
		});
	}

	// 미리보기
	public void preview() {
		// 유저가 선택한 파일 얻기
		file = chooser.getSelectedFile();

		// 파일로부터 이미지 생성
		try {
			BufferedImage buffrImg = ImageIO.read(file);
			img = buffrImg.getScaledInstance(150, 150, Image.SCALE_SMOOTH); // 단일 이미지 사용
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// 그림 다시 그리기
		p_preview.repaint();
	}

	public void getCategoryDetail(Category category) {
		// 하위 카테고리 목록 가져오기
		List<CategoryDetail> detail = categoryDetailDAO.selectByCategoryId(category.getCt_id());

		// 모든 하위 카테고리 콤보아이템 지우기
		cbCategoryDetail.removeAllItems();

		CategoryDetail dummy = new CategoryDetail();
		dummy.setCt_dt_name("상세카테고리를 선택하세요");
		dummy.setCt_dt_id(0);
		cbCategoryDetail.addItem(dummy);

		// 서브 카테고리 수만큼 반복하면서, 두번째 콤포박스에 SubCategory 모델을 채워넣기
		for (int i = 0; i < detail.size(); i++) {
			CategoryDetail categoryDetail = detail.get(i);// i번째 요소 꺼내기
			cbCategoryDetail.addItem(categoryDetail);
		}
	};

	// Edit 전용 하위 카테고리 채우기 메서드
	private void fillCategoryDetailForEdit(Category selectedCategory, JComboBox<CategoryDetail> cbCategoryDetail) {
		List<CategoryDetail> detailList = categoryDetailDAO.selectByCategoryId(selectedCategory.getCt_id());
		cbCategoryDetail.removeAllItems();
		for (CategoryDetail detail : detailList) {
			cbCategoryDetail.addItem(detail);
		}
	}

	public void insert() {
		// mysql에서 트랜잭션이 적용되려면, 4개의 DAO 모두 같은 Connection이어야 한다
		Connection con = dbManager.getConnection();

		try {
			con.setAutoCommit(false);

			Category category = (Category) cbCategory.getSelectedItem();
			CategoryDetail categoryDetail = (CategoryDetail) cbCategoryDetail.getSelectedItem();
			Brand brand = (Brand) cbBrand.getSelectedItem();
			String active = (String) cbActive.getSelectedItem();
			int optionNum = 0;

			Product product = new Product();

			product.setCategory(category);
			product.setProduct_name(tfName.getText());
			product.setCategory_detail(categoryDetail);
			product.setBrand(brand);

			productDAO.insert(product);

			int product_id = productDAO.selectRecentPk();
			product.setProduct_id(product_id);// 구해온 최신 pk를 Product에 반영

			ProductOption productOption = new ProductOption();

			productOption.setOption_active(active);
			productOption.setOption_name(tfOptionName.getText());
			productOption.setPrice(Integer.parseInt(tfPrice.getText()));
			productOption.setProduct(product);

			if (active.equals("y")) {
				int maxOptionNo = productOptionDAO.selectMaxOptionNo(product_id);
				optionNum = maxOptionNo + 1;
			} else if (active.equals("n")) {
				optionNum = 99;
			}
			productOption.setOption_no(optionNum);

			StringBuffer codeMaker = new StringBuffer();
			codeMaker.append(category.getCt_code());
			codeMaker.append("-");
			codeMaker.append(categoryDetail.getCt_dt_code());
			codeMaker.append("-");
			codeMaker.append(brand.getBd_code());
			codeMaker.append("-");
			codeMaker.append(50 + optionNum);

			productOption.setOption_code(codeMaker.toString());

			productOptionDAO.insert(productOption);

			int productOption_id = productOptionDAO.selectRecentPk();
			productOption.setOption_id(productOption_id);

			// 상품에 딸려있는 이미지 등록
			ProductImg productImg = new ProductImg();
			productImg.setProductOption(productOption); // 1) 어떤 상품에..
			productImg.setImg_filename(file.getName()); // 2) 어떤 파일명으로..
			productImgDAO.insert(productImg, con);

			con.commit();
			mainLayout.setDataDirty(true);
			mainLayout.refreshIfDirty();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ProductException e) {
			e.printStackTrace();
		} finally {
			try {
				con.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
