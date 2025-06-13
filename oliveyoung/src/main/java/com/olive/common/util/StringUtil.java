package com.olive.common.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StringUtil {
	
	public static String getSecuredPass(String pwd) {
		// javaSE는 암호화 알고리즘 함수를 보유하고 있음
		String pass = pwd;
		StringBuffer sb = new StringBuffer();			// String의 불변적 특징을 해결한 객체
		
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] hash = md.digest(pass.getBytes("UTF-8"));
			
			// 잘게 쪼개진 byte[] hash를 16진수 문자열로 반환
			
			for (int i=0;i<hash.length;i++) {
				/* 음수를 변경하려고 하면 ffffff(부호비트)가 앞에 덧붙여진 16진수가 나옴,
				 * 부호비트는 암호화에 사용되지 않으므로 제거해야 함.
				 * 이를 제거하기 위해 0xff를 대상으로 &연산자를 사용하여 true 값만 추출 */
				String hex = Integer.toHexString(0xff & hash[i]);
				
				// 16진수가 두 자릿 값으로 나오지 않고 한 자릿수로 나오는 문제를 해결
				if (hex.length()<2) sb.append("0");
				sb.append(hex);				// 스트링 누적
			}
			System.out.println(sb.toString());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return sb.toString();
	}
}
