package com.amazonaws.lambda.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

import com.google.gson.Gson;

import com.amazonaws.lambda.db.*;
import com.amazonaws.lambda.model.*;

public class ParticipantShowScheduleHandler  implements RequestStreamHandler {
	
	public LambdaLogger logger = null;

	/** Load from RDS, if it exists
	 * 
	 * @throws Exception 
	 */
	ArrayList<TimeSlot> getTimeSlots(String scheduleid) throws Exception {
		if (logger != null) { logger.log("in getTimeSlots"); }
		DAO dao = new DAO();

		return dao.getTimeSlots(scheduleid);}

    @Override
	public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
		logger = context.getLogger();
		logger.log("Loading Java Lambda handler to create constant");

		JSONObject headerJson = new JSONObject();
		headerJson.put("Content-Type",  "application/json");  // not sure if needed anymore?
		headerJson.put("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
	    headerJson.put("Access-Control-Allow-Origin",  "*");
	        
		JSONObject responseJson = new JSONObject();
		responseJson.put("headers", headerJson);

		CreateScheduleResponse response = null;
		
  //  extract queryStringParameters from incoming HTTP POST request. If any error,  then return 422 error;
	String queryStringParameters;
	boolean processed = false;
	try {
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		JSONParser parser = new JSONParser();
		JSONObject event = (JSONObject) parser.parse(reader);
		logger.log("event:" + event.toJSONString());
		
		String method = (String) event.get("httpMethod");
		if (method != null && method.equalsIgnoreCase("OPTIONS")) {
			logger.log("Options request");
		//	List<TimeSlot>  TimeSlotsRequested = new 
		    response = new CreateScheduleResponse("name", 200);  // OPTIONS needs a 200 response
	        responseJson.put("queryStringParameters", new Gson().toJson(response));
	        processed = true;
	        queryStringParameters = null;
		} else {
			System.out.println("Testing");
			queryStringParameters = event.get("body").toString();
			//System.out.println(queryStringParameters);
			//logger.log(queryStringParameters);
			if (queryStringParameters == null) {
				queryStringParameters = event.toJSONString();  // this is only here to make testing easier
			}
		} 
		} catch (ParseException pe) {
			logger.log(pe.toString());
			response = new CreateScheduleResponse("Bad Request:" + pe.getMessage(), 422);  // unable to process input
	        responseJson.put("queryStringParameters", new Gson().toJson(response));
	        processed = true;
	        queryStringParameters = null;
		}if (!processed) {
			ParticipantShowScheduleRequest req = new Gson().fromJson(queryStringParameters, ParticipantShowScheduleRequest.class);
			logger.log(req.toString());

			ParticipantShowScheduleResponse resp;
			try {
				List<TimeSlot> list = getTimeSlots(req.scheduleid);
				resp = new ParticipantShowScheduleResponse(list, 200);
			} catch (Exception e) {
				resp = new ParticipantShowScheduleResponse(403);
			}

			// compute proper response
	        responseJson.put("body", new Gson().toJson(resp));  
		}
		 logger.log("end result:" + responseJson.toJSONString());
	        logger.log(responseJson.toJSONString());
	        OutputStreamWriter writer = new OutputStreamWriter(output, "UTF-8");
	        writer.write(responseJson.toJSONString());  
	        writer.close();
		}
	}


		
		
	