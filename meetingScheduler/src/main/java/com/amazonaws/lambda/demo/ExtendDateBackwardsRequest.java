package com.amazonaws.lambda.demo;

public class ExtendDateBackwardsRequest {
	String scheduleid;
	String secretcode;
	String newStartDate;
	
	public ExtendDateBackwardsRequest(String scheduleid, String secretcode, String newStartDate){
		this.scheduleid = scheduleid;
		this.secretcode = secretcode;
		this.newStartDate = newStartDate;
	}
	public String toString() {
		return "Extend backwards(" + scheduleid + ", " + newStartDate + ")";
	}
}
