package com.amazonaws.lambda.demo;

public class CloseTimeSlotResponse {
	String response;
	int httpCode;
	
	public CloseTimeSlotResponse (String s, int code) {
		this.response = s;
		this.httpCode = code;
	}
	
	// 200 means success
	public CloseTimeSlotResponse (String s) {
		this.response = s;
		this.httpCode = 200;
	}
	
	public String toString() {
		return "Response(" + response + ")";
	}

}
