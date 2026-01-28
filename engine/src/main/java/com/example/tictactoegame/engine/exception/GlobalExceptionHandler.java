package com.example.tictactoegame.engine.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(Throwable.class)
  public ResponseEntity<Object> handleError(Throwable ex) {
    HttpStatus status = getStatus(ex);
    if (status.value() == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
      ex.printStackTrace();
    }
    ErrorResponse errorResponse = new ErrorResponse(ex, status.value());
    return new ResponseEntity<>(errorResponse, status);
  }

  private HttpStatus getStatus(Throwable ex) {
    if (ex instanceof IllegalArgumentException
      || ex instanceof IllegalStateException
      || ex instanceof HttpMessageNotReadableException) {
      return HttpStatus.BAD_REQUEST;
    } else if (ex instanceof NotFoundException) {
      return HttpStatus.NOT_FOUND;
    } else if (ex instanceof HttpRequestMethodNotSupportedException) {
      return HttpStatus.METHOD_NOT_ALLOWED;
    } else if (ex instanceof HttpMediaTypeNotAcceptableException) {
      return HttpStatus.NOT_ACCEPTABLE;
    } else if (ex instanceof ConflictException) {
      return HttpStatus.CONFLICT;
    }
    return HttpStatus.INTERNAL_SERVER_ERROR;
  }
}
