package com.example.tictactoegame.engine.dto;

import com.example.tictactoegame.engine.model.GameEntity;
import com.example.tictactoegame.engine.model.GameStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class GameDTO {

  private long gameId;
  private String state;
  private GameStatus status;

  public GameDTO(GameEntity game) {
    this.gameId = game.getId();
    this.state = game.getState();
    this.status = game.getStatus();
  }
}
