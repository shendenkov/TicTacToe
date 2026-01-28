package com.example.tictactoegame.session.external;

import com.example.tictactoegame.session.model.GameDTO;
import com.example.tictactoegame.session.model.GameStatus;
import com.example.tictactoegame.session.model.MoveDTO;

public interface GameEngineGateway {

  GameDTO createNewGame(long id);
  GameDTO getCurrentGameState(long id);
  GameStatus makeMove(long id, MoveDTO move);
}
