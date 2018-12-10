package com.amazonaws.lambda.db;
// Testing AGAIN
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.amazonaws.lambda.model.Schedule;
import com.amazonaws.lambda.model.TimeSlot;

public class DAO {

	java.sql.Connection conn; 

    public DAO() {
    	try  {
//    		System.out.println("\n\n\n");
    		conn = DatabaseUtil.connect();
    	} catch (Exception e) {
//    		e.printStackTrace();  
    		conn = null;
    	}
    }
    
    
    public boolean addSchedule(Schedule schedule) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO Schedules (id, secretcode, startdate, enddate, daystarthour, dayendhour, organizer) values(?,?,?,?,?,?,?);");
            
            ps.setString(1, schedule.id);
            ps.setString(2, schedule.getsecretcode());
            ps.setString(3, schedule.startdate);
            ps.setString(4, schedule.enddate);
            ps.setString(5, schedule.daystarthour);
            ps.setString(6, schedule.dayendhour);
            ps.setString(7, schedule.organizer);
            ps.execute();

            insertTimeSlots(schedule);
            
            return true;

        } catch (Exception e) {
//        	e.printStackTrace();
            throw new Exception("Failed to insert schedule: " + e.getMessage());
        }
    }
    
    public boolean insertTimeSlots(Schedule schedule) throws Exception {
    	try{
    		
    		SimpleDateFormat dayofyear = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat hourofday = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat fulltime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            
            java.util.Date startD = (java.util.Date) dayofyear.parse(schedule.startdate);
            java.util.Date endD = (java.util.Date) dayofyear.parse(schedule.enddate);
            
            java.util.Date startT = (java.util.Date) hourofday.parse(schedule.daystarthour);
            java.util.Date endT = (java.util.Date) hourofday.parse(schedule.dayendhour);
            
            java.util.Date starttimeofdayINIT = (java.util.Date) fulltime.parse(schedule.startdate+" "+schedule.daystarthour);
            Date starttimeofday = new Date(0);
            starttimeofday.setTime(starttimeofdayINIT.getTime());
            
            int numdays = (int) (1+Math.abs((startD.getTime()-endD.getTime())/(1000*60*60*24)));

            int numrows = (int) ((startT.getTime()-endT.getTime())/(schedule.meetinglength*60000));
            
            String weekdayString = " ";
            String MstartString = " ";
            String MendString = " ";
            
            Date MstartTDate;
            Date MendTDate;
            
            for (int j=0;j<Math.abs(numdays);j++) {
            	weekdayString = dayofyear.format(starttimeofday);
            	for (int i=0;i<Math.abs(numrows);i++) {
            		
            		// dates for the start and end time
            		MstartTDate = new Date((starttimeofday.getTime())+((schedule.meetinglength*60000)*i)); // date.getTime()+((slotlength*60000)*i);
            		MendTDate = new Date((starttimeofday.getTime())+((schedule.meetinglength*60000)*(i+1)));
                    // Strings for the start and end time
                    MstartString = hourofday.format(MstartTDate);
                    MendString = hourofday.format(MendTDate);
                    
            		PreparedStatement ps = conn.prepareStatement("INSERT INTO TimeSlots (id, secretcode, startdate, enddate, starttime, endtime, participant, available, scheduleid) values(?,?,?,?,?,?,?,?,?);");
                    ps.setString(1, getSaltString());
                    ps.setString(2, getSaltString());
                    ps.setString(3, weekdayString);
                    ps.setString(4, weekdayString);
                    ps.setString(5, MstartString);
                    ps.setString(6, MendString);
                    ps.setString(7, "");
                    ps.setInt(8, 0);
                    ps.setString(9, schedule.id);
                    ps.execute();
                  
                }
            	
            	starttimeofday.setTime(starttimeofday.getTime() + (1000*60*60*24));
            	
            }
            
            return true;
            
    	} catch (Exception e) {
    		//e.printStackTrace();
            throw new Exception("Failed to insert timeslots: " + e.getMessage());
    	}
    }

	public ArrayList<TimeSlot> getTimeSlots(String scheduleid) throws Exception {
    	try {
            ArrayList<TimeSlot> timeslots = new ArrayList<TimeSlot>();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM TimeSlots WHERE scheduleid = ? ORDER BY startdate, starttime;");
            ps.setString(1,  scheduleid);
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
            PreparedStatement ps = conn.prepareStatement("SELECT TimeSlots.id, TimeSlots.secretcode, TimeSlots.startDate, TimeSlots.enddate, TimeSlots.starttime, TimeSlots.endtime, TimeSlots.participant, TimeSlots.available, TimeSlots.scheduleid FROM TimeSlots JOIN Schedule ON TimeSlots.scheduleid = schedule.scheduleid  WHERE schedule.secretcode = ? ORDER BY TimeSlots.startdate, TimeSlots.starttime;");
            ps.setString(1,  secretcode);
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
    	String participant  = resultSet.getString("participant");
    	int available = resultSet.getInt("available");
    	String scheduleid = resultSet.getString("scheduleid");
        return new TimeSlot (secretcode, startdate, enddate, starttime, endtime, participant, scheduleid, available);
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
    
    public boolean updateTimeSlot(String starttime, String startdate, String sID, int open) throws Exception{
    	try {
    		boolean r = false;
    		PreparedStatement ps = conn.prepareStatement("UPDATE TimeSlots SET available = ? WHERE startdate = ? AND starttime = ? and scheduleid = ?;");
    		ps.setInt(1, open);
    		ps.setString(2, startdate);
    		ps.setString(3, starttime);
    		ps.setString(4, sID);
    		int numRows = ps.executeUpdate();
    		if(numRows > 0) {
    			r = true;
    		}
    		return r;
    	}
    	catch(Exception e) {
    		throw new Exception("Failed in updating time slot: " + e.getMessage());
    	}    	
    }
    
    public TimeSlot findTimeSlot(String starttime, String startdate, String sID) throws Exception{
    	try {
    		PreparedStatement ps = conn.prepareStatement("SELECT * FROM TimeSlots WHERE startdate = ? AND starttime = ? AND scheduleid = ?;");
    		ps.setString(1, startdate);
    		ps.setString(2, starttime);
    		ps.setString(3, sID);
    		ResultSet resultSet = ps.executeQuery();
    		resultSet.next();
    		TimeSlot b = generateTimeSlot(resultSet);
    		resultSet.close();
    		return b;
    	
    	}
    	catch(Exception e) {
    		throw new Exception("Failed in finding time slot: " + e.getMessage());
    	}
    }
    
    public boolean createMeeting(TimeSlot meeting) throws Exception{
    	try {
    		boolean r = false;
    		PreparedStatement ps = conn.prepareStatement("UPDATE TimeSlots SET participant = ?, available = ?, secretcode = ? WHERE startdate = ? AND starttime = ? AND scheduleid = ?;");
    		ps.setString(1, meeting.participant);
    		ps.setInt(2, meeting.available);
    		ps.setString(3, meeting.getSecretcode());
    		ps.setString(4, meeting.startdate);
    		ps.setString(5, meeting.starttime);
    		ps.setString(6, meeting.scheduleid);
    		int result = ps.executeUpdate();
    		System.out.println(result);
    		
    		if(result == 0) {
    			return false;
    		} else {
    			return true;
    		}
    	}
    		
    	catch(Exception e) {
    		throw new Exception("Failed in updating participant: " + e.getMessage());
    	}    	
    }
    public boolean deleteMeeting(String sID, String secretcode) throws Exception{
    	try {
    		boolean r = false;
    		PreparedStatement ps = conn.prepareStatement("UPDATE TimeSlots SET participant = ? AND availability = ? WHERE secretcode = ? AND scheduleid = ?");
    		ps.setString(1, "");
    		ps.setInt(2, 0);
    		ps.setString(3, secretcode);
    		ps.setString(4, sID);
    		int numRows = ps.executeUpdate();
    		if(numRows > 0) {
    			r = true;
    		}
    		return r;
    	}
    	catch(Exception e) {
    		throw new Exception("Failed in deleting time slot: " + e.getMessage());

    	}
    }

}