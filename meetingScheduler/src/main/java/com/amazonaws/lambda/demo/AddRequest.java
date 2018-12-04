package com.amazonaws.lambda.demo;

public class AddRequest {
	String arg1;
	String arg2;
	
	public AddRequest (String a1, String a2) {
		this.arg1 = a1;
		this.arg2 = a2;
	}
	
	public String toString() {
		return "Add(" + arg1 + "," + arg2 + ")";
	}
}
