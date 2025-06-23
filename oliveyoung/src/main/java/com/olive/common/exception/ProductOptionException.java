package com.olive.common.exception;

public class ProductOptionException extends RuntimeException{
	
	public ProductOptionException(String msg) {
		super(msg);
	}
	
	// 예외 객체들의 최상위 객체가 바로 Throwable 인터페이스이다
	// 따라서 어떠한 예외가 전달되어도, 다 받을 수 있는 자료형 이다
	public ProductOptionException(Throwable e) {
		super(e);
	}
	
	public ProductOptionException(String msg, Throwable e) {
		super(msg, e);
	}
	
	
}
