package com.amazonaws.lambda.demo;

public class DeletedScheduleRequest {
	String secretCode;
	
	public  DeletedScheduleRequest (String secretCode) {
		this.secretCode=secretCode;
	}
	public String toString() {
		return "Fetch(" + secretCode + ")";
}

	

}
