package com.amazonaws.lambda.demo;

public class ShowScheduleRequest {
	String scheduleid;
	
	public ShowScheduleRequest (String scheduleid) {
		this.scheduleid = scheduleid;
	}
	
	public String toString() {
		return "Add(" + scheduleid + ")";
	}
}
