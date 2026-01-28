package com.example.tictactoegame.engine.service;

import com.example.tictactoegame.engine.exception.ConflictException;
import com.example.tictactoegame.engine.exception.NotFoundException;
import com.example.tictactoegame.engine.model.*;
import com.example.tictactoegame.engine.repository.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GameServiceTest {

  @Mock
  private GameRepository gameRepository;

  @InjectMocks
  private GameService gameService;

  @BeforeEach
  void setUp() {
    reset(gameRepository);
  }

  @Test
  void shouldCreateGame() {
    long gameId = 1L;
    GameEntity mockGame = new GameEntity(gameId);
    when(gameRepository.save(mockGame)).thenReturn(mockGame);

    GameDTO game = gameService.createGame(gameId);

    assertEquals(gameId, game.getGameId());
    assertEquals(GameEntity.EMPTY_BOARD, game.getState());
    assertEquals(GameStatus.IN_PROGRESS, game.getStatus());
  }

  @Test
  void shouldThrowExceptionWhenWrongIdOnCreateGame() {
    long gameId = -1L;

    assertThrows(IllegalArgumentException.class, () -> gameService.createGame(gameId));
  }

  @Test
  void shouldThrowExceptionWhenIdExistedOnCreateGame() {
    long gameId = 1L;
    GameEntity mockGame = new GameEntity(gameId);
    when(gameRepository.findById(gameId)).thenReturn(Optional.of(mockGame));

    assertThrows(ConflictException.class, () -> gameService.createGame(gameId));
  }

  @Test
  void shouldReturnGame() {
    long gameId = 2L;
    GameEntity mockGame = new GameEntity(gameId);
    when(gameRepository.findById(gameId)).thenReturn(Optional.of(mockGame));

    GameDTO game = gameService.getGame(gameId);

    assertEquals(gameId, game.getGameId());
    assertEquals(GameEntity.EMPTY_BOARD, game.getState());
    assertEquals(GameStatus.IN_PROGRESS, game.getStatus());
  }

  @Test
  void shouldThrowExceptionWhenGameNotFoundOnGetGame() {
    long gameId = 2L;
    when(gameRepository.findById(gameId)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> gameService.getGame(gameId));
  }

  @Test
  void shouldMakeMove() {
    long gameId = 3L;
    GameEntity mockGame = new GameEntity(gameId);
    MoveDTO move = new MoveDTO(Player.X, 0);
    when(gameRepository.findById(gameId)).thenReturn(Optional.of(mockGame));

    GameStatus status = gameService.makeMove(gameId, move);

    assertEquals(GameStatus.IN_PROGRESS, status);
  }

  @Test
  void shouldWinOnMakeMove() {
    long gameId = 3L;
    GameEntity mockGame = new GameEntity(gameId)
      .setState("XX_OO____")
      .setCurrentTurn(Player.X);
    MoveDTO move = new MoveDTO(Player.X, 2);
    when(gameRepository.findById(gameId)).thenReturn(Optional.of(mockGame));

    GameStatus status = gameService.makeMove(gameId, move);

    assertEquals(GameStatus.WIN, status);
  }

  @Test
  void shouldDrawOnMakeMove() {
    long gameId = 3L;
    GameEntity mockGame = new GameEntity(gameId)
      .setState("XXOOOXXO_")
      .setCurrentTurn(Player.X);
    MoveDTO move = new MoveDTO(Player.X, 8);
    when(gameRepository.findById(gameId)).thenReturn(Optional.of(mockGame));

    GameStatus status = gameService.makeMove(gameId, move);

    assertEquals(GameStatus.DRAW, status);
  }

  @Test
  void shouldThrowExceptionWhenGameNotFoundOnMakeMove() {
    long gameId = 3L;
    MoveDTO move = new MoveDTO(Player.X, 0);
    when(gameRepository.findById(gameId)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> gameService.makeMove(gameId, move));
  }

  @Test
  void shouldThrowExceptionWhenGameFinishedOnMakeMove() {
    long gameId = 3L;
    GameEntity mockGame = new GameEntity(gameId)
      .setState("XXXOO____")
      .setStatus(GameStatus.WIN)
      .setWinner(Player.X)
      .setCurrentTurn(Player.X);
    MoveDTO move = new MoveDTO(Player.O, 5);
    when(gameRepository.findById(gameId)).thenReturn(Optional.of(mockGame));

    assertThrows(IllegalStateException.class, () -> gameService.makeMove(gameId, move));
  }

  @Test
  void shouldThrowExceptionWhenWrongPlayerOnMakeMove() {
    long gameId = 3L;
    GameEntity mockGame = new GameEntity(gameId)
      .setState("X________")
      .setCurrentTurn(Player.O);
    MoveDTO move = new MoveDTO(Player.X, 1);
    when(gameRepository.findById(gameId)).thenReturn(Optional.of(mockGame));

    assertThrows(IllegalArgumentException.class, () -> gameService.makeMove(gameId, move));
  }

  @Test
  void shouldThrowExceptionWhenCellOccupiedOnMakeMove() {
    long gameId = 3L;
    GameEntity mockGame = new GameEntity(gameId)
      .setState("X________")
      .setCurrentTurn(Player.O);
    MoveDTO move = new MoveDTO(Player.O, 0);
    when(gameRepository.findById(gameId)).thenReturn(Optional.of(mockGame));

    assertThrows(IllegalArgumentException.class, () -> gameService.makeMove(gameId, move));
  }
}
