package com.amazonaws.lambda.demo;

public class EditTimeslotResponse {
	String response;
	int httpCode;
	
	public EditTimeslotResponse (String s, int code) {
		this.response = s;
		this.httpCode = code;
	}
	
	// 200 means success
	public EditTimeslotResponse (String s) {
		this.response = s;
		this.httpCode = 200;
	}
	
	public String toString() {
		return "Response(" + response + ")";
	}


}
