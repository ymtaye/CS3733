package com.amazonaws.lambda.demo;

public class OpenTimeResponse {
	String response;
	int httpCode;
	
	public OpenTimeResponse (String s, int code) {
		this.response = s;
		this.httpCode = code;
	}
	
	// 200 means success
	public OpenTimeResponse (String s) {
		this.response = s;
		this.httpCode = 200;
	}
	
	public String toString() {
		return "Response(" + response + ")";
	}

}
