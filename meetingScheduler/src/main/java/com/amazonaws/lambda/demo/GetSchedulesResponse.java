package com.amazonaws.lambda.demo;

import java.util.ArrayList;
import java.util.List;
import com.amazonaws.lambda.model.Schedule;


public class GetSchedulesResponse {
	List<Schedule> list;
	int httpCode;
	
	public GetSchedulesResponse (List<Schedule> list, int code) {
		this.list = list;
		this.httpCode = code;
	}
	
	public GetSchedulesResponse (int code) {
		this.list = new ArrayList<Schedule>();
		this.httpCode = code;
	}
	
	public String toString() {
		if (list == null) { return "EmptyConstants"; }
		return "AllConstants(" + list.size() + ")";
	}
}
