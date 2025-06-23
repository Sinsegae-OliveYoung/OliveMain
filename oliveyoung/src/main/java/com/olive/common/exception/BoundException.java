package com.olive.common.exception;

public class BoundException extends RuntimeException{
	public BoundException (String msg) {
		super(msg);
	}
	public BoundException (Throwable e) {
		super(e);
	}
	public BoundException (String msg, Throwable e) {
		super(msg, e);
	}
}
