package com.olive.manage;

import java.awt.Font;

public class ManageConfig {

	/**
	 * 폰트
	 */
	public static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 22);
	public static final Font PLAIN_FONT = new Font("Noto Sans KR", Font.PLAIN, 16);
	public static final Font BOLD_FONT = new Font("Noto Sans KR", Font.BOLD, 16);
	
	/**
	 * 이미지 경로 
	 */
	public static final String CALENDAR_IMAGE = "images/calendar_icon.png";
	
	/**
	 * 각 manage panel에 대응되는 cardlayout의 value값
	 * 
	 */
	public static final String USER_LIST_KEY = "USER_LIST";
	public static final String USER_DETAIL_KEY = "USER_DETAIL";
	public static final String APPROVAL_LIST_KEY = "APPROVAL_LIST";
	public static final String APPROVAL_DETAIL_KEY = "APPROVAL_DETAIL";
	
	/**
	 * 각 manage panel에 보여줄 title
	 * 
	 */
	public static final String USER_LIST_TITLE = "사용자 목록";
	public static final String USER_DETAIL_TITLE= "사용자 상세정보";
	public static final String APPROVAL_LIST_TITLE= "결재 목록";
	public static final String APPROVAL_DETAIL_TITLE= "결재 상세 정보";
	
	
}
