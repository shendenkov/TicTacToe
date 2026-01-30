package com.example.tictactoegame.session.service;

import com.example.tictactoegame.session.dto.GameDTO;
import com.example.tictactoegame.session.dto.MoveDTO;
import com.example.tictactoegame.session.external.GameEngineGateway;
import com.example.tictactoegame.session.model.*;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadLocalRandom;

public class GameSimulation implements Runnable {

  public static char EMPTY_CELL = '_';

  private final SessionService sessionService;
  private final GameEngineGateway gameEngineGateway;

  private final SessionEntity session;

  @Setter
  private volatile ScheduledFuture<?> thisTask;

  @Getter
  private GameStatus status;
  private Player player = Player.X;

  public GameSimulation(SessionService sessionService, GameEngineGateway gameEngineGateway, SessionEntity session) {
    this.sessionService = sessionService;
    this.gameEngineGateway = gameEngineGateway;
    this.session = session;
  }

  @Override
  public void run() {
    GameDTO game = gameEngineGateway.getCurrentGameState(session.getId());
    status = game.getStatus();
    if (GameStatus.isFinished(status)) {
      thisTask.cancel(false);
      return;
    }

    int position = ThreadLocalRandom.current().nextInt(9);
    while (game.getState().charAt(position) != EMPTY_CELL) {
      position = ThreadLocalRandom.current().nextInt(9);
    }

    MoveDTO move = new MoveDTO(player, position);
    status = gameEngineGateway.makeMove(game.getGameId(), move);
    sessionService.saveMove(session, move);

    if (GameStatus.isFinished(status)) {
      thisTask.cancel(false);
      return;
    }

    player = player == Player.X ? Player.O : Player.X;
  }
}
