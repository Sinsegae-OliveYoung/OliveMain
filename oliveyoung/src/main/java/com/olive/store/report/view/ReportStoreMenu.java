package com.olive.store.report.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.geom.Ellipse2D;

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
import com.olive.common.view.Panel;
import com.olive.mainlayout.MainLayout;


/* ----------------------------
 *  지점별 매출 (월별)
 *  : 출고량 * 가격 where 지점
 * ---------------------------- */

public class ReportStoreMenu extends Panel{

	JPanel p_title;
	JLabel lb_title;

	JPanel p_content;
	XYDataset dataset;				// 데이터 집합
	final JFreeChart chart;			// 데이터 집합을 포함하는 차트
	final ChartPanel chartPanel;	// 차트 전용 패널
	
	public ReportStoreMenu(MainLayout mainLayout) {
		super(mainLayout);

		// create
		p_title = new JPanel();
		lb_title = new JLabel("지점별 매출");

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
        
        chartPanel.setPreferredSize(new Dimension(Config.CONTENT_W-50, 570));
        
        // assemble
		p_title.add(lb_title);
		add(p_title);

        p_content.add(chartPanel);
		add(p_content);
	}

	// 데이터 집합을 생성하는 메서드
	public XYDataset createDataset() {

		// 하나의 데이터 선언, 세팅
		XYSeries sinSin = new XYSeries("신세계점");
		sinSin.add(1, 4000);
		sinSin.add(2, 2000);
		sinSin.add(3, 8000);
		sinSin.add(4, 6000);
		sinSin.add(5, 8000);
		sinSin.add(6, 7000);
		sinSin.add(7, 4000);
		sinSin.add(8, 3000);
		sinSin.add(9, 9000);
		sinSin.add(10, 5000);
		sinSin.add(11, 2000);
		sinSin.add(12, 7000);

		// 하나의 데이터 선언, 세팅
		XYSeries sinBong = new XYSeries("아이앤씨점");
		sinBong.add(1, 5000);
		sinBong.add(2, 6000);
		sinBong.add(3, 4000);
		sinBong.add(4, 8000);
		sinBong.add(5, 6000);
		sinBong.add(6, 7000);
		sinBong.add(7, 5000);
		sinBong.add(8, 3000);
		sinBong.add(9, 7000);
		sinBong.add(10, 6000);
		sinBong.add(11, 2000);
		sinBong.add(12, 4000);
		
		XYSeriesCollection dataset = new XYSeriesCollection();	// 데이터 집합 생성
		dataset.addSeries(sinSin);												// 집합에 데이터 삽입
		dataset.addSeries(sinBong);												
		
		return dataset;
	}

	// 차트를 생성하는 메서드
	private JFreeChart createChart(XYDataset dataset) {
		
		// 라인 차트 선언, 세팅 (차트이름, x축이름, y축이름, 데이터셋, 플롯방향, 범례, 툴팁, url)
		JFreeChart chart = ChartFactory.createXYLineChart("", "월", "단위: 만원", dataset, PlotOrientation.VERTICAL, true, true, false);		
		chart.setBackgroundPaint(Config.WHITE);  														// 차트 배경색
		chart.getLegend().setBorder(0,0,0,0); 																// 범례 테두리
		chart.getTitle().setPadding(10, 0, 20, 0);  															// 위쪽 간격	
		chart.getLegend().setItemFont(new Font("Noto Sans KR", Font.PLAIN, 12));			// 범례 폰트
		     
		// 플롯 선언, 세팅
		XYPlot plot = chart.getXYPlot();
		plot.setBackgroundPaint(Config.WHITE);															// 플롯 배경색
		plot.setDomainGridlinePaint(Color.LIGHT_GRAY);												// x축그리드 색상
		plot.setRangeGridlinePaint(Color.LIGHT_GRAY);													// y축그리드 색상
		plot.setInsets(new RectangleInsets(10, 10, 10, 10)); 											// 그래프 내부 여백
		plot.getDomainAxis().setLabelFont(new Font("Noto Sans KR", Font.PLAIN, 12)); 	// plot 내부 X축 라벨
		plot.getRangeAxis().setLabelFont(new Font("Noto Sans KR", Font.PLAIN, 12)); 		// plot 내부 Y축 라벨
		
		// 데이터 세팅
		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		
		renderer.setSeriesPaint(0,  Config.DARK_GREEN);												// 첫번째 데이터: 색상
		renderer.setSeriesStroke(0, new BasicStroke(2.0f)); 											// 첫번째 데이터: 선 스타일
		renderer.setSeriesLinesVisible(0, true);																// 첫번째 데이터: 선 활성화
		renderer.setSeriesShape(0, new Ellipse2D.Double(-3.0, -3.0, 6.0, 6.0));					// 첫번째 데이터: 도형 스타일
		renderer.setSeriesShapesVisible(0, false);  														// 첫번째 데이터: 도형 활성화
		plot.setRenderer(renderer);
		
		renderer.setSeriesPaint(1,  Config.GREEN);															// 첫번째 데이터: 색상
		renderer.setSeriesStroke(1, new BasicStroke(2.0f)); 											// 첫번째 데이터: 선 스타일
		renderer.setSeriesLinesVisible(1, true);																// 첫번째 데이터: 선 활성화
		renderer.setSeriesShape(1, new Ellipse2D.Double(-3.0, -3.0, 6.0, 6.0));					// 첫번째 데이터: 도형 스타일
		renderer.setSeriesShapesVisible(1, false);  														// 첫번째 데이터: 도형 활성화
		plot.setRenderer(renderer);
        
        // x축 범례 세팅
        NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
        domainAxis.setRange(1, 12); 																			// x축 눈금의 범위: 1~12
        domainAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());				// 눈금의 기준: 정수
  		
		// y축 범례 세팅
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setRange(1000, 10000); 																	// y축 눈금의 범위: 1~12
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());					// 눈금의 기준: 정수
        // rangeAxis.setTickUnit(new NumberTickUnit(0.5));												// 눈금의 기준: 실수
      
		return chart;
	}
	
	// 참고자료
	// http://www.java2s.com/Code/Java/Chart/JFreeChartLineChartDemo6.htm
	// http://www.java2s.com/Code/Java/Chart/JFreeChartBarChartDemo3differentcolorswithinaseries.htm
	// https://www.jfree.org/jfreechart/devguide.html
}
