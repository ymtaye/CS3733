package com.amazonaws.lambda.demo;

public class DeleteOldSchedulesRequest {
	int days;

	public DeleteOldSchedulesRequest(int days) {
		this.days = days;
	}
	public String toString() {
		return "Fetch(" + Integer.toString(days) + ")";
	}
}
