package es.mqm.webapp.controller;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
class NoSuchElementExceptionControllerAdvice {
 @ExceptionHandler(NoSuchElementException.class)
 public ResponseEntity<Map<String, Object>> handleNotFound(HttpServletRequest request) {
	 return buildResponse(HttpStatus.NOT_FOUND, "Resource not found", request.getRequestURI());
 }

 @ExceptionHandler(MethodArgumentTypeMismatchException.class)
 public ResponseEntity<Map<String, Object>> handleException(HttpServletRequest request) {
	 return buildResponse(HttpStatus.BAD_REQUEST, "Invalid request parameter", request.getRequestURI());
 }

 @ExceptionHandler(IllegalArgumentException.class)
 public ResponseEntity<Map<String, Object>> handleBadRequest(HttpServletRequest request) {
	 return buildResponse(HttpStatus.BAD_REQUEST, "Invalid request", request.getRequestURI());
 }

 private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String message, String path) {
	 Map<String, Object> body = new LinkedHashMap<>();
	 body.put("error", status.getReasonPhrase());
	 body.put("message", message);
	 body.put("path", path);
	 return ResponseEntity.status(status).body(body);
 }
}
