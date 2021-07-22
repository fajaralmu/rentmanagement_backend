package com.fajar.rentmanagement.exception;

public class ApplicationException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7610558300205998680L;

	public ApplicationException(String message) {
		super(message);
	}

	public ApplicationException(Exception ex) {
		super(ex.getMessage());
	}
  
}
