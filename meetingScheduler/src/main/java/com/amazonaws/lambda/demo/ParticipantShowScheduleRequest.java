package com.amazonaws.lambda.demo;

public class ParticipantShowScheduleRequest {
    String secretcode;  
	
	public 	ParticipantShowScheduleRequest (String secretcode) {
		this.secretcode = secretcode;
		
	}	
	public String toString() {
		return "Add(" + secretcode + ")";
	}

}
