package com.fvthree.blogpost.exceptions;

public class HTTP400Exception extends RuntimeException {
	
	private static final long serialVersionUID = 2522133760325645649L;

	public HTTP400Exception() {
		super();
	}
	
	public HTTP400Exception(String message, Throwable cause) {
		super(message,cause);
	}
	
	public HTTP400Exception(String message) {
		super(message);
	}
	
	public HTTP400Exception(Throwable cause) {
		super(cause);
	}
}
