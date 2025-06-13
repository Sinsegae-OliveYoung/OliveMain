package com.olive.common.util;

import java.io.File;

/* 애플리케이션 전반에 걸쳐 유용하게 사용할 공통코드 중 파일 관련된 기능*/
public class FileUtil {
	
	/* ---------------------------
	 *  확장자 반환하는 메서드
	 * --------------------------- */
	public static String getExt(String path) {
		// 1) 가장 마지막 점의 위치를 알아내기
		// 2) 가장 마지막 점의 위치 바로 다음 위치부터 가장 마지막 문자열까지 추출하기
		return path.substring(path.lastIndexOf(".")+1, path.length());
	}

	/* --------------------------------------
	 *  중복되지 않는 이름으로 새로운 파일 생성
	 *  targetDir: 파일이 새로 생성될 위치
	 * -------------------------------------- */
	public static File createFile(String targetDir, String ext) {
		long time = System.currentTimeMillis();

		// File.separator: os에 맞춰 디렉토리 사이 구분값(슬래시)을 넣어줌
		String filename = targetDir + File.separator + time + "." + ext;
		
		return new File(filename);
	}
}
