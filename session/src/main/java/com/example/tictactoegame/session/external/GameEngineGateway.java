package com.example.tictactoegame.session.external;

import com.example.tictactoegame.session.dto.GameDto;
import com.example.tictactoegame.session.model.GameStatus;
import com.example.tictactoegame.session.dto.MoveDto;

public interface GameEngineGateway {

  GameDto createNewGame(long id);
  GameDto getCurrentGameState(long id);
  GameStatus makeMove(long id, MoveDto move);
}
