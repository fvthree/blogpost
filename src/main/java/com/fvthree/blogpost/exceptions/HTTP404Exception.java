package com.fvthree.blogpost.exceptions;

public class HTTP404Exception extends RuntimeException {

	private static final long serialVersionUID = -4809214211570240855L;

	public HTTP404Exception() {
		super();
	}
	
	public HTTP404Exception(String message, Throwable cause) {
		super(message, cause);
	}
	
	public HTTP404Exception(String message) {
		super(message);
	}
	
	public HTTP404Exception(Throwable cause) {
		super(cause);
	}

}
