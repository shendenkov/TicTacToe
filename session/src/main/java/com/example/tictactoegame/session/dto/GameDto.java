package com.example.tictactoegame.session.dto;

import com.example.tictactoegame.session.model.GameStatus;
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

  @Setter
  private GameStatus status;
}
