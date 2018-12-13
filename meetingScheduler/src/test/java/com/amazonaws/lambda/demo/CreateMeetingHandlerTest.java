package com.amazonaws.lambda.demo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Assert;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.Gson;

public class CreateMeetingHandlerTest {
	Context createContext(String apiCall) {
        TestContext ctx = new TestContext();
        ctx.setFunctionName(apiCall);
        return ctx;
    }

    @Test
    public void testshow() throws IOException {
    	CreateMeetingHandler handler = new CreateMeetingHandler();

    	CreateMeetingRequest ar = new CreateMeetingRequest("2018-12-12", "2018-12-24", "09:00:00", "18:00:00", "Thar", "7X6R900HAH58YUODR6");
        String addRequest = new Gson().toJson(ar);
        String jsonRequest = new Gson().toJson(new PostRequest(addRequest));
        
        InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
        OutputStream output = new ByteArrayOutputStream();

        handler.handleRequest(input, output, createContext("close"));

        PostResponse post = new Gson().fromJson(output.toString(), PostResponse.class);
        CreateMeetingResponse resp = new Gson().fromJson(post.body, CreateMeetingResponse.class);
        Assert.assertEquals(resp.response, "Unable to create meeting from [2018-12-12 to 2018-12-24] for participant:Thar");
        
    }
}
