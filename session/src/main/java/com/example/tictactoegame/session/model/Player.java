package com.example.tictactoegame.session.model;

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
