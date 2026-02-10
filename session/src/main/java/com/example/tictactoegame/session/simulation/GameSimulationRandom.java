package com.example.tictactoegame.session.simulation;

import com.example.tictactoegame.session.dto.GameDto;
import com.example.tictactoegame.session.dto.MoveDto;
import com.example.tictactoegame.session.dto.SessionDto;
import com.example.tictactoegame.session.external.GameEngineConnector;
import com.example.tictactoegame.session.model.GameStatus;
import com.example.tictactoegame.session.model.Player;
import com.example.tictactoegame.session.model.SessionEntity;
import com.example.tictactoegame.session.service.GameEventPublisher;
import com.example.tictactoegame.session.service.SessionService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
public class GameSimulationRandom implements GameSimulation {

  public static char EMPTY_CELL = '_';

  private final SessionService sessionService;
  private final GameEngineConnector gameEngineConnector;
  private final GameEventPublisher gameEventPublisher;

  @Setter
  private SessionEntity session;

  @Setter
  private volatile ScheduledFuture<?> thisTask;

  @Getter
  private GameStatus status;
  private Player player = Player.X;

  @Override
  public void run() {
    GameDto game = gameEngineConnector.getCurrentGameState(session.getId());
    status = game.getStatus();
    if (GameStatus.isFinished(status)) {
      thisTask.cancel(false);
      return;
    }

    int position = ThreadLocalRandom.current().nextInt(9);
    while (game.getState().charAt(position) != EMPTY_CELL) {
      position = ThreadLocalRandom.current().nextInt(9);
    }

    MoveDto move = new MoveDto(player, position);
    status = gameEngineConnector.makeMove(game.getGameId(), move);
    sessionService.saveMove(session, move);

    game = gameEngineConnector.getCurrentGameState(session.getId());
    gameEventPublisher.publish(String.valueOf(session.getId()), SessionDto.from(session, game));

    if (GameStatus.isFinished(status)) {
      thisTask.cancel(false);
      return;
    }

    player = player == Player.X ? Player.O : Player.X;
  }
}
