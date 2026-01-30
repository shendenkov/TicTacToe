package com.example.tictactoegame.engine.service;

import com.example.tictactoegame.engine.dto.GameDTO;
import com.example.tictactoegame.engine.dto.MoveDTO;
import com.example.tictactoegame.engine.exception.ConflictException;
import com.example.tictactoegame.engine.exception.NotFoundException;
import com.example.tictactoegame.engine.model.*;
import com.example.tictactoegame.engine.repository.GameRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GameService {

  public static char EMPTY_CELL = '_';

  private final GameRepository repository;

  private final int[][] possibleWins = {
    {0, 1, 2}, {3, 4, 5}, {6, 7, 8},
    {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
    {0, 4, 8}, {2, 4, 6}
  };

  public GameService(GameRepository repository) {
    this.repository = repository;
  }

  @Transactional
  public GameDTO createGame(long id) {
    if (id <= 0) {
      throw new IllegalArgumentException("Game id can not be zero or negative");
    }
    repository.findById(id)
      .ifPresent(game -> {
        throw new ConflictException("Game with such id already exists");
      });

    GameEntity game = repository.save(new GameEntity(id));
    return new GameDTO(game);
  }

  @Transactional(readOnly = true)
  public GameDTO getGame(long id) {
    GameEntity game = getGameEntity(id);
    return new GameDTO(game);
  }

  @Transactional
  public GameStatus makeMove(long id, MoveDTO move) {
    GameEntity game = getGameEntity(id);

    validateGame(game);
    validateMove(game, move);

    changeState(game, move);
    checkWinner(game);
    changePlayer(game);
    repository.save(game);

    return game.getStatus();
  }

  private GameEntity getGameEntity(long id) {
    return repository.findById(id)
      .orElseThrow(() -> new NotFoundException("Game not found"));
  }

  private void validateGame(GameEntity game) {
    if (GameStatus.isFinished(game.getStatus())) {
      throw new IllegalStateException("Game already finished");
    }
  }

  private void validateMove(GameEntity game, MoveDTO move) {
    if (move.getSymbol() != game.getCurrentTurn()) {
      throw new IllegalArgumentException("Wrong player turn");
    }
    if (game.getState().charAt(move.getPosition()) != EMPTY_CELL) {
      throw new IllegalArgumentException("Cell already occupied");
    }
  }

  private void changeState(GameEntity game, MoveDTO move) {
    StringBuilder stringBuilder = new StringBuilder(game.getState());
    stringBuilder.setCharAt(move.getPosition(), move.getSymbol().getSymbol());
    game.setState(stringBuilder.toString());
  }

  private void checkWinner(GameEntity game) {
    String board = game.getState();

    for (int[] winLine : possibleWins) {
      char cellSymbol = board.charAt(winLine[0]);
      if (cellSymbol != EMPTY_CELL
        && cellSymbol == board.charAt(winLine[1])
        && cellSymbol == board.charAt(winLine[2])) {
        game.setStatus(GameStatus.WIN)
          .setWinner(game.getCurrentTurn() == Player.X ? Player.X : Player.O);
        return;
      }
    }

    if (board.indexOf(EMPTY_CELL) == -1) {
      game.setStatus(GameStatus.DRAW);
    }
  }

  private void changePlayer(GameEntity game) {
    if (!GameStatus.isFinished(game.getStatus())) {
      game.setCurrentTurn(game.getCurrentTurn() == Player.X ? Player.O : Player.X);
    }
  }
}
