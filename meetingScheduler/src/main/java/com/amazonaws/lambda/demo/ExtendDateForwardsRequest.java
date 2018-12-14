package com.amazonaws.lambda.demo;

public class ExtendDateForwardsRequest {
	String scheduleid;
	String secretcode;
	String newEndDate;
	
	public ExtendDateForwardsRequest(String scheduleid, String secretcode, String newEndDate) {
		this.scheduleid = scheduleid;
		this.secretcode = secretcode;
		this.newEndDate = newEndDate;
	}
	
	public String toString() {
		return "Extend(" + scheduleid + ", " + secretcode + ", " + newEndDate + ")";
	}
}
