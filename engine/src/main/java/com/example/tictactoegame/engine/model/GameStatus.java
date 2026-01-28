package com.example.tictactoegame.engine.model;

public enum GameStatus {

  IN_PROGRESS,
  WIN,
  DRAW;

  public static boolean isFinished(GameStatus status) {
    return status != IN_PROGRESS;
  }
}
