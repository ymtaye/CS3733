package com.amazonaws.lambda.demo;

public class DeleteOldSchedulesResponse {
	String response;
	int httpCode;
	
	public DeleteOldSchedulesResponse (String s, int code) {
		this.response = s;
		this.httpCode = code;
	}
	
	// 200 means success
	public DeleteOldSchedulesResponse (String s) {
		this.response = s;
		this.httpCode = 200;
	}
	
	public String toString() {
		return "Response(" + response + ")";
	}
}
