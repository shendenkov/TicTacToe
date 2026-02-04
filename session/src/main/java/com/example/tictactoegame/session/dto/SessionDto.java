package com.example.tictactoegame.session.dto;

import com.example.tictactoegame.session.model.SessionEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class SessionDto {

  private long sessionId;
  private List<MoveDto> moves;
  private GameDto game;

  public static SessionDto from(SessionEntity session, GameDto game) {
    List<MoveDto> moves = session.getMoves().stream()
      .map(MoveDto::from)
      .collect(Collectors.toList());
    return new SessionDto(session.getId(), moves, game);
  }
}
