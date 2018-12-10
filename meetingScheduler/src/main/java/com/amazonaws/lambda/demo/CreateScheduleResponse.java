package com.amazonaws.lambda.demo;

public class CreateScheduleResponse {
	String secretcode;
	String scheduleid;
	String response;
	int httpCode;
	
	public CreateScheduleResponse (String scheduleid, String secretcode, int code) {
		this.scheduleid = scheduleid;
		this.secretcode = secretcode;
		this.httpCode = code;
	}
	
	// 200 means success
	public CreateScheduleResponse (String scheduleid, String secretcode) {
		this.scheduleid = scheduleid;
		this.secretcode = secretcode;
		this.httpCode = 200;
	}
	
	public CreateScheduleResponse (String response) {
		this.response = response;
		this.httpCode = 200;
	}
	
	public CreateScheduleResponse (String response, int code) {
		this.response = response;
		this.httpCode = code;
	}
	
	public String toString() {
		return "Response(" + scheduleid +"," +secretcode+ ")";
	}
}
