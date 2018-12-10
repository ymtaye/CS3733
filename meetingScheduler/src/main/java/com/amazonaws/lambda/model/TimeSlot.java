package com.amazonaws.lambda.model;

public class TimeSlot {
	public String startdate;
	public String enddate;
	public String starttime;
	public String endtime;
	public String participant;
	public String scheduleid;
	public int available;
	String secretcode;
	
	public TimeSlot (String secretcode, String startdate, String enddate, String starttime, String endtime, String p, String s, int available) {
		this.secretcode = secretcode;
		this.startdate = startdate;
		this.enddate = enddate;
		this.starttime = starttime;
		this.endtime = endtime;
		this.participant = p;
		this.scheduleid = s;
		this.available = available;
	}
	
	public String getSecretcode() {
		return this.secretcode;
	}
	
	public boolean isAvailable () {
		if(this.available == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public int closeSlot() {
		if(this.available == 1) {
			return 1;
		}
		this.available = 1;
		return 0;
	}
	
	public int openSlot() {
		if(this.available == 0) {
			return 0;
		}
		this.available = 0;
		return 1;
	}

	
}
