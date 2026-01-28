package com.example.tictactoegame.session.model;

public enum GameStatus {

  IN_PROGRESS,
  WIN,
  DRAW;

  public static boolean isFinished(GameStatus status) {
    return status != IN_PROGRESS;
  }
}
