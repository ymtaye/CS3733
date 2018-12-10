package com.amazonaws.lambda.demo;

public class DeleteMeetingResponse {
	String response;
	int httpCode;
	
	public DeleteMeetingResponse (String s, int code) {
		this.response = s;
		this.httpCode = code;
	}
	
	// 200 means success
	public DeleteMeetingResponse (String s) {
		this.response = s;
		this.httpCode = 200;
	}
	
	public String toString() {
		return "Response(" + response + ")";
	}

}
