package com.example.tictactoegame.session.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "moves")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class MoveEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  protected long id;

  @ManyToOne
  @JoinColumn(name="session_id", nullable=false)
  private SessionEntity session;

  @Column(name="symbol", length=1, nullable=false)
  @Enumerated(EnumType.STRING)
  private Player symbol;

  @Column(name="position", nullable=false)
  private int position;

  public MoveEntity(SessionEntity session, Player symbol, int position) {
    this.session = session;
    this.symbol = symbol;
    this.position = position;
  }
}
