package com.amazonaws.lambda.demo;

public class DeleteOldSchedulesRequest {
	int days;
	String password;

	public DeleteOldSchedulesRequest(int days, String password) {
		this.days = days;
		this.password = password;
	}
	public String toString() {
		return "Fetch(" + Integer.toString(days) + ")";
	}
}
