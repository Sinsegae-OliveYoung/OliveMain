package com.olive.store.report.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Paint;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

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
import com.olive.common.view.Panel;
import com.olive.mainlayout.MainLayout;
import com.olive.store.StorePage;

/* -------------------------
 *  가장 많이 팔린 상품 n개
 *  : 출고량순 order by limit
 * ------------------------- */

public class ReportProductMenu extends Panel {

	JPanel p_title;
	JLabel lb_title;

	JPanel p_content;
	CategoryDataset dataset; 		// 데이터 집합
	JFreeChart chart; 					// 데이터 집합을 포함하는 차트
	ChartPanel chartPanel; 			// 차트 전용 패널

	public ReportProductMenu(MainLayout mainLayout) {
		super(mainLayout);

		// create
		p_title = new JPanel();
		lb_title = new JLabel("상품별 매출");

		p_content = new JPanel();
		dataset = createDataset();
		chart = createChart(dataset);
		chartPanel = new ChartPanel(chart);

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

		p_content.setBackground(Config.WHITE);

		chartPanel.setPreferredSize(new Dimension(Config.CONTENT_W - 50, 570));

		// assemble
		p_title.add(lb_title);
		add(p_title);

		p_content.add(chartPanel);
		add(p_content);
	}

	private CategoryDataset createDataset() {
		double[][] data = new double[][] { { 20, 20, 18, 15, 13, 12, 11, 8, 7, 7, 4, 1 } };
		return DatasetUtilities.createCategoryDataset("Series ", "Category ", data);
	}

	private JFreeChart createChart(CategoryDataset dataset) {

		JFreeChart chart = ChartFactory.createBarChart("", "월", "판매 수량", dataset, PlotOrientation.VERTICAL, false, true,
				false);
		chart.setBackgroundPaint(Config.WHITE);
		chart.getTitle().setPadding(10, 0, 20, 0); // 위쪽 간격

		CategoryPlot plot = chart.getCategoryPlot();
		plot.setNoDataMessage("데이터 없음");
		plot.setRangeGridlinesVisible(false);
		plot.setBackgroundPaint(Config.WHITE); // 플롯 배경색
		plot.setInsets(new RectangleInsets(10, 10, 10, 10)); // 그래프 내부 여백
		plot.getDomainAxis().setLabelFont(new Font("Noto Sans KR", Font.PLAIN, 12)); // plot 내부 X축 라벨
		plot.getRangeAxis().setLabelFont(new Font("Noto Sans KR", Font.PLAIN, 12)); // plot 내부 Y축 라벨
		plot.setOutlineVisible(false);

		List<Color> colors = CustomColors(12);
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
