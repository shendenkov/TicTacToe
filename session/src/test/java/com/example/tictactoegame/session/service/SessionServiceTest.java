package com.example.tictactoegame.session.service;

import com.example.tictactoegame.session.dto.GameDto;
import com.example.tictactoegame.session.dto.SessionDto;
import com.example.tictactoegame.session.exception.ServiceUnavailableException;
import com.example.tictactoegame.session.external.GameEngineConnector;
import com.example.tictactoegame.session.model.*;
import com.example.tictactoegame.session.repository.MoveRepository;
import com.example.tictactoegame.session.repository.SessionRepository;
import com.example.tictactoegame.session.simulation.GameSimulation;
import com.example.tictactoegame.session.simulation.GameSimulationRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.scheduling.TaskScheduler;

import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.ScheduledFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SessionServiceTest {

  @Mock
  private SessionRepository sessionRepository;

  @Mock
  private MoveRepository moveRepository;

  @Mock
  private GameEngineConnector gameEngineConnector;

  @Mock
  private GameEventPublisher gameEventPublisher;

  @Mock
  private ObjectProvider<GameSimulation> simulationProvider;

  @Mock
  private TaskScheduler taskScheduler;

  @InjectMocks
  private SessionService sessionService;

  @BeforeEach
  void setUp() {
    reset(sessionRepository, moveRepository, gameEngineConnector, gameEventPublisher, simulationProvider, taskScheduler);
  }

  @Test
  void shouldCreateSession() {
    long sessionId = 1;
    SessionEntity mockSession = new SessionEntity()
      .setId(sessionId);
    GameDto mockGame = new GameDto(sessionId, "_________", GameStatus.IN_PROGRESS);
    when(sessionRepository.save(any())).thenReturn(mockSession);
    when(gameEngineConnector.createNewGame(sessionId)).thenReturn(mockGame);

    SessionDto session = sessionService.createSession();

    assertEquals(sessionId, session.getSessionId());
    assertTrue(session.getMoves().isEmpty());
    assertEquals(sessionId, session.getGame().getGameId());
    assertEquals("_________", session.getGame().getState());
    assertEquals(GameStatus.IN_PROGRESS, session.getGame().getStatus());
  }

  @Test
  void shouldThrowExceptionWhenGameEngineServiceUnavailableOnCreateSession() {
    long sessionId = 1;
    SessionEntity mockSession = new SessionEntity()
      .setId(sessionId);
    when(sessionRepository.save(any())).thenReturn(mockSession);
    when(gameEngineConnector.createNewGame(sessionId)).thenThrow(new ServiceUnavailableException(""));

    assertThrows(ServiceUnavailableException.class, () -> sessionService.createSession());
  }

  @Test
  void shouldReturnSession() {
    long sessionId = 2L;
    SessionEntity mockSession = new SessionEntity()
      .setId(sessionId);
    GameDto mockGame = new GameDto(sessionId, "_________", GameStatus.IN_PROGRESS);
    when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(mockSession));
    when(gameEngineConnector.getCurrentGameState(sessionId)).thenReturn(mockGame);

    SessionDto session = sessionService.getSession(sessionId);

    assertEquals(sessionId, session.getSessionId());
    assertTrue(session.getMoves().isEmpty());
    assertEquals(sessionId, session.getGame().getGameId());
    assertEquals("_________", session.getGame().getState());
    assertEquals(GameStatus.IN_PROGRESS, session.getGame().getStatus());
  }

  @Test
  void shouldThrowExceptionWhenGameEngineServiceUnavailableOnGetSession() {
    long sessionId = 2;
    SessionEntity mockSession = new SessionEntity()
      .setId(sessionId);
    when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(mockSession));
    when(gameEngineConnector.getCurrentGameState(sessionId)).thenThrow(new ServiceUnavailableException(""));

    assertThrows(ServiceUnavailableException.class, () -> sessionService.getSession(sessionId));
  }

  @Test
  void shouldStartGame() {
    long sessionId = 3L;
    SessionEntity mockSession = new SessionEntity()
      .setId(sessionId);
    GameDto mockGame = new GameDto(sessionId, "_________", GameStatus.IN_PROGRESS);
    when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(mockSession));
    when(simulationProvider.getObject()).thenReturn(new GameSimulationRandom(sessionService, gameEngineConnector, gameEventPublisher));
    when(gameEngineConnector.getCurrentGameState(sessionId)).thenReturn(mockGame);
    when(gameEngineConnector.makeMove(eq(sessionId), any())).thenReturn(GameStatus.IN_PROGRESS);

    sessionService.startGame(sessionId);

    ArgumentCaptor<GameSimulation> runnableCaptor = ArgumentCaptor.forClass(GameSimulation.class);
    verify(taskScheduler).scheduleWithFixedDelay(runnableCaptor.capture(), any());

    GameSimulation gameSimulation = runnableCaptor.getValue();
    gameSimulation.run();

    assertEquals(GameStatus.IN_PROGRESS, gameSimulation.getStatus());
  }

  @Test
  @SuppressWarnings("unchecked")
  void shouldStartAndWinGame() {
    long sessionId = 3L;
    SessionEntity mockSession = new SessionEntity()
      .setId(sessionId);
    GameDto mockGame = new GameDto(sessionId, "XX_OO____", GameStatus.IN_PROGRESS);
    when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(mockSession));
    when(simulationProvider.getObject()).thenReturn(new GameSimulationRandom(sessionService, gameEngineConnector, gameEventPublisher));
    when(taskScheduler.scheduleWithFixedDelay(any(), any())).thenReturn(Mockito.mock(ScheduledFuture.class));
    when(gameEngineConnector.getCurrentGameState(sessionId)).thenReturn(mockGame);
    when(gameEngineConnector.makeMove(eq(sessionId), any())).thenReturn(GameStatus.WIN);

    sessionService.startGame(sessionId);

    ArgumentCaptor<GameSimulation> runnableCaptor = ArgumentCaptor.forClass(GameSimulation.class);
    verify(taskScheduler).scheduleWithFixedDelay(runnableCaptor.capture(), any());

    GameSimulation gameSimulation = runnableCaptor.getValue();
    gameSimulation.run();

    assertEquals(GameStatus.WIN, gameSimulation.getStatus());
  }

  @Test
  @SuppressWarnings("unchecked")
  void shouldStartAndDrawGame() {
    long sessionId = 3L;
    SessionEntity mockSession = new SessionEntity()
      .setId(sessionId);
    GameDto mockGame = new GameDto(sessionId, "XXOOOXXO_", GameStatus.IN_PROGRESS);
    when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(mockSession));
    when(simulationProvider.getObject()).thenReturn(new GameSimulationRandom(sessionService, gameEngineConnector, gameEventPublisher));
    when(taskScheduler.scheduleWithFixedDelay(any(), any())).thenReturn(Mockito.mock(ScheduledFuture.class));
    when(gameEngineConnector.getCurrentGameState(sessionId)).thenReturn(mockGame);
    when(gameEngineConnector.makeMove(eq(sessionId), any())).thenReturn(GameStatus.DRAW);

    sessionService.startGame(sessionId);

    ArgumentCaptor<GameSimulation> runnableCaptor = ArgumentCaptor.forClass(GameSimulation.class);
    verify(taskScheduler).scheduleWithFixedDelay(runnableCaptor.capture(), any());

    GameSimulation gameSimulation = runnableCaptor.getValue();
    gameSimulation.run();

    assertEquals(GameStatus.DRAW, gameSimulation.getStatus());
  }

  @Test
  void shouldThrowExceptionWhenGameAlreadyStartedOnStartGame() {
    long sessionId = 3L;
    SessionEntity mockSession = new SessionEntity()
      .setId(sessionId);
    mockSession.setMoves(Collections.singletonList(new MoveEntity(mockSession, Player.X, 0)));
    when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(mockSession));

    assertThrows(IllegalStateException.class, () -> sessionService.startGame(sessionId));
  }
}
