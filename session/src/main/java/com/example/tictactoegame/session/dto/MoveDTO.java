package com.example.tictactoegame.session.dto;

import com.example.tictactoegame.session.model.MoveEntity;
import com.example.tictactoegame.session.model.Player;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class MoveDTO {

  private Player symbol;
  private int position;

  public MoveDTO(MoveEntity move) {
    this.symbol = move.getSymbol();
    this.position = move.getPosition();
  }
}
