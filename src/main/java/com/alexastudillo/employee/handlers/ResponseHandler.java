package com.alexastudillo.employee.handlers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseHandler {
	public ResponseEntity<Object> generateResponse(final String message, final HttpStatus httpStatus,
			final Object response) {
		final Map<String, Object> map = new HashMap<String, Object>();
		map.put("message", message);
		map.put("status", httpStatus.value());
		map.put("data", response);
		return new ResponseEntity<Object>(map, httpStatus);
	}

	public ResponseEntity<Object> generateResponse(final Map<String, Object> map, final HttpStatus httpStatus) {
		return new ResponseEntity<Object>(map, httpStatus);
	}

	public ResponseEntity<Object> generateResponseWithoutData(final String message, final HttpStatus httpStatus) {
		final Map<String, Object> map = new HashMap<String, Object>();
		map.put("message", message);
		map.put("status", httpStatus.value());
		return new ResponseEntity<Object>(map, httpStatus);
	}
}
