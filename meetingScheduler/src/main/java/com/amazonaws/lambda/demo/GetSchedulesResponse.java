package com.amazonaws.lambda.demo;

import java.util.ArrayList;
import java.util.List;
import com.amazonaws.lambda.model.Schedule;
import com.amazonaws.lambda.model.TimeSlot;

public class GetSchedulesResponse {
	List<TimeSlot> list;
	int httpCode;
	
	public GetSchedulesResponse (List<TimeSlot> list, int code) {
		this.list = list;
		this.httpCode = code;
	}
	
	public GetSchedulesResponse (int code) {
		this.list = new ArrayList<TimeSlot>();
		this.httpCode = code;
	}
	
	public String toString() {
		if (list == null) { return "EmptyConstants"; }
		return "AllConstants(" + list.size() + ")";
	}
}
