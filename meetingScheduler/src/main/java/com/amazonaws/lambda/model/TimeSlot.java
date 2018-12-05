package com.amazonaws.lambda.model;
//testing
public class TimeSlot {
	public String startdate;
	public String enddate;
	public String starttime;
	public String endtime;
	public String participant;
	public Schedule schedule;
	String secretcode;
	
	public TimeSlot (String secretcode, String startdate, String enddate, String starttime, String endtime, String p, Schedule s) {
		this.secretcode = secretcode;
		this.startdate = startdate;
		this.enddate = enddate;
		this.starttime = starttime;
		this.endtime = endtime;
		this.participant = p;
		this.schedule = s;
	}
	
}
