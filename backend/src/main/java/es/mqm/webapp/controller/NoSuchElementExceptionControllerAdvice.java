package es.mqm.webapp.controller;

import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
class NoSuchElementExceptionControllerAdvice {
 @ResponseStatus(HttpStatus.NOT_FOUND)
 @ExceptionHandler(NoSuchElementException.class)
 public void handleNotFound() {
 }

 @ResponseStatus(HttpStatus.BAD_REQUEST)
 @ExceptionHandler(MethodArgumentTypeMismatchException.class)
 public void handleException() {
 }
}