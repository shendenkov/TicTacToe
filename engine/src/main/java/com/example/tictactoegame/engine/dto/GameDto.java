package com.example.tictactoegame.engine.dto;

import com.example.tictactoegame.engine.model.GameEntity;
import com.example.tictactoegame.engine.model.GameStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class GameDto {

  private long gameId;
  private String state;
  private GameStatus status;

  public static GameDto from(GameEntity game) {
    return new GameDto(game.getId(), game.getState(), game.getStatus());
  }
}
