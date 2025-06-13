package com.olive.common.config;

import java.awt.Color;

/*-------------------------------------------
 * 상수 정의 클래스
 * ------------------------------------------- */

public class Config {

	public static final String url = "jdbc:mysql://localhost:3306/olive";
	public static final String user = "olive";
	public static final String pass = "1234";
	
	public static final String LOGO_PATH = "C:/github/lecture_workspace/java_workspace/TeamProject_1/logo2.png";
	
	/*--------------------------------------------
	 *  페이지 정의
	 ---------------------------------------------*/
	public static final int MAIN_PAGE = 0;
	public static final int BOUND_PAGE = 1;
	public static final int STOCK_PAGE = 2;
	public static final int SCHEDULE_PAGE = 3;
	public static final int STORE_PAGE = 4;
	public static final int CONFIG_PAGE = 5;
	
	/*--------------------------------------------
	 *  메인 레이아웃 설정
	 ---------------------------------------------*/
	public static final int LAYOUT_W = 1300;
	public static final int LAYOUT_H = 800;
	public static final int NAVI_W = LAYOUT_W;
	public static final int NAVI_H = 60;
	public static final int SIDE_W = 200;
	public static final int SIDE_H = LAYOUT_H - NAVI_H;
	public static final int CONTENT_W = LAYOUT_W - SIDE_W;
	public static final int CONTENT_H = LAYOUT_H - NAVI_H;
	 

	/*--------------------------------------------
	 *  색상 선언
	 ---------------------------------------------*/
	public static final Color DARK_GREEN = new Color(0, 180, 100);
	public static final Color GREEN = new Color(130, 220, 40);
	public static final Color LIGHT_GREEN = new Color(200,245,156);
	public static final Color LIGHT_GRAY = new Color(246, 246, 246);
	public static final Color WHITE = new Color(252,252,252);
	public static final Color PINK = new Color(255,120,120);
	
}
