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

public class OpenTimeSlotHandlerTest {

	Context createContext(String apiCall) {
        TestContext ctx = new TestContext();
        ctx.setFunctionName(apiCall);
        return ctx;
    }

    @Test
    public void testshow() throws IOException {
        OpenTimeSlotHandler handler = new OpenTimeSlotHandler();

        OpenTimeSlotRequest ar = new OpenTimeSlotRequest("7X6R900HAH58YUODR6", "09:00:00", "2018-12-05");
        String addRequest = new Gson().toJson(ar);
        String jsonRequest = new Gson().toJson(new PostRequest(addRequest));
        
        InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
        OutputStream output = new ByteArrayOutputStream();

        handler.handleRequest(input, output, createContext("add"));

        PostResponse post = new Gson().fromJson(output.toString(), PostResponse.class);
        OpenTimeSlotResponse resp = new Gson().fromJson(post.body, OpenTimeSlotResponse.class);
        Assert.assertEquals(resp.response, "Unable to update time slot on  [2018-12-05 at 09:00:00]");
    }
}
