package com.amazonaws.lambda.demo;

public class OpenTimeSlotRequest {
	String sID;
	String starttime;
	String startdate;
	int open;
	
	public OpenTimeSlotRequest(String sID, String starttime, String startdate) {
		this.sID = sID;
		this.starttime = starttime;
		this.startdate = startdate;
		this.open = 0;
	}
	
	public String toString() {
		return "Fetch(" + sID + ", " + starttime + ", " + startdate + ", " + Integer.toString(open) + ")";
	}

}
