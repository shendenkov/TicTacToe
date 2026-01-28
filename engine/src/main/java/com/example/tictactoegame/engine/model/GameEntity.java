package com.example.tictactoegame.engine.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Entity
@Table(name = "games")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
@Accessors(chain = true)
public class GameEntity {

  public static final String EMPTY_BOARD = "_________";

  @Id
  protected long id;

  /*
    Chars X and O are player's move, char _ means empty cell.
    String char position means game board position:
    0 1 2
    3 4 5
    6 7 8
  */
  @Column(name="state", length=9, nullable=false)
  private String state = EMPTY_BOARD;

  @Column(name="currentTurn", length=1, nullable=false)
  @Enumerated(EnumType.STRING)
  private Player currentTurn = Player.X;

  @Column(name="status", length=20, nullable=false)
  @Enumerated(EnumType.STRING)
  private GameStatus status = GameStatus.IN_PROGRESS;

  @Column(name="winner", length=1)
  @Enumerated(EnumType.STRING)
  private Player winner;

  public GameEntity(long id) {
    this.id = id;
  }
}
