package com.amazonaws.lambda.demo;

public class OpenTimeRequest {
	String scheduleid;
	String secretcode;
	String time;
	
	public OpenTimeRequest(String scheduleid, String secretcode, String time) {
		this.scheduleid = scheduleid;
		this.secretcode = secretcode;
		this.time = time;
	}
	
	public String toString() {
		return "Open all(" + time + ", " + scheduleid + ")";
	}

}
