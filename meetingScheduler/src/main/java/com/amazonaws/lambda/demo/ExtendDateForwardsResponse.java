package com.amazonaws.lambda.demo;

public class ExtendDateForwardsResponse {
	String response;
	int httpCode;
	
	public ExtendDateForwardsResponse (String s, int code) {
		this.response = s;
		this.httpCode = code;
	}
	
	// 200 means success
	public ExtendDateForwardsResponse (String s) {
		this.response = s;
		this.httpCode = 200;
	}
	
	public String toString() {
		return "Response(" + response + ")";
	}

}
