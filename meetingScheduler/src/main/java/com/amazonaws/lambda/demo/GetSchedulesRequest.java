package com.amazonaws.lambda.demo;

public class GetSchedulesRequest {
    int hours;
	
	public GetSchedulesRequest (int hours) {
		this.hours = hours;
	}
	
	public String toString() {
		return "Add(" + Integer.toString(hours) + ")";
	}
}

	

