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

public class DeleteOldSchedulesHandlerTest {
	Context createContext(String apiCall) {
        TestContext ctx = new TestContext();
        ctx.setFunctionName(apiCall);
        return ctx;
    }

    @Test
    public void testshow() throws IOException {
    	DeleteOldSchedulesHandler handler = new DeleteOldSchedulesHandler();

    	DeleteOldSchedulesRequest ar = new DeleteOldSchedulesRequest(7);
        String addRequest = new Gson().toJson(ar);
        String jsonRequest = new Gson().toJson(new PostRequest(addRequest));
        
        InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
        OutputStream output = new ByteArrayOutputStream();

        handler.handleRequest(input, output, createContext("close"));

        PostResponse post = new Gson().fromJson(output.toString(), PostResponse.class);
        DeleteOldSchedulesResponse resp = new Gson().fromJson(post.body, DeleteOldSchedulesResponse.class);
        Assert.assertEquals(resp.response, "Unable to delete schedules for  [7]");
    }
}
