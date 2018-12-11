package com.amazonaws.lambda.model;

import java.util.ArrayList;

public class Schedule {
	public String id;
	public String startdate;
	public String enddate;
	public String daystarthour;
	public String dayendhour;
	public String organizer;
	String secretcode;
	public ArrayList<TimeSlot> timeslots;
	public int meetinglength; // minutes
	public String creationTime;
	public String creationDate;

	public Schedule (String id, String startdate, String enddate, String starttime, String endtime, String organizer, String secretcode, int meetinglength) {
		this.secretcode = secretcode;
		this.startdate = startdate;
		this.enddate = enddate;
		this.daystarthour = starttime;
		this.dayendhour = endtime;
		this.organizer = organizer;
		this.id = id;
		this.meetinglength = meetinglength;
	}

	public String getsecretcode() {
		return this.secretcode;
	}
	
	public void initTimeSlots(ArrayList<TimeSlot> timeslots) {
		for(int i=0;i<timeslots.size();i++) {
			this.timeslots.add(timeslots.get(i));
		}
	}
	

}
