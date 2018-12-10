package com.amazonaws.lambda.demo;

public class DeleteMeetingRequest {
	String scheduleid;
	String secretcode;

	public DeleteMeetingRequest(String scheduleid, String secretcode) {
		this.scheduleid = scheduleid;
		this.secretcode = secretcode;
	}
	public String toString() {
		return "Delete(" + scheduleid + "," + secretcode + ")";
	}
}
