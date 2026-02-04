package com.example.tictactoegame.session.simulation;

import com.example.tictactoegame.session.model.GameStatus;
import com.example.tictactoegame.session.model.SessionEntity;

import java.util.concurrent.ScheduledFuture;

public interface GameSimulation extends Runnable {

  void setSession(SessionEntity session);
  void setThisTask(ScheduledFuture<?> thisTask);
  GameStatus getStatus();
}
