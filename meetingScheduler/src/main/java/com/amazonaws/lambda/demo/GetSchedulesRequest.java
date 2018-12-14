package com.amazonaws.lambda.demo;

public class GetSchedulesRequest {
    int hours;
    String password;
	
	public GetSchedulesRequest (int hours, String password) {
		this.hours = hours;
		this.password = password;
	}
	
	public String toString() {
		return "Add(" + Integer.toString(hours) + ")";
	}
}

	

