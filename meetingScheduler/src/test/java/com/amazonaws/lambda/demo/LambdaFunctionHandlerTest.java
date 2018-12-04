package com.amazonaws.lambda.demo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.junit.Assert;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.Gson;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class LambdaFunctionHandlerTest {

	Context createContext(String apiCall) {
        TestContext ctx = new TestContext();
        ctx.setFunctionName(apiCall);
        return ctx;
    }

    @Test
    public void testAddTwoNumbers() throws IOException {
        CreateScheduleHandler handler = new CreateScheduleHandler();

        AddRequest ar = new AddRequest("10.24", "15.7");
        String addRequest = new Gson().toJson(ar);
        String jsonRequest = new Gson().toJson(new PostRequest(addRequest));
        
        InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
        OutputStream output = new ByteArrayOutputStream();

        handler.handleRequest(input, output, createContext("add"));

        PostResponse post = new Gson().fromJson(output.toString(), PostResponse.class);
        AddResponse resp = new Gson().fromJson(post.body, AddResponse.class);
        Assert.assertEquals(25.94, resp.value, 0.00001);
    }

}
