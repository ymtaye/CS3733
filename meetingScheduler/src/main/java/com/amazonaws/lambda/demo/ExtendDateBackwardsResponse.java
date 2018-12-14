package com.amazonaws.lambda.demo;

public class ExtendDateBackwardsResponse {
	String response;
	int httpCode;
	
	public ExtendDateBackwardsResponse (String s, int code) {
		this.response = s;
		this.httpCode = code;
	}
	
	// 200 means success
	public ExtendDateBackwardsResponse (String s) {
		this.response = s;
		this.httpCode = 200;
	}
	
	public String toString() {
		return "Response(" + response + ")";
	}
}
