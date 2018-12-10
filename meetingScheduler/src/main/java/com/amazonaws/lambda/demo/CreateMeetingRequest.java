package com.amazonaws.lambda.demo;

public class CreateMeetingRequest {
	String startdate;
	String enddate;
	String starttime;
	String endtime;
	String participant;
	String scheduleID;
	
	public CreateMeetingRequest (String startdate, String enddate, String starttime, String endtime, String participant, String scheduleID) {
		this.startdate = startdate;
		this.enddate = enddate;
		this.starttime = starttime;
		this.endtime = endtime;
		this.participant = participant;
		this.scheduleID = scheduleID;
	}
	
	public String toString() {
		return "Create(" + startdate + "," + enddate + "," + starttime + "," + endtime + "," + participant + "," + scheduleID + ")";
	}
}
