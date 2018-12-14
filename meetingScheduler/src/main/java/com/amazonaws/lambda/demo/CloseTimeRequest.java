package com.amazonaws.lambda.demo;

public class CloseTimeRequest {
	String scheduleid;
	String secretcode;
	String time;
	
	public CloseTimeRequest(String scheduleid, String secretcode, String time) {
		this.scheduleid = scheduleid;
		this.secretcode = secretcode;
		this.time = time;
	}
	public String toString() {
		return "Close all(" + time + ", " + scheduleid + ")";
	}
}
