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
import java.net.URL;
import java.net.http.HttpClient;
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
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

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
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 10, 20, 30));

		Font buttonFont = new Font("SansSerif", Font.PLAIN, 13);
		Dimension buttonSize = new Dimension(100, 30);
		Color buttonText = new Color(40, 40, 40);

		JButton btnAdd = ButtonUtil.greenButtonUtil("상품 등록");
		JButton btnEdit = ButtonUtil.greenButtonUtil("상품 상세");
		JButton btnDelete = ButtonUtil.pinkButtonUtil("상품 삭제");

		JButton[] buttons = { btnAdd, btnEdit, btnDelete };

		for (JButton btn : buttons) {
			buttonPanel.add(btn);
			btn.setPreferredSize(buttonSize);
			btn.setMaximumSize(buttonSize);
			buttonPanel.add(Box.createRigidArea(new Dimension(0, 30)));
		}
		
		btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (mainLayout.user.getRole().getRole_id() == 3) {
					JOptionPane.showMessageDialog(ProductListPanel.this, "상품 등록 권한 없음");
					return;
				}

				new ProductDialog(mainLayout, null, null).setVisible(true);
	            
	            mainLayout.setDataDirty(true); 
	            mainLayout.refreshIfDirty();
			}
		});

		btnEdit.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
//		        if (mainLayout.user.getRole().getRole_id() != 1) {
//		            JOptionPane.showMessageDialog(ProductListPanel.this, "상품 수정 권한 없음");
//		            return;
//		        }

		        int row = table.getSelectedRow();
		        if (row == -1) {
		            JOptionPane.showMessageDialog(ProductListPanel.this, "수정할 상품을 선택하세요.");
		            return;
		        } else {
		            int modelRow = table.convertRowIndexToModel(row);
		            ProductOption selectedOption = model.getProductOptionAt(modelRow);
		            Product selectedProduct = selectedOption.getProduct();

		            new ProductDialog(mainLayout, selectedOption, selectedProduct).setVisible(true);

		            mainLayout.setDataDirty(true); 
		            mainLayout.refreshIfDirty();
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
			            
			            mainLayout.setDataDirty(true); 
			            mainLayout.refreshIfDirty();
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

		for (int i = 0; i < table.getColumnCount(); i++) {
			table.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
			table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}

		scroll = new JScrollPane(table);
		scroll.setBackground(Config.WHITE); // scroll 자체도 같은 배경색으로
		scroll.getViewport().setBackground(Config.WHITE);

		// scroll을 감싸는 패널 생성 (여백 + 테두리 적용)
		JPanel scrollWrapper = new JPanel(new BorderLayout());
		scrollWrapper.setBackground(Config.WHITE);
		scrollWrapper.setBorder(BorderFactory.createEmptyBorder(25, 25, 10, 15));

		scrollWrapper.add(scroll, BorderLayout.CENTER);

		TableUtil.tableStyleUtil(table, scroll, 700, true);

		add(titlePanel, BorderLayout.NORTH);
		add(buttonPanel, BorderLayout.EAST);
		add(scrollWrapper, BorderLayout.CENTER);


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

}