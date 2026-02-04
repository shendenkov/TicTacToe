package com.example.tictactoegame.session.service;

import com.example.tictactoegame.session.dto.GameDto;
import com.example.tictactoegame.session.dto.MoveDto;
import com.example.tictactoegame.session.dto.SessionDto;
import com.example.tictactoegame.session.exception.NotFoundException;
import com.example.tictactoegame.session.external.GameEngineGateway;
import com.example.tictactoegame.session.model.MoveEntity;
import com.example.tictactoegame.session.model.SessionEntity;
import com.example.tictactoegame.session.repository.MoveRepository;
import com.example.tictactoegame.session.repository.SessionRepository;
import com.example.tictactoegame.session.simulation.GameSimulation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.concurrent.ScheduledFuture;

@Service
@RequiredArgsConstructor
public class SessionService {

  private final SessionRepository sessionRepository;
  private final MoveRepository moveRepository;
  private final GameEngineGateway gameEngineGateway;
  private final ObjectProvider<GameSimulation> simulationProvider;
  private final TaskScheduler taskScheduler;

  @Value("${app.simulation.sleep-interval}")
  private long playerSleepInterval;

  @Transactional
  public SessionDto createSession() {
    SessionEntity session = sessionRepository.save(new SessionEntity());

    GameDto game = gameEngineGateway.createNewGame(session.getId());

    return SessionDto.from(session, game);
  }

  @Transactional(readOnly = true)
  public SessionDto getSession(long id) {
    SessionEntity session = getSessionEntity(id);

    GameDto game = gameEngineGateway.getCurrentGameState(id);

    return SessionDto.from(session, game);
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
    GameSimulation gameSimulation = simulationProvider.getObject();
    gameSimulation.setSession(session);

    ScheduledFuture<?> future = taskScheduler.scheduleWithFixedDelay(gameSimulation, Duration.ofMillis(playerSleepInterval));
    gameSimulation.setThisTask(future);
  }

  @Transactional
  public void saveMove(SessionEntity session, MoveDto move) {
    moveRepository.save(new MoveEntity(session, move.getSymbol(), move.getPosition()));
  }
}
