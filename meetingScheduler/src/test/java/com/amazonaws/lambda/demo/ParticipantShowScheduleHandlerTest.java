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

public class ParticipantShowScheduleHandlerTest {
	Context createContext(String apiCall) {
        TestContext ctx = new TestContext();
        ctx.setFunctionName(apiCall);
        return ctx;
    }

    @Test
    public void testshow() throws IOException {
        ParticipantShowScheduleHandler handler = new ParticipantShowScheduleHandler();

        ParticipantShowScheduleRequest ar = new ParticipantShowScheduleRequest("7X6R900HAH58YUODR6");
        String addRequest = new Gson().toJson(ar);
        String jsonRequest = new Gson().toJson(new PostRequest(addRequest));
        
        InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
        OutputStream output = new ByteArrayOutputStream();

        handler.handleRequest(input, output, createContext("close"));

        PostResponse post = new Gson().fromJson(output.toString(), PostResponse.class);
        ParticipantShowScheduleResponse resp = new Gson().fromJson(post.body, ParticipantShowScheduleResponse.class);
        Assert.assertEquals(resp.httpCode, 200);
        
    }
}
