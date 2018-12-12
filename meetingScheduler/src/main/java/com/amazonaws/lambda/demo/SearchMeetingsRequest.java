package com.amazonaws.lambda.demo;

public class SearchMeetingsRequest {
	int Month;
	int Year;
	String DayOfWeek;
	int DayOfMonth; 
	String Timeslot;
	
	public SearchMeetingsRequest (int Month, int Year, String Day, int Date, String Time) {
		this.Month = Month;
		this.Year=Year;
		this.DayOfWeek = Day;
		this.DayOfMonth = Date;
		this.Timeslot = Time;
	}
	public String toString() {
		return "Add(" + Integer.toString(Month) +  Integer.toString(Year) + DayOfWeek + Integer.toString(DayOfMonth) + Timeslot +")";
	}
	
}
