package com.amazonaws.lambda.model;

import java.sql.Date;
import java.sql.Time;
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
	public java.sql.Date creationDate;
	public java.sql.Time creationTime;

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
	public Schedule (String id, String secretcode, String startdate, String enddate, String daystarthour, String dayendhour, String organizer,  Date date, Time time) {
		this.secretcode = secretcode;
		this.startdate = startdate;
		this.enddate = enddate;
		this.daystarthour = daystarthour;
		this.dayendhour = dayendhour;
		this.organizer = organizer;
		this.id = id;
		this.creationDate = date;
		this.creationTime = time;
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
