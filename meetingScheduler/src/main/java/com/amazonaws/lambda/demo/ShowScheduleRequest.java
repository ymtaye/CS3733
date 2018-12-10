package com.amazonaws.lambda.demo;

public class ShowScheduleRequest {
	
	String secretcode;
	
	public ShowScheduleRequest (String secretcode) {
		this.secretcode = secretcode;
	}
	
	public String toString() {
		return "Add(" + secretcode + ")";
	}
}
