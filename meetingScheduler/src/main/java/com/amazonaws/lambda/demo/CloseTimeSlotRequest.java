package com.amazonaws.lambda.demo;

public class CloseTimeSlotRequest {
	String sID;
	String starttime;
	String startdate;
	int close;
	
	public CloseTimeSlotRequest(String sID, String starttime, String startdate) {
		this.sID = sID;
		this.starttime = starttime;
		this.startdate = startdate;
		this.close = 1;
	}
	public String toString() {
		return "Fetch(" + sID + "," + starttime + "," + startdate + "," + Integer.toString(close) + ")";
	}
}
