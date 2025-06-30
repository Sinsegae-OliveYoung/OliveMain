package com.olive.common.exception;

public class BranchException extends RuntimeException{
	public BranchException (String msg) {
		super(msg);
	}
	public BranchException (Throwable e) {
		super(e);
	}
	public BranchException (String msg, Throwable e) {
		super(msg, e);
	}
}
