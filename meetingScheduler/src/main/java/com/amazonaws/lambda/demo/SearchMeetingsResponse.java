package com.amazonaws.lambda.demo;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.lambda.model.TimeSlot;

public class SearchMeetingsResponse {

	List<TimeSlot> list;
	int httpCode;

	public SearchMeetingsResponse(List<TimeSlot> list2, int code) {
		this.list = list2;
		this.httpCode = code;

	}

	public SearchMeetingsResponse(int code) {
		this.httpCode = code;

	}
	public String toString() {
		if (list == null) { return "EmptyConstants"; }
		return "AllConstants(" + list.size() + ")";
	}
}
