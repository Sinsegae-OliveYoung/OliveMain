package com.olive.store.report.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.geom.Ellipse2D;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;

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
import com.olive.common.repository.BranchDAO;
import com.olive.common.repository.StockDAO;
import com.olive.common.util.style.ComboBoxUtil;
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

	JPanel p_combo;
	JComboBox<Integer> cb_year;

	JPanel p_content;
	XYDataset dataset; // 데이터 집합
	final JFreeChart chart; // 데이터 집합을 포함하는 차트
	final ChartPanel chartPanel; // 차트 전용 패널

	StockDAO stockDAO;
	BranchDAO branchDAO;
	List<Map<String, Integer>> salesList = null;
	List<Branch> brList = null;

	LocalDate now;
	int year;

	int userId;

	public ReportTotalMenu(MainLayout mainLayout) {
		super(mainLayout);

		// create
		p_title = new JPanel();
		lb_title = new JLabel("기간별 총 매출");

		p_combo = new JPanel();
		cb_year = new JComboBox<Integer>();

		p_content = new JPanel();
		chart = createChart(dataset);
		chartPanel = new ChartPanel(chart);

		stockDAO = new StockDAO();
		branchDAO = new BranchDAO();
		salesList = new ArrayList<>();
		brList = new ArrayList<>();

		now = LocalDate.now();
		year = now.getYear();

		userId = mainLayout.user.getUser_id();
		dataset = createSalesDataset(year); // 데이터 생성하기
		setCombobox();
		chart.getXYPlot().setDataset(dataset); // 차트에 데이터 세팅

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

		cb_year.setUI(new ComboBoxUtil());
		cb_year.setPreferredSize(new Dimension(200, 30));
		cb_year.setBorder(new LineBorder(Color.GRAY, 1, true));

		p_content.setBackground(Config.WHITE);

		chartPanel.setPreferredSize(new Dimension(Config.CONTENT_W - 50, 550));

		// assemble
		p_title.add(lb_title);
		add(p_title);

		p_combo.add(cb_year);
		add(p_combo);

		p_content.add(chartPanel);
		add(p_content);

		// listener
		cb_year.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				year = (Integer) cb_year.getSelectedItem();
				dataset = createSalesDataset(year); // 데이터 생성하기
				chart.getXYPlot().setDataset(dataset); // 차트에 데이터 세팅
			}
		});
	}

	// 데이터 집합을 생성하는 메서드
	public XYDataset createSalesDataset(int year) {
		brList = branchDAO.getBranchList(userId); // 유저에 해당되는 지점 리스트를 가져옴
		XYSeriesCollection dataset = new XYSeriesCollection(); // 데이터 집합 생성
		XYSeries series = new XYSeries("소속 지점 매출 합"); // 데이터명 설정

		// 1~12월 매출 0으로 초기화
		Map<Integer, Integer> totalSalesMap = new HashMap<>(); // 1~12월까지의 데이터를 세팅할 맵 선언
		for (int i = 1; i <= 12; i++)
			totalSalesMap.put(i, 0); // 누락된 월 방지

		// 각 지점의 매출 데이터를 누적
		for (Branch branch : brList) {
			List<Map<String, Integer>> salesMapList = stockDAO.selectSales(branch.getBr_id()); // 해당 지점 매출 리스트 저장

			// 원하는 년도의 데이터 불러오기
			for (Map<String, Integer> sale : salesMapList) {
				if (sale.get("Year") == year) {
					int month = sale.get("Month");
					int sales = sale.get("Sales") / 1000000;
					totalSalesMap.put(month, totalSalesMap.get(month) + sales);
				}
			}
		}

		// 차트 데이터셋에 추가
		for (int i = 1; i <= 12; i++)
			series.add(i, totalSalesMap.get(i));

		dataset.addSeries(series); // 집합에 데이터 삽입
		return dataset;
	}

	// 차트를 생성하는 메서드
	private JFreeChart createChart(XYDataset dataset) {

		// 라인 차트 선언, 세팅 (차트이름, x축이름, y축이름, 데이터셋, 플롯방향, 범례, 툴팁, url)
		JFreeChart chart = ChartFactory.createXYLineChart("", "월", "단위: 백만원", dataset, PlotOrientation.VERTICAL, true,
				true, false);
		chart.setBackgroundPaint(Config.WHITE); // 차트 배경색
		chart.getLegend().setBorder(0, 0, 0, 0); // 범례 테두리
		chart.getTitle().setPadding(-20, 0, 20, 0); // 위쪽 간격
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
		// rangeAxis.setRange(1000, 10000); // y축 눈금의 범위: 1~12
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits()); // 눈금의 기준: 정수
		// rangeAxis.setTickUnit(new NumberTickUnit(0.5)); // 눈금의 기준: 실수

		return chart;
	}

	public void refresh() {
		chart.getXYPlot().setDataset(createSalesDataset(year));
	}
	
	// 콤보박스에 모든 년도 추가
	public void setCombobox() {
		Set<Integer> yearSet = new HashSet<>(); // 중복 방지를 위한 Set 변수

		for (Branch branch : brList)
			salesList = stockDAO.selectSales(branch.getBr_id()); // 해당 지점 매출 리스트 저장

			// Set에 추가
		for (Map<String, Integer> sale : salesList) {
			yearSet.add(sale.get("Year"));
		}

		// 리스트로 변환 후 내림차순 정렬
		List<Integer> yearList = new ArrayList<>(yearSet);
		Collections.sort(yearList, Collections.reverseOrder());

		// 콤보박스에 추가
		for (int year : yearList) {
			cb_year.addItem(year);
		}
	}

	// 참고자료
	// http://www.java2s.com/Code/Java/Chart/JFreeChartLineChartDemo6.htm
	// http://www.java2s.com/Code/Java/Chart/JFreeChartBarChartDemo8.htm
	// https://www.jfree.org/jfreechart/devguide.html
}
