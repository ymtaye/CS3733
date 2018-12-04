package com.amazonaws.lambda.model;

public class Schedule {
	public String id;
	public String startdate;
	public String enddate;
	public String daystarthour;
	public String dayendhour;
	public String organizer;
	String secretcode;
	public ArrayList<TimeSlot> timeslots;

	public Schedule (String id, String secretcode, String startdate, String enddate, String starttime, String endtime, String organizer) {
		this.secretcode = secretcode;
		this.startdate = startdate;
		this.enddate = enddate;
		this.daystarthour = starttime;
		this.dayendhour = endtime;
		this.organizer = organizer;
		this.id = id;
	}
	
	public String getsecretcode() {
		return this.secretcode;
	}
	
}