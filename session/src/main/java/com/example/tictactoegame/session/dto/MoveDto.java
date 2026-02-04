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
public class MoveDto {

  private Player symbol;
  private int position;

  public static MoveDto from(MoveEntity move) {
    return new MoveDto(move.getSymbol(), move.getPosition());
  }
}
