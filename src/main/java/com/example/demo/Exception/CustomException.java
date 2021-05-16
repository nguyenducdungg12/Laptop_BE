package com.example.demo.Exception;

public class CustomException extends RuntimeException{
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public CustomException(String exMessage, Exception exception) {
	        super(exMessage, exception);
	    }
	    public CustomException(String exMessage) {
	        super(exMessage);
	    }
}
