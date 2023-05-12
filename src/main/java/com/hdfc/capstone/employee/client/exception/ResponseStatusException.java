package com.hdfc.capstone.employee.client.exception;

public class ResponseStatusException extends Exception {

	@Override
	public String getMessage() {
		return "Enter Only Long Values";
	}
	
}
