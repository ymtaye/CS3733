package com.amazonaws.lambda.demo;

public class EditTimeslotRequest {
	String secretcode;
    String day;
	
	String action;   // Represents whether user wants to close/open
	
	public EditTimeslotRequest (String sec, String day, String open) {
		this.day = day;
		
		this.action = open;
		this.secretcode =sec;
	}
	public String toString() {
		return "Add(" + secretcode + day + action + ")";
	}
	
	

	public String getDay() {
		return day;
	}
	public String getOpen() {
		return action;
	}
}




