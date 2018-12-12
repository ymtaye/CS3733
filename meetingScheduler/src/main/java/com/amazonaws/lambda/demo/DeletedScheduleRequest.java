package com.amazonaws.lambda.demo;

public class DeletedScheduleRequest {
	String sID;
	
	public  DeletedScheduleRequest (String sID) {
		this.sID=sID;
	}
	public String toString() {
		return "Fetch(" + sID + ")";
}

	

}
