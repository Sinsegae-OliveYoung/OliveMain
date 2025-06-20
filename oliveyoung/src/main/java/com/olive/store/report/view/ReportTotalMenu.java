package com.olive.store.report.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.geom.Ellipse2D;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleInsets;

import com.olive.common.config.Config;
import com.olive.common.model.Branch;
import com.olive.common.model.Stock;
import com.olive.common.repository.BranchDAO;
import com.olive.common.repository.StockDAO;
import com.olive.common.view.Panel;
import com.olive.mainlayout.MainLayout;

/* -------------------------
 *  팀장 - 모든 지점의 총 매출 (월별)
 *  : 출고량 * 가격
 *  
 *  점장 - 해당 지점의 총 매출 (월별)
 *  : 출고량 * 가격
 * ------------------------- */

public class ReportTotalMenu extends Panel {

	JPanel p_title;
	JLabel lb_title;

	JPanel p_content;
	XYDataset dataset; // 데이터 집합
	final JFreeChart chart; // 데이터 집합을 포함하는 차트
	final ChartPanel chartPanel; // 차트 전용 패널

	StockDAO stockDAO;
	BranchDAO branchDAO;
	List<Stock> stList = new ArrayList<>();
	List<Branch> brList = new ArrayList<>();

	Date today;
	//SimpleDateFormat
	
	int userId;
	int year, month;

	public ReportTotalMenu(MainLayout mainLayout) {
		super(mainLayout);

		// create
		p_title = new JPanel();
		lb_title = new JLabel("월별 총 매출");

		p_content = new JPanel();
		dataset = createDataset();
		chart = createChart(dataset);
		chartPanel = new ChartPanel(chart);

		stockDAO = new StockDAO();
		branchDAO = new BranchDAO();
		
		today = new Date();

		userId = mainLayout.user.getUser_id();
		
		//year = DateFormat.

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

	public void loadData() {
		brList = branchDAO.getBranchList(userId);

		for (int i = 0; i < brList.size(); i++) {
			stList = stockDAO.selectBrSales(year, month, (branchDAO.getBranchList(userId)).get(i).getBr_id());
		}
		
		//createDataset()
	}

	// 데이터 집합을 생성하는 메서드
	public XYDataset createDataset() {

		// 하나의 데이터 선언, 세팅
		XYSeries series = new XYSeries("동작구");
		series.add(1, 4000);
		series.add(2, 2000);
		series.add(3, 8000);
		series.add(4, 6000);
		series.add(5, 8000);
		series.add(6, 7000);
		series.add(7, 4000);
		series.add(8, 3000);
		series.add(9, 9000);
		series.add(10, 5000);
		series.add(11, 2000);
		series.add(12, 7000);

		XYSeriesCollection dataset = new XYSeriesCollection(); // 데이터 집합 생성
		dataset.addSeries(series); // 집합에 데이터 삽입

		return dataset;
	}

	// 차트를 생성하는 메서드
	private JFreeChart createChart(XYDataset dataset) {

		// 라인 차트 선언, 세팅 (차트이름, x축이름, y축이름, 데이터셋, 플롯방향, 범례, 툴팁, url)
		JFreeChart chart = ChartFactory.createXYLineChart("", "월", "단위: 만원", dataset, PlotOrientation.VERTICAL, true,
				true, false);
		chart.setBackgroundPaint(Config.WHITE); // 차트 배경색
		chart.getLegend().setBorder(0, 0, 0, 0); // 범례 테두리
		chart.getTitle().setPadding(10, 0, 20, 0); // 위쪽 간격
		chart.getLegend().setItemFont(new Font("Noto Sans KR", Font.PLAIN, 12)); // 범례 폰트
		// chart.setTitle(new TextTitle("총 매출", new Font("Noto Sans KR", Font.BOLD,
		// 15))); // 차트 제목
		// chart.setBorderVisible(true); // 테두리 표시
		// chart.setBorderPaint(Color.GRAY); // 테두리 설정

		// 플롯 선언, 세팅
		XYPlot plot = chart.getXYPlot();
		plot.setBackgroundPaint(Config.WHITE); // 플롯 배경색
		plot.setDomainGridlinePaint(Color.LIGHT_GRAY); // x축그리드 색상
		plot.setRangeGridlinePaint(Color.LIGHT_GRAY); // y축그리드 색상
		plot.getDomainAxis().setLabelFont(new Font("Noto Sans KR", Font.PLAIN, 12)); // plot 내부 X축 라벨
		plot.getRangeAxis().setLabelFont(new Font("Noto Sans KR", Font.PLAIN, 12)); // plot 내부 Y축 라벨
		plot.setInsets(new RectangleInsets(10, 10, 10, 10)); // 그래프 내부 여백

		// 데이터 세팅
		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		renderer.setSeriesPaint(0, Config.DARK_GREEN); // 첫번째 데이터: 색상
		renderer.setSeriesStroke(0, new BasicStroke(2.0f)); // 첫번째 데이터: 선 스타일
		renderer.setSeriesLinesVisible(0, true); // 첫번째 데이터: 선 활성화
		renderer.setSeriesShape(0, new Ellipse2D.Double(-3.0, -3.0, 6.0, 6.0)); // 첫번째 데이터: 도형 스타일
		renderer.setSeriesShapesVisible(0, false); // 첫번째 데이터: 도형 활성화
		plot.setRenderer(renderer);

		// x축 범례 세팅
		NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
		domainAxis.setRange(1, 12); // x축 눈금의 범위: 1~12
		domainAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits()); // 눈금의 기준: 정수

		// y축 범례 세팅
		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setRange(1000, 10000); // y축 눈금의 범위: 1~12
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits()); // 눈금의 기준: 정수
		// rangeAxis.setTickUnit(new NumberTickUnit(0.5)); // 눈금의 기준: 실수

		return chart;
	}

	// 참고자료
	// http://www.java2s.com/Code/Java/Chart/JFreeChartLineChartDemo6.htm
	// http://www.java2s.com/Code/Java/Chart/JFreeChartBarChartDemo8.htm
	// https://www.jfree.org/jfreechart/devguide.html
}
