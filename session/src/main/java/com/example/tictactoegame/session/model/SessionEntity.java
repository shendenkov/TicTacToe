package com.example.tictactoegame.session.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sessions")
@Data
@Accessors(chain = true)
public class SessionEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  protected long id;

  @OneToMany(mappedBy = "session")
  @OrderBy("id ASC")
  private List<MoveEntity> moves = new ArrayList<>();
}
