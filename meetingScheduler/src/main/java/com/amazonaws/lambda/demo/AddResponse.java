package com.amazonaws.lambda.demo;

public class AddResponse {
	double value;
	int httpCode;
	
	public AddResponse (double v, int code) {
		this.value = v;
		this.httpCode = code;
	}
	
	// 200 means success
	public AddResponse (double v) {
		this.value = v;
		this.httpCode = 200;
	}
	
	public String toString() {
		return "Value(" + value + ")";
	}
}
