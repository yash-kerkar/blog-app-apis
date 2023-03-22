package com.blogapp.blog.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.blogapp.blog.payloads.APIResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<APIResponse> resourceNotFoundException(ResourceNotFoundException exception){
		String message = exception.getMessage();
		APIResponse apiResponse = new APIResponse(message,false);
		return new ResponseEntity<>(apiResponse,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> methodArgsNotFoundException(MethodArgumentNotValidException exception){
		Map<String, String> map = new HashMap<>();
		exception.getBindingResult().getAllErrors().forEach(err->{
			String field = ((FieldError)err).getField();
			String message = err.getDefaultMessage();
			map.put(field, message);
		});
		return new ResponseEntity<>(map,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(ApiException.class)
	public ResponseEntity<APIResponse> apiException(ApiException exception){
		String message = exception.getMessage();
		APIResponse apiResponse = new APIResponse(message,false);
		return new ResponseEntity<>(apiResponse,HttpStatus.BAD_REQUEST);
	}
}
