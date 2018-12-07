package com.amazonaws.lambda.db;
// Testing 
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
            SimpleDateFormat fulltime = new SimpleDateFormat("yyyy-MM-ddTHH:mm:ss");
            
            java.util.Date startD = (java.util.Date) dayofyear.parse(schedule.startdate);
            java.util.Date endD = (java.util.Date) dayofyear.parse(schedule.enddate);
            
            java.util.Date startT = (java.util.Date) hourofday.parse(schedule.daystarthour);
            java.util.Date endT = (java.util.Date) hourofday.parse(schedule.dayendhour);
            
            java.util.Date starttimeofdayINIT = (java.util.Date) fulltime.parse(schedule.startdate+"T"+schedule.daystarthour);
            Date starttimeofday = new Date(0);
            starttimeofday.setTime(starttimeofdayINIT.getTime());
            System.out.println("util: "+ starttimeofdayINIT.getTime()+"\n\n");
            System.out.println("sql: "+ starttimeofday.getTime()+"\n\n");
            
            int numdays = (int) ((startD.getTime()-endD.getTime())/(1000*60*60*24));
            int numrows = (int) ((startT.getTime()-endT.getTime())/(schedule.meetinglength*60000));
            
            String weekdayString = "";
            String MstartString = "";
            String MendString = "";
            
            Date MstartTDate;
            Date MendTDate;
            
            for (int j=0;j<Math.abs(numdays);j++) {
            	
            	for (int i=0;i<Math.abs(numrows);i++) {
            		
            		// dates for the start and end time
            		MstartTDate = new Date((starttimeofday.getTime())+((schedule.meetinglength*60000)*i)); // date.getTime()+((slotlength*60000)*i);
            		MendTDate = new Date((starttimeofday.getTime())+((schedule.meetinglength*60000)*(i+1)));
                  
                    // Strings for the start and end time
                    MstartString = hourofday.format(MstartTDate);
                    MendString = hourofday.format(MendTDate);
                    
            		PreparedStatement ps = conn.prepareStatement("INSERT INTO TimeSlots (id, secretcode, startdate, enddate, starttime, endtime, participant, scheduleid) values(?,?,?,?,?,?,?,?);");
                   
                    ps.setString(1, getSaltString());
                    ps.setString(2, getSaltString());
                    ps.setString(3, weekdayString);
                    ps.setString(4, weekdayString);
                    ps.setString(5, MstartString);
                    ps.setString(6, MendString);
                    ps.setString(7, "");
                    ps.setString(8, schedule.id);
                    ps.execute();
                  
                }
            	
            	weekdayString = dayofyear.format(starttimeofday);
            	System.out.println(weekdayString+"\n\n");
            	
            	// increment one day ahead
            	starttimeofday.setTime(starttimeofday.getTime() + (1000*60*60*24));
            	
            }
            
            return true;
            
    	} catch (Exception e) {
    		//e.printStackTrace();
            throw new Exception("Failed to insert schedule: " + e.getMessage());
    	}
    }

	public ArrayList<TimeSlot> getTimeSlots(String scheduleid) throws Exception {
    	try {
            ArrayList<TimeSlot> timeslots = new ArrayList<TimeSlot>();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM TimeSlots WHERE scheduleid = ?;");
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
            throw new Exception("Failed in getting schedule: " + e.getMessage());
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
    	String scheduleid = resultSet.getString("scheduleid");
        return new TimeSlot (secretcode, startdate, enddate, starttime, endtime, participant, scheduleid);
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

}