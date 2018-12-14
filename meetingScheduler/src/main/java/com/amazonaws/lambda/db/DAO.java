package com.amazonaws.lambda.db;

// Testing AGAIN
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.amazonaws.lambda.demo.EditTimeslotRequest;
import com.amazonaws.lambda.demo.SearchMeetingsRequest;
import com.amazonaws.lambda.model.Schedule;
import com.amazonaws.lambda.model.TimeSlot;

public class DAO {

	java.sql.Connection conn;

	public DAO() {
		try {
//    		System.out.println("\n\n\n");
			conn = DatabaseUtil.connect();
		} catch (Exception e) {
//    		e.printStackTrace();  
			conn = null;
		}
	}
	public static int setAvaliableToInt(String open) {
		
		switch (open.toLowerCase()) {
		case "open":
			return 0;
		case "close":
			return 1;
		default:
			return -1;
		}
	}

	public static int getDayOfWeekAsInt(String day) {
		if (day == null) {
			return -1;
		}
		switch (day.toLowerCase()) {
		case "monday":
			return 2;
		case "tuesday":
			return 3;
		case "wednesday":
			return 4;
		case "thursday":
			return 5;
		case "friday":
			return 6;
		case "saturday":
			return 7;
		case "sunday":
			return 1;
		default:
			return -1;
		}
	}

	public boolean addSchedule(Schedule schedule) throws Exception {
		try {
			PreparedStatement ps = conn.prepareStatement(
					"INSERT INTO Schedules (id, secretcode, startdate, enddate, daystarthour, dayendhour, organizer, creationdate, creationtime) values(?,?,?,?,?,?,?,NOW(),NOW());");

			ps.setString(1, schedule.id);
			ps.setString(2, schedule.getsecretcode());
			ps.setString(3, schedule.startdate);
			ps.setString(4, schedule.enddate);
			ps.setString(5, schedule.daystarthour);
			ps.setString(6, schedule.dayendhour);
			ps.setString(7, schedule.organizer);
//            ps.setString(8, "CURDATE()");
//            ps.setString(9, "CURTIME()");
			ps.execute();

			insertTimeSlots(schedule);

			return true;

		} catch (Exception e) {
//        	e.printStackTrace();
			throw new Exception("Failed to insert schedule: " + e.getMessage());
		}
	}

	public boolean insertTimeSlots(Schedule schedule) throws Exception {
		try {

			SimpleDateFormat dayofyear = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat hourofday = new SimpleDateFormat("HH:mm:ss");
			SimpleDateFormat fulltime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			java.util.Date startD = (java.util.Date) dayofyear.parse(schedule.startdate);
			java.util.Date endD = (java.util.Date) dayofyear.parse(schedule.enddate);

			java.util.Date startT = (java.util.Date) hourofday.parse(schedule.daystarthour);
			java.util.Date endT = (java.util.Date) hourofday.parse(schedule.dayendhour);

			java.util.Date starttimeofdayINIT = (java.util.Date) fulltime
					.parse(schedule.startdate + " " + schedule.daystarthour);
			Date starttimeofday = new Date(0);
			starttimeofday.setTime(starttimeofdayINIT.getTime());

			int numdays = (int) (1 + Math.abs((startD.getTime() - endD.getTime()) / (1000 * 60 * 60 * 24)));

			int numrows = (int) ((startT.getTime() - endT.getTime()) / (schedule.meetinglength * 60000));

			String weekdayString = " ";
			String MstartString = " ";
			String MendString = " ";

			Date MstartTDate;
			Date MendTDate;

			for (int j = 0; j < Math.abs(numdays); j++) {
				weekdayString = dayofyear.format(starttimeofday);
				for (int i = 0; i < Math.abs(numrows); i++) {

					// dates for the start and end time
					MstartTDate = new Date((starttimeofday.getTime()) + ((schedule.meetinglength * 60000) * i)); // date.getTime()+((slotlength*60000)*i);
					MendTDate = new Date((starttimeofday.getTime()) + ((schedule.meetinglength * 60000) * (i + 1)));
					// Strings for the start and end time
					MstartString = hourofday.format(MstartTDate);
					MendString = hourofday.format(MendTDate);
					java.sql.Time start = java.sql.Time.valueOf(MstartString); // Formatting for SQL
					java.sql.Time end = java.sql.Time.valueOf(MendString); // Formatting for SQL

					PreparedStatement ps = conn.prepareStatement(
							"INSERT INTO TimeSlots (id, secretcode, startdate, enddate, starttime, endtime, participant, available, scheduleid) values(?,?,?,?,?,?,?,?,?);");
					ps.setString(1, getSaltString());
					ps.setString(2, getSaltString());
					ps.setObject(3, MstartTDate);
					ps.setObject(4, MendTDate);
					ps.setTime(5, start);
					ps.setTime(6, end);
					ps.setString(7, "");
					ps.setInt(8, 0);
					ps.setString(9, schedule.id);
					ps.execute();

				}

				starttimeofday.setTime(starttimeofday.getTime() + (1000 * 60 * 60 * 24));

			}

			return true;

		} catch (Exception e) {
			// e.printStackTrace();
			throw new Exception("Failed to insert timeslots: " + e.getMessage());
		}
	}

	public ArrayList<TimeSlot> getTimeSlots(String scheduleid) throws Exception {
		try {
			ArrayList<TimeSlot> timeslots = new ArrayList<TimeSlot>();
			PreparedStatement ps = conn
					.prepareStatement("SELECT * FROM TimeSlots WHERE scheduleid = ? ORDER BY startdate, starttime;");
			ps.setString(1, scheduleid);
			ResultSet resultSet = ps.executeQuery();

			while (resultSet.next()) {
				timeslots.add(generateTimeSlot(resultSet));
			}
			resultSet.close();
			ps.close();

			return timeslots;

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Failed in getting timeslots: " + e.getMessage());
		}

	}

	// Get timeslot for Organizer to access timeslot with secretcode
	public ArrayList<TimeSlot> getTimeSlotsForOrg(String secretcode) throws Exception {
		try {
			ArrayList<TimeSlot> timeslots = new ArrayList<TimeSlot>();
			PreparedStatement ps = conn.prepareStatement(
					"SELECT TimeSlots.id, TimeSlots.secretcode, TimeSlots.startDate, TimeSlots.enddate, TimeSlots.starttime, TimeSlots.endtime, TimeSlots.participant, TimeSlots.available, TimeSlots.scheduleid FROM TimeSlots JOIN Schedules ON TimeSlots.scheduleid = Schedules.id  WHERE Schedules.secretcode = ? ORDER BY TimeSlots.startdate, TimeSlots.starttime;");
			ps.setString(1, secretcode);
			ResultSet resultSet = ps.executeQuery();

			while (resultSet.next()) {
				timeslots.add(generateTimeSlot(resultSet));
			}
			resultSet.close();
			ps.close();

			return timeslots;

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Failed in getting timeslots: " + e.getMessage());
		}

	}

	private TimeSlot generateTimeSlot(ResultSet resultSet) throws Exception {
		String id = resultSet.getString("id");
		String secretcode = resultSet.getString("secretcode");
		String startdate = resultSet.getString("startdate");
		String enddate = resultSet.getString("enddate");
		String starttime = resultSet.getString("starttime");
		String endtime = resultSet.getString("endtime");
		String participant = resultSet.getString("participant");
		int available = resultSet.getInt("available");
		String scheduleid = resultSet.getString("scheduleid");
		return new TimeSlot(id, secretcode, startdate, enddate, starttime, endtime, participant, scheduleid, available);
	}

	private Schedule generateSchedule(ResultSet resultSet) throws Exception {
		String id = resultSet.getString("id");
		String secretcode = resultSet.getString("secretcode");
		String startdate = resultSet.getString("startdate");
		String enddate = resultSet.getString("enddate");
		String daystarthour = resultSet.getString("daystarthour");
		String dayendhour = resultSet.getString("dayendhour");
		String organizer = resultSet.getString("organizer");
		java.sql.Date creationDate = resultSet.getDate("creationdate");
		java.sql.Time creationTime = resultSet.getTime("creationtime");

		return new Schedule(id, secretcode, startdate, enddate, daystarthour, dayendhour, organizer, creationDate,
				creationTime);
	}

	String getSaltString() {
		String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		while (salt.length() < 18) { // length of the random string.
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}
		String saltStr = salt.toString();
		return saltStr;

	}

	public boolean updateTimeSlot(String starttime, String startdate, String sID, int open) throws Exception {
		try {
			boolean r = false;
			PreparedStatement ps = conn.prepareStatement(
					"UPDATE TimeSlots SET participant = ?, available = ? WHERE startdate = ? AND starttime = ? AND scheduleid = ?;");
			ps.setString(1, "");
			ps.setInt(2, open);
			ps.setString(3, startdate);
			ps.setString(4, starttime);
			ps.setString(5, sID);
			int numRows = ps.executeUpdate();
			if (numRows > 0) {
				r = true;
			}
			return r;
		} catch (Exception e) {
			throw new Exception("Failed in updating time slot: " + e.getMessage());
		}
	}

	public TimeSlot findTimeSlot(String starttime, String startdate, String sID) throws Exception {
		try {
			PreparedStatement ps = conn.prepareStatement(
					"SELECT * FROM TimeSlots WHERE startdate = ? AND starttime = ? AND scheduleid = ?;");
			ps.setString(1, startdate);
			ps.setString(2, starttime);
			ps.setString(3, sID);
			ResultSet resultSet = ps.executeQuery();
			resultSet.next();
			TimeSlot b = generateTimeSlot(resultSet);
			resultSet.close();
			return b;

		} catch (Exception e) {
			throw new Exception("Failed in finding time slot: " + e.getMessage());
		}
	}

	public boolean createMeeting(TimeSlot meeting) throws Exception {
		try {
			PreparedStatement ps = conn.prepareStatement(
					"UPDATE TimeSlots SET participant = ?, available = ?, secretcode = ? WHERE startdate = ? AND starttime = ? AND available = ? AND scheduleid = ?;");
			ps.setString(1, meeting.participant);
			ps.setInt(2, meeting.available);
			ps.setString(3, meeting.getSecretcode());
			ps.setString(4, meeting.startdate);
			ps.setString(5, meeting.starttime);
			ps.setInt(6, 0);
			ps.setString(7, meeting.scheduleid);
			int result = ps.executeUpdate();
			System.out.println(result);

			if (result == 0) {
				return false;
			} else {
				return true;
			}
		}

		catch (Exception e) {
			throw new Exception("Failed in updating participant: " + e.getMessage());
		}
	}

	public boolean deleteMeeting(String sID, String secretcode, String starttime, String startdate) throws Exception {
		try {
			String sc = getSaltString();
			boolean r = false;
			PreparedStatement ps = conn.prepareStatement(
					"UPDATE TimeSlots SET secretcode = ?, participant = ?, available = ? WHERE secretcode = ? AND starttime = ? AND startdate = ? AND NOT participant = ? AND available = ? AND scheduleid = ?");
			ps.setString(1, sc);
			ps.setString(2, "");
			ps.setInt(3, 0);
			ps.setString(4, secretcode);
			ps.setString(5, starttime);
			ps.setString(6, startdate);
			ps.setString(7, "");
			ps.setInt(8, 1);
			ps.setString(9, sID);
			int numRows = ps.executeUpdate();
			if (numRows > 0) {
				r = true;
			}
			return r;
		} catch (Exception e) {
			throw new Exception("Failed in deleting time slot: " + e.getMessage());

		}
	}

	public boolean deleteSchedule(String secretCode) throws Exception {
		try {

			boolean r = false;
			PreparedStatement ps = conn.prepareStatement("DELETE FROM Schedules Where secretcode = ?;");
			ps.setString(1, secretCode);

			int numRows = ps.executeUpdate();
			if (numRows > 0) {
				r = true;
			}
			return r;
		} catch (Exception e) {
			throw new Exception("Failed in deleting schedule: " + e.getMessage());

		}
	}
	public class WrongPassException extends Exception{
		private String mess;
		public WrongPassException() {this.mess = "";}
		public void setMessage(String m) {this.mess = m;}
		@Override public String getMessage() {return this.mess;}
	}
	public boolean deleteOldSchedules(int days, String password) throws Exception {
		try {
			String pass = "yeetcarlswagon";
			boolean r = false;
			LocalDate date = LocalDate.now();
			System.out.println("The date is" + date.toString());
			date = date.minusDays(days);
			java.sql.Date formattedDateTime = java.sql.Date.valueOf(date);

			//System.out.println("Entering DAO");
			//System.out.println(date);
			//System.out.println(formattedDateTime);
			if(password.equals(pass)) {
				PreparedStatement ps = conn.prepareStatement("DELETE FROM Schedules WHERE creationdate <= ?;");
				ps.setDate(1, formattedDateTime);
				int numRows = ps.executeUpdate();
				if (numRows > 0) {
					r = true;
				}
			}
			else {
				String skrrrt = "That's the wrong password Jimbo";
				WrongPassException whoopityscoop = new WrongPassException();
				whoopityscoop.setMessage(skrrrt);
				throw whoopityscoop;
			}
			return r;
		}
		catch(WrongPassException w) {
			throw new Exception("Failed in deleting old schedules: " + w.getMessage());
		}
		catch (Exception e) {
			throw new Exception("Failed in deleting old schedules: " + e.getMessage());
		}
	}

	public ArrayList<Schedule> getSchedules(int hours, String password) throws Exception {
		try {
			String pass = "yeetcarlswagon";
			ArrayList<Schedule> ScheduleSYS = new ArrayList<Schedule>();
			LocalTime time = LocalTime.now();
			time = time.minusHours(hours);
			java.sql.Time formattedTime = java.sql.Time.valueOf(time);

			//System.out.println("Entering DAO");
			//System.out.println(hours);
			//System.out.println(formattedTime);
			if(password.equals(pass)){
			PreparedStatement ps = conn.prepareStatement(
					"SELECT * FROM  Schedules WHERE creationtime >= ? ORDER BY startdate, daystarthour;");
			ps.setTime(1, formattedTime);
			ResultSet resultSet = ps.executeQuery();
			while (resultSet.next()) {
				ScheduleSYS.add(generateSchedule(resultSet));
			}
			resultSet.close();
			ps.close();
			}
			else {
				String msg = "That's not the right password Jimbo";
				WrongPassException beans = new WrongPassException();
				beans.setMessage(msg);
				throw beans;
			}
			return ScheduleSYS;
		}
		catch(WrongPassException w) {
			throw new Exception("Failed in getting timeslots" + w.getMessage());
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Failed in getting timeslots: " + e.getMessage());
		}
	}


	public List<TimeSlot> FilterAll(SearchMeetingsRequest req, String id) throws Exception {

	
	//shoutout to Yared for that gross SQL statement
	//haha gets the job done
	//public List<TimeSlot> FilterAll(SearchMeetingsRequest req) throws Exception {
		int Year = req.getYear();
		int month = req.getMonth();
		int DayOfMonth = req.getDayOfMonth();
		int DayWeek;
		if (req.getDayOfWeek() == null) {
			DayWeek = 0;
		} else
			DayWeek = getDayOfWeekAsInt(req.getDayOfWeek());

		try {
			java.sql.Time start;
			java.sql.Time end;

			List<TimeSlot> ScheduleSYS = new ArrayList<TimeSlot>();
			if (req.getStart() != null) {
				start = java.sql.Time.valueOf(req.getStart());
			} else
				start = java.sql.Time.valueOf("00:00:00"); // Formatting for SQL and checking if null

			if (req.getEnd() != null) {
				end = java.sql.Time.valueOf(req.getEnd());
			} else
				end = java.sql.Time.valueOf("23:00:00");
			
			PreparedStatement ps = conn.prepareStatement(
					"SELECT * FROM TimeSlots WHERE available = 0 AND(YEAR(startdate) = ? OR  0 =?) AND (month(startdate) = ? OR 0 = ?) AND	(Day(startdate) = ? OR 0 =?) AND ((starttime >= ? AND endtime <= ?) OR ('0' = ?) OR ('0' = ?))  AND	(dayofweek(startdate) = ? OR 0 = ?) AND scheduleid = ?");
			ps.setInt(1, Year);
			ps.setInt(2, Year);
			ps.setInt(3, month);
			ps.setInt(4, month);
			ps.setInt(5, DayOfMonth);
			ps.setInt(6, DayOfMonth);
			ps.setTime(7, start);
			ps.setTime(8, end);
			ps.setTime(9, start);
			ps.setTime(10, end);
			ps.setInt(11, DayWeek);
			ps.setInt(12, DayWeek);
			ps.setString(13, id);

			ResultSet resultSet = ps.executeQuery();

			while (resultSet.next()) {
				ScheduleSYS.add(generateTimeSlot(resultSet));
			}
			resultSet.close();
			ps.close();

			return ScheduleSYS;

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Failed in getting timeslots: " + e.getMessage());
		}
	}
	public boolean Edit(String code, EditTimeslotRequest req) throws Exception {
		try {
			java.sql.Date day;
			if (req.getDay() != null) {
				day = java.sql.Date.valueOf(req.getDay());
			} else
				day = java.sql.Date.valueOf("0000-00-00");
					
			
			int open = setAvaliableToInt(req.getOpen());
			
			boolean r = false;
			PreparedStatement ps = conn.prepareStatement(
					"UPDATE TimeSlots JOIN Schedules ON TimeSlots.scheduleid = Schedules.id SET TimeSlots.available = ? WHERE TimeSlots.startdate = ? and Schedules.secretcode = ?;");
			ps.setInt(1, open);
		
			ps.setDate(2, day);
			ps.setString(3, code);
			
			int numRows = ps.executeUpdate();
			if (numRows > 0) {
				r = true;
			}
			return r;
		} catch (Exception e) {
			throw new Exception("Failed in updating time slot: " + e.getMessage());
		}
	}

	public int getMeetingLength(String scheduleID) throws Exception{
    	try {
    		PreparedStatement ps = conn.prepareStatement("SELECT * FROM TimeSlots WHERE scheduleid = ?");
    		ps.setString(1, scheduleID);
    		ResultSet rS = ps.executeQuery();
    		rS.next();
    		String st = rS.getString("starttime");
    		String et = rS.getString("endtime");
    		rS.close();
    		int stH,stM;
    		int etH,etM;
    		stH = Integer.parseInt(st.substring(0, 2));
    		etH = Integer.parseInt(et.substring(0, 2));
    		stM = Integer.parseInt(st.substring(3, 5));
    		etM = Integer.parseInt(et.substring(3, 5));
    		stM = stM + (stH*60);
    		etM = etM + (etH*60);
    		int returnVal = (etM - stM);
    		return returnVal;
    	}
    	catch(Exception e) {
    		throw new Exception("Failed to get meeting length: " + e.getMessage());
    	}
    }
	public class DateFormatException extends Exception{
		private String mess;
		public DateFormatException() {this.mess = "";}
		public void setMessage(String m) {this.mess = m;}
		@Override public String getMessage() {return this.mess;}
	}
	public boolean extendDateBackwards(String scheduleid, String secretcode, String newStartDate) throws Exception{
	    try {
	    	boolean r = false;
	    	SimpleDateFormat dayofyear = new SimpleDateFormat("yyyy-MM-dd");
	        SimpleDateFormat hourofday = new SimpleDateFormat("HH:mm:ss");
	        SimpleDateFormat fulltime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    		
	    	int meetingLength = getMeetingLength(scheduleid);
	   		PreparedStatement ps = conn.prepareStatement("SELECT * FROM Schedules WHERE id = ? AND secretcode = ?;");
	   		ps.setString(1, scheduleid);
	   		ps.setString(2, secretcode);
    		ResultSet rS = ps.executeQuery();
    		if(rS.next()) {r = true;} //checking to see if there is a schedule
	    	String startdate = rS.getString("startdate");
	   		String dayStartHour = rS.getString("daystarthour");
	   		String dayEndHour = rS.getString("dayendhour");
	    		
    		java.util.Date sD = (java.util.Date) dayofyear.parse(startdate);
	        java.util.Date nSD = (java.util.Date) dayofyear.parse(newStartDate);
            java.util.Date startT = (java.util.Date) hourofday.parse(dayStartHour);
	        java.util.Date endT = (java.util.Date) hourofday.parse(dayEndHour);
            boolean isBefore = false;
            if(nSD.before(sD)) {isBefore = true;} //checking to see if the dates are in order
            sD.setTime(sD.getTime() - (1000*60*60*24));
            if(!isBefore) {
	           	String errorMessage = "The Dates for the extension are out of order";
	           	DateFormatException d = new DateFormatException();
	           	d.setMessage(errorMessage);
	           	throw d;
	        }
	        if(isBefore) {
	            java.util.Date starttimeofdayINIT = (java.util.Date) fulltime.parse(newStartDate+" "+dayStartHour);
	            starttimeofdayINIT.setTime(starttimeofdayINIT.getTime());
	            Date starttimeofday = new Date(0);
	            starttimeofday.setTime(starttimeofdayINIT.getTime());
	            int numdays = (int) (1+Math.abs((sD.getTime()-nSD.getTime())/(1000*60*60*24)));
	            int numrows = (int) ((startT.getTime()-endT.getTime())/(meetingLength*60000));

            	String weekdayString = " ";
            	String MstartString = " ";
            	String MendString = " ";
            	
            	Date MstartTDate;
            	Date MendTDate;
            	for (int j=0;j<Math.abs(numdays);j++) {
            		weekdayString = dayofyear.format(starttimeofday);
            		for (int i=0;i<Math.abs(numrows);i++) {
            		// dates for the start and end time
            			MstartTDate = new Date((starttimeofday.getTime())+((meetingLength*60000)*i)); 
            			MendTDate = new Date((starttimeofday.getTime())+((meetingLength*60000)*(i+1)));
                    // 	Strings for the start and end time
            			MstartString = hourofday.format(MstartTDate);
            			MendString = hourofday.format(MendTDate);
            			
            			PreparedStatement ps2 = conn.prepareStatement("INSERT INTO TimeSlots (id, secretcode, startdate, enddate, starttime, endtime, participant, available, scheduleid) values(?,?,?,?,?,?,?,?,?);");
            			ps2.setString(1, getSaltString());
            			ps2.setString(2, getSaltString());
            			ps2.setString(3, weekdayString);
            			ps2.setString(4, weekdayString);
            			ps2.setString(5, MstartString);
            			ps2.setString(6, MendString);
            			ps2.setString(7, "");
            			ps2.setInt(8, 0);
            			ps2.setString(9, scheduleid);
            			ps2.execute();
                  
            		}
            		starttimeofday.setTime(starttimeofday.getTime() + (1000*60*60*24));
            	}
            	PreparedStatement ps3 = conn.prepareStatement("UPDATE Schedules SET startdate = ? WHERE id = ?;");
            	ps3.setString(1, newStartDate);
            	ps3.setString(2, scheduleid);
            	ps3.executeUpdate();
            	}
            return r && isBefore;
    	}
	    catch(DateFormatException d) {
	    	throw new Exception(d.getMessage());
	    }
	    catch(Exception e) {
	    	e.printStackTrace();
	    	throw new Exception("Failed to extend date backwards: " + e.getMessage());
	    }
	}
	public boolean extendDateForwards(String scheduleID, String newEndDate, String sc) throws Exception{
    	try {
    		boolean r = false;
    		SimpleDateFormat dayofyear = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat hourofday = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat fulltime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    		
    		int meetingLength = getMeetingLength(scheduleID);
    		PreparedStatement ps = conn.prepareStatement("SELECT * FROM Schedules WHERE id = ? AND secretcode = ?;");
    		ps.setString(1, scheduleID);
    		ps.setString(2, sc);
    		ResultSet rS = ps.executeQuery();
    		if(rS.next()) {r = true;} //checking to see if there is a schedule
    		String enddate = rS.getString("enddate");
    		String dayStartHour = rS.getString("daystarthour");
    		String dayEndHour = rS.getString("dayendhour");
    		
    		java.util.Date eD = (java.util.Date) dayofyear.parse(enddate);
            java.util.Date nED = (java.util.Date) dayofyear.parse(newEndDate);
            java.util.Date startT = (java.util.Date) hourofday.parse(dayStartHour);
            java.util.Date endT = (java.util.Date) hourofday.parse(dayEndHour);
            boolean isAfter = false;
            if(nED.after(eD)) {isAfter = true;} //checking to see if the dates are in order
            if(!isAfter) {
            	String errorMessage = "The Dates for the extension are out of order";
            	DateFormatException d = new DateFormatException();
            	d.setMessage(errorMessage);
            	throw d;
            }
            eD.setTime(eD.getTime() + (1000*60*60*24)); //setting the date forwards
            if(isAfter) {
            	java.util.Date starttimeofdayINIT = (java.util.Date) fulltime.parse(enddate+" "+dayStartHour);
            	starttimeofdayINIT.setTime(starttimeofdayINIT.getTime() + (1000*60*60*24));
            	Date starttimeofday = new Date(0);
            	starttimeofday.setTime(starttimeofdayINIT.getTime());
            	int numdays = (int) (1+Math.abs((eD.getTime()-nED.getTime())/(1000*60*60*24)));
            	int numrows = (int) ((startT.getTime()-endT.getTime())/(meetingLength*60000));

            	String weekdayString = " ";
            	String MstartString = " ";
            	String MendString = " ";
            	
            	Date MstartTDate;
            	Date MendTDate;
            	for (int j=0;j<Math.abs(numdays);j++) {
            		weekdayString = dayofyear.format(starttimeofday);
            		for (int i=0;i<Math.abs(numrows);i++) {
            		// dates for the start and end time
            			MstartTDate = new Date((starttimeofday.getTime())+((meetingLength*60000)*i)); 
            			MendTDate = new Date((starttimeofday.getTime())+((meetingLength*60000)*(i+1)));
                    // 	Strings for the start and end time
            			MstartString = hourofday.format(MstartTDate);
            			MendString = hourofday.format(MendTDate);
            			
            			PreparedStatement ps2 = conn.prepareStatement("INSERT INTO TimeSlots (id, secretcode, startdate, enddate, starttime, endtime, participant, available, scheduleid) values(?,?,?,?,?,?,?,?,?);");
            			ps2.setString(1, getSaltString());
            			ps2.setString(2, getSaltString());
            			ps2.setString(3, weekdayString);
            			ps2.setString(4, weekdayString);
            			ps2.setString(5, MstartString);
            			ps2.setString(6, MendString);
            			ps2.setString(7, "");
            			ps2.setInt(8, 0);
            			ps2.setString(9, scheduleID);
            			ps2.execute();
                  
            		}
            		starttimeofday.setTime(starttimeofday.getTime() + (1000*60*60*24));
            	}
            	PreparedStatement ps3 = conn.prepareStatement("UPDATE Schedules SET enddate = ? WHERE id = ?;");
            	ps3.setString(1, newEndDate);
            	ps3.setString(2, scheduleID);
            	ps3.executeUpdate();
            	}
            return r && isAfter;
    	}
    	catch(DateFormatException d) {
    		throw new Exception(d.getMessage());
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    		throw new Exception("Failed to extend date forwards: " + e.getMessage());
    	}
    }

	public boolean closeTime(String scheduleid, String secretcode, String time) throws Exception{
		try {
			boolean r = false;
			boolean j = false;
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM Schedules WHERE id = ? AND secretcode = ?;");
			ps.setString(1, scheduleid);
			ps.setString(2, secretcode);
			ResultSet rS = ps.executeQuery();
			if(rS.next()) {j = true;}
			PreparedStatement ps2 = conn.prepareStatement("UPDATE TimeSlots SET participant = ?, available = ? WHERE starttime = ? AND scheduleid = ?;");
			ps2.setString(1, "");
			ps2.setInt(2, 1);
			ps2.setString(3, time);
			ps2.setString(4, scheduleid);
			int numRows = ps2.executeUpdate();
			if (numRows > 0) {
				r = true;
			}
			return r && j;
			}
		catch(Exception e) {throw new Exception("Failed in closing for a time: " + e.getMessage());}
	}
	public boolean openTime(String scheduleid, String secretcode, String time) throws Exception{
		try {
			boolean r = false;
			boolean j = false;
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM Schedules WHERE id = ? AND secretcode = ?;");
			ps.setString(1, scheduleid);
			ps.setString(2, secretcode);
			ResultSet rS = ps.executeQuery();
			if(rS.next()) {j = true;}
			PreparedStatement ps2 = conn.prepareStatement("UPDATE TimeSlots SET participant = ?, available = ? WHERE starttime = ? AND scheduleid = ?;");
			ps2.setString(1, "");
			ps2.setInt(2, 0);
			ps2.setString(3, time);
			ps2.setString(4, scheduleid);
			int numRows = ps2.executeUpdate();
			if (numRows > 0) {
				r = true;
			}
			return r && j;
			}
		catch(Exception e) {throw new Exception("Failed in opening for a time: " + e.getMessage());}
	}
	
}