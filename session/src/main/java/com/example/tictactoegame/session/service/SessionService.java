package com.example.tictactoegame.session.service;

import com.example.tictactoegame.session.exception.NotFoundException;
import com.example.tictactoegame.session.external.GameEngineGateway;
import com.example.tictactoegame.session.model.*;
import com.example.tictactoegame.session.repository.MoveRepository;
import com.example.tictactoegame.session.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.concurrent.ScheduledFuture;

@Service
public class SessionService {

  private final SessionRepository sessionRepository;
  private final MoveRepository moveRepository;

  private final TaskScheduler taskScheduler;
  private final GameEngineGateway gameEngineGateway;

  @Value("${app.player.sleep-interval}")
  private long playerSleepInterval;

  public SessionService(SessionRepository sessionRepository, MoveRepository moveRepository, TaskScheduler taskScheduler,
                        GameEngineGateway gameEngineGateway) {
    this.sessionRepository = sessionRepository;
    this.moveRepository = moveRepository;
    this.taskScheduler = taskScheduler;
    this.gameEngineGateway = gameEngineGateway;
  }

  @Transactional
  public SessionDTO createSession() {
    SessionEntity session = sessionRepository.save(new SessionEntity());

    GameDTO game = gameEngineGateway.createNewGame(session.getId());

    return new SessionDTO(session, game);
  }

  @Transactional(readOnly = true)
  public SessionDTO getSession(long id) {
    SessionEntity session = getSessionEntity(id);

    GameDTO game = gameEngineGateway.getCurrentGameState(id);

    return new SessionDTO(session, game);
  }

  @Transactional(readOnly = true)
  public void startGame(long id) {
    SessionEntity session = getSessionEntity(id);

    if (!session.getMoves().isEmpty()) {
      throw new IllegalStateException("Game already started");
    }

    startGameSimulation(session);
  }

  private SessionEntity getSessionEntity(long id) {
    return sessionRepository.findById(id)
      .orElseThrow(() -> new NotFoundException("Session not found"));
  }

  protected void startGameSimulation(SessionEntity session) {
    GameSimulation task = new GameSimulation(this, gameEngineGateway, session);
    ScheduledFuture<?> future = taskScheduler.scheduleWithFixedDelay(task, Duration.ofMillis(playerSleepInterval));
    task.setThisTask(future);
  }

  @Transactional
  protected void saveMove(SessionEntity session, MoveDTO move) {
    moveRepository.save(new MoveEntity(session, move.getSymbol(), move.getPosition()));
  }
}
