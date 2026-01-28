package com.example.tictactoegame.engine.model;

import lombok.Getter;

public enum Player {

  O('O'),
  X('X');

  @Getter
  private final char symbol;

  Player(char symbol) {
    this.symbol = symbol;
  }
}
