package com.amazonaws.lambda.demo;

public class DeletedScheduleResponse {
	String response;
	int httpCode;
	
	public DeletedScheduleResponse (String s, int code) {
		this.response = s;
		this.httpCode = code;
	}
	
	// 200 means success
	public DeletedScheduleResponse (String s) {
		this.response = s;
		this.httpCode = 200;
	}
	
	public String toString() {
		return "Response(" + response + ")";
	}


}
