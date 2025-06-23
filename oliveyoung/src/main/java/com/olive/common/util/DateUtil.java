package com.olive.common.util;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtil {
	
	/**
	 * String -> java.sql.Date 형식으로 변환 
	 * @param dateStr : yyyy.mm.dd 형식의 문자열 
	 */
	public static Date stringToDate(String dateStr) {

        // 1. 포맷에 맞게 DateTimeFormatter 생성
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");

        // 2. LocalDate로 파싱
        LocalDate localDate = LocalDate.parse(dateStr, formatter);

        // 3. LocalDate → java.sql.Date
        return Date.valueOf(localDate);
	}

}
