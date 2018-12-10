package com.amazonaws.lambda.demo;

public class DeleteMeetingRequest {
	String scheduleid;
	String secretcode;
	String starttime;
	String startdate;

	public DeleteMeetingRequest(String scheduleid, String secretcode, String starttime, String startdate) {
		this.scheduleid = scheduleid;
		this.secretcode = secretcode;
		this.startdate = startdate;
		this.starttime = starttime;
	}
	public String toString() {
		return "Delete(" + scheduleid + "," + secretcode + ")";
	}
}
