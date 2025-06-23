package com.olive.store.report.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Paint;
import java.awt.event.ItemEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.ui.RectangleInsets;

import com.olive.common.config.Config;
import com.olive.common.repository.BranchDAO;
import com.olive.common.repository.OutBoundDAO;
import com.olive.common.util.style.ComboBoxUtil;
import com.olive.common.view.Panel;
import com.olive.mainlayout.MainLayout;

/* -------------------------
 *  가장 많이 팔린 상품 n개
 *  : 출고량순 order by limit
 * ------------------------- */

public class ReportProductMenu extends Panel {

	JPanel p_title;
	JLabel lb_title;

	JPanel p_combo;
	JComboBox<String> cb_months;
	String[] cb_items = {"최근 3개월", "최근 6개월", "최근 12개월"};
	
	JPanel p_content;
	CategoryDataset dataset; 		// 데이터 집합
	JFreeChart chart; 					// 데이터 집합을 포함하는 차트
	ChartPanel chartPanel; 			// 차트 전용 패널

	OutBoundDAO outBoundDAO;
	BranchDAO branchDAO;
	List<Map<String, String>> productList = null;
	List<String> productName = new ArrayList<>();
	List<Double> quantities = new ArrayList<>();

	String months;
	int userId;

	public ReportProductMenu(MainLayout mainLayout) {
		super(mainLayout);

		// create
		p_title = new JPanel();
		lb_title = new JLabel("상품별 매출");

		p_combo = new JPanel();
		cb_months = new JComboBox<String>();

		p_content = new JPanel();
		chart = createChart(dataset);
		chartPanel = new ChartPanel(chart);

		outBoundDAO = new OutBoundDAO();
		branchDAO = new BranchDAO();
		productList = new ArrayList<>();

		userId = mainLayout.user.getUser_id();
		dataset = createDataset(3);
		chart.getCategoryPlot().setDataset(dataset); // 차트에 데이터 세팅
		
		// style
		setLayout(new FlowLayout());
		setPreferredSize(new Dimension(Config.CONTENT_W, Config.CONTENT_H));
		setBackground(Config.WHITE);

		p_title.setPreferredSize(new Dimension(Config.CONTENT_W, 60));
		p_title.setBorder(BorderFactory.createEmptyBorder(20, 40, 0, 0));
		p_title.setLayout(new FlowLayout(FlowLayout.LEFT));
		p_title.setOpaque(false);

		lb_title.setFont(new Font("Noto Sans KR", Font.BOLD, 26));
		lb_title.setHorizontalAlignment(JLabel.RIGHT);

		p_combo.setPreferredSize(new Dimension(Config.CONTENT_W, 35));
		p_combo.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 50));
		p_combo.setLayout(new FlowLayout(FlowLayout.RIGHT));
		p_combo.setOpaque(false);

		cb_months.setUI(new ComboBoxUtil());
		cb_months.setPreferredSize(new Dimension(200, 30));
		cb_months.setBorder(new LineBorder(Color.GRAY, 1, true));

		p_content.setBackground(Config.WHITE);

		chartPanel.setPreferredSize(new Dimension(Config.CONTENT_W - 50, 550));

		// assemble
		p_title.add(lb_title);
		add(p_title);

	    for (String item : cb_items) cb_months.addItem(item);
		p_combo.add(cb_months);
		add(p_combo);

		p_content.add(chartPanel);
		add(p_content);
		
		// listener
		cb_months.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				months = cb_months.getSelectedItem().toString();
				productName.clear();
				quantities.clear();
				if (months.equals("최근 3개월")) dataset = createDataset(3);
				else if (months.equals("최근 6개월")) dataset = createDataset(6);
				else if (months.equals("최근 12개월")) dataset = createDataset(12);
				chart.getCategoryPlot().setDataset(dataset); // 차트에 데이터 세팅
			}
		});
	}

	private CategoryDataset createDataset(int months) {
		productList = outBoundDAO.getTopProduct(months);
		
		for (Map<String, String> topProduct : productList) {
			String name = topProduct.get("Name");
			double quantity = Double.parseDouble(topProduct.get("Quantity"));
			
			productName.add(name);
			quantities.add(quantity);
		}

		String[] names = productName.toArray(new String[0]); 
		String[] key = {"판매량"};
		
        double[][] data = new double[1][quantities.size()];
        for (int i = 0; i < quantities.size(); i++)
        	data[0][i] = quantities.get(i);
        
        return DatasetUtilities.createCategoryDataset(key, names, data);
	}

	private JFreeChart createChart(CategoryDataset dataset) {

		JFreeChart chart = ChartFactory.createBarChart("", "", "판매 수량", dataset, PlotOrientation.VERTICAL, false, true,
				false);

		chart.setBackgroundPaint(Config.WHITE);
		chart.getTitle().setPadding(-20, 0, 20, 0); // 위쪽 간격

		CategoryPlot plot = chart.getCategoryPlot();
		plot.setNoDataMessage("데이터 없음");
		plot.setRangeGridlinesVisible(false);
		plot.setBackgroundPaint(Config.WHITE); // 플롯 배경색
		plot.setInsets(new RectangleInsets(10, 10, 10, 10)); // 그래프 내부 여백
		plot.getDomainAxis().setLabelFont(new Font("Noto Sans KR", Font.BOLD, 8)); // plot 내부 X축 라벨
		plot.getDomainAxis().setTickLabelFont(new Font("Noto Sans KR", Font.BOLD, 8)); // plot 내부 X축 라벨
		plot.getRangeAxis().setLabelFont(new Font("Noto Sans KR", Font.PLAIN, 12)); // plot 내부 Y축 라벨
		plot.getRangeAxis().setTickLabelFont(new Font("Noto Sans KR", Font.PLAIN, 12)); // plot 내부 Y축 라벨
		plot.setOutlineVisible(false);

		List<Color> colors = CustomColors(6);
		CustomRenderer renderer = new CustomRenderer(colors);
		renderer.setBarPainter(new StandardBarPainter()); // 기본 페인터로 설정 (입체감 제거)
		renderer.setShadowVisible(false); // 그림자 제거
		plot.setRenderer(renderer);

		renderer.setBaseToolTipGenerator(
				new StandardCategoryToolTipGenerator("{0} - {1} : {2}", new DecimalFormat("#,##0")));

		ValueAxis rangeAxis = plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		rangeAxis.setLowerMargin(0.15);
		rangeAxis.setUpperMargin(0.15);

		return chart;

	}

	public List CustomColors(int index) {

		List<Color> colors = new ArrayList<>();

		// 시작색 (진한 초록) → 끝색 (연한 초록)
		int r1 = 0, g1 = 180, b1 = 100;
		int r2 = 200, g2 = 245, b2 = 156;

		for (int i = 0; i < index; i++) {
			float ratio = (float) i / (index - 1);

			int r = (int) (r1 + (r2 - r1) * ratio);
			int g = (int) (g1 + (g2 - g1) * ratio);
			int b = (int) (b1 + (b2 - b1) * ratio);

			colors.add(new Color(r, g, b));
		}
		return colors;

	}

	class CustomRenderer extends BarRenderer {

		private List<Color> colors;

		public CustomRenderer(List<Color> colors) {
			this.colors = colors;
		}

		public Paint getItemPaint(int row, int col) {
			return colors.get(col % colors.size());
		}
	}
}
