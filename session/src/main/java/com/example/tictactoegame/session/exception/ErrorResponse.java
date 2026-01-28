package com.example.tictactoegame.session.exception;

public class ErrorResponse {

  public int status;
  public String message = "";

  public ErrorResponse(Throwable e, int status) {
    this.status = status;
    this.message = null == e.getMessage() ? "" : e.getMessage().replace("com.example.tictactoegame.session.exception.", "");
  }
}
