package com.amazonaws.lambda.demo;

import java.sql.Time;
import java.time.*;

public class SearchMeetingsRequest {
	int Month;
	int Year;
	String DayOfWeek;
	int DayOfMonth;
	String Start;
	String End;
	 String scheduleid;  

	public SearchMeetingsRequest(int Month, int Year, int Date, String Day, String Start, String End, String id) {
		
		this.Month = Month;
		this.Year = Year;
		this.DayOfWeek = Day;
		this.DayOfMonth = Date;
		this.Start = Start;
		this.End = End;
		this.scheduleid = id;

	}

	public String toString() {
		return "Add(" + Integer.toString(Month) + Integer.toString(Year) + DayOfWeek + Integer.toString(DayOfMonth)
				+ Start + End + scheduleid + ")";
	}

	public int getMonth() {
		return Month;
	};

	public int getYear() {
		return Year;
	};

	public int getDayOfMonth() {
		return DayOfMonth;
	}

	public String getStart() {
		return Start;
	}

	public String getEnd() {
		return End;
	}

	public String getDayOfWeek() {
		return DayOfWeek;
	}
}
