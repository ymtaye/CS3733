package com.amazonaws.lambda.demo;

public class CreateScheduleRequest {
	String startdate;
	String enddate;
	String daystarthour;
	String dayendhour;
	String organizer;
	
	public CreateScheduleRequest (String startdate, String enddate, String starttime, String endtime, String organizer) {
		this.startdate = startdate;
		this.enddate = enddate;
		this.daystarthour = starttime;
		this.dayendhour = endtime;
		this.organizer = organizer;
	}
	
	public String toString() {
		return "Create(" + startdate + "," + enddate + "," + daystarthour + "," + dayendhour + "," + organizer + ")";
	}
}
