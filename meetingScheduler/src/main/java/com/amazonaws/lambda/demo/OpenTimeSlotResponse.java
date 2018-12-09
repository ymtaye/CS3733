package com.amazonaws.lambda.demo;

public class OpenTimeSlotResponse {
	String response;
	int httpCode;
	
	public OpenTimeSlotResponse (String s, int code) {
		this.response = s;
		this.httpCode = code;
	}
	
	// 200 means success
	public OpenTimeSlotResponse (String s) {
		this.response = s;
		this.httpCode = 200;
	}
	
	public String toString() {
		return "Response(" + response + ")";
	}

}
