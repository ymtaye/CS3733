package com.amazonaws.lambda.demo;

public class ParticipantShowScheduleRequest {
    String scheduleid;  
	
	public 	ParticipantShowScheduleRequest (String scheduleid) {
		this.scheduleid = scheduleid;
		
	}	
	public String toString() {
		return "Add(" + scheduleid + ")";
	}

}
