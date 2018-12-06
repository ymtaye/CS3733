package com.amazonaws.lambda.db;

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
            System.out.println("\n\n\n");
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
            
            Date startD = (Date) dayofyear.parse(schedule.startdate);
            Date endD = (Date) dayofyear.parse(schedule.startdate);
            
            Date startT = (Date) hourofday.parse(schedule.daystarthour);
            Date endT = (Date) hourofday.parse(schedule.dayendhour);
            
            String dayofmeetingString = "";
            String MstartString = "";
            String MendString = "";
            Date dayofmeeting = (Date) dayofyear.parse(schedule.startdate);
            Date MstartTDate;
            Date MendTDate;
            
            int numdays = (int) ((startD.getTime()-endD.getTime())/(1000*60*60*24));
            int numrows = (int) ((startT.getTime()-endT.getTime())/(schedule.meetinglength*60000));
            long days = dayofmeeting.getTime()/(1000*60*60*24);
            long MstartT = dayofmeeting.getTime();
            long MendT = dayofmeeting.getTime();
            
            for (int j=0;j<numdays;j++) {
            	// increment dayofmeeting one day ahead
            	days = days + j;
            	dayofmeeting = new Date(days);
            	// find a way to get the string
            	dayofmeetingString = dayofyear.format(dayofmeeting);
            	
            	for (int i=0;i<numrows;i++) {
            		
            		// longs for the start and end time
            		MstartT = (dayofmeeting.getTime())+((schedule.meetinglength*60000)*i); // date.getTime()+((slotlength*60000)*i);
            		MendT = (dayofmeeting.getTime())+((schedule.meetinglength*60000)*(i+1));
            		// dates for the start and end time
            		MstartTDate = new Date(MstartT);
                    MendTDate = new Date(MendT);
                    // Strings for the start and end time
                    MstartString = hourofday.format(MstartTDate);
                    MendString = hourofday.format(MendTDate);
                    
            		PreparedStatement ps = conn.prepareStatement("INSERT INTO TimeSlots (id, secretcode, startdate, enddate, starttime, endtime, participant, scheduleid) values(?,?,?,?,?,?,?,?);");
                    System.out.println("\n\n\n");
                    ps.setString(1, getSaltString());
                    ps.setString(2, getSaltString());
                    ps.setString(3, dayofmeetingString);
                    ps.setString(4, dayofmeetingString);
                    ps.setString(5, MstartString);
                    ps.setString(6, MendString);
                    ps.setString(7, "");
                    ps.setString(8, schedule.id);
                    ps.execute();
                  
                }
            	
            	days = days - j;
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