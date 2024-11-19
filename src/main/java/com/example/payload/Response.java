package com.example.payload;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;

@Getter
public class Response<T> {

	private int status;
	private String message;
	private T data;
	
	private Response(int status, String message, T data) {
		this.status = status;
		this.message = message;
		this.data = data;
	}
	
	public static Response<Void> success(String message) {
		return new Response<Void>(HttpServletResponse.SC_OK, message, null);
	}

	public static <T> Response<T> success(T data) {
		return new Response<T>(HttpServletResponse.SC_OK, null, data);
	}
	
	public static <T> Response<T> success(T data, String message) {
		return new Response<T>(HttpServletResponse.SC_OK, message, data);		
	}
	
	public static Response<Void> fail(int status, String message) {
		return new Response<Void>(status, message, null);		
	}
	
	public static <T> Response<T> fail(int status, String message, T errorData) {
		return new Response<T>(status, message, errorData);		
	}
}
