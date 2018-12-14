package com.amazonaws.lambda.demo;

public class CreateScheduleRequest {
	String startdate;
	String enddate;
	String daystarthour;
	String dayendhour;
	String organizer;
	int meetinglength;
	
	public CreateScheduleRequest (String startdate, String enddate, String starttime, String endtime, String organizer, int meetinglength) {
		this.startdate = startdate;
		this.enddate = enddate;
		this.daystarthour = starttime;
		this.dayendhour = endtime;
		this.organizer = organizer;
		this.meetinglength = meetinglength;
	}
	
	public String toString() {
		return "Create(" + startdate + ", " + enddate + ", " + daystarthour + ", " + dayendhour + ", " + organizer + ")";
	}
}
