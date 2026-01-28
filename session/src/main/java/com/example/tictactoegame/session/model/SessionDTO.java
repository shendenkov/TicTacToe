package com.example.tictactoegame.session.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class SessionDTO {

  private long sessionId;
  private List<MoveDTO> moves;
  private GameDTO game;

  public SessionDTO(SessionEntity session, GameDTO game) {
    this.sessionId = session.getId();
    this.moves = session.getMoves().stream()
      .map(MoveDTO::new)
      .collect(Collectors.toList());
    this.game = game;
  }
}
