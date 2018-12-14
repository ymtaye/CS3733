package com.amazonaws.lambda.demo;

public class CloseTimeResponse {
	String response;
	int httpCode;
	
	public CloseTimeResponse (String s, int code) {
		this.response = s;
		this.httpCode = code;
	}
	
	// 200 means success
	public CloseTimeResponse (String s) {
		this.response = s;
		this.httpCode = 200;
	}
	
	public String toString() {
		return "Response(" + response + ")";
	}
}
