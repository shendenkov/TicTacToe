package com.example.tictactoegame.session.external;

import com.example.tictactoegame.session.exception.ServiceUnavailableException;
import com.example.tictactoegame.session.dto.GameDTO;
import com.example.tictactoegame.session.model.GameStatus;
import com.example.tictactoegame.session.dto.MoveDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@ConditionalOnProperty(
  name = "app.engine-service.protocol",
  havingValue = "http",
  matchIfMissing = true
)
public class GameEngineHttpGateway implements GameEngineGateway {

  private final RestClient restClient = RestClient.create();

  @Value("${app.engine-service.host}")
  private String engineServiceHost;

  @Override
  public GameDTO createNewGame(long id) {
    ResponseEntity<GameDTO> response = restClient.post()
      .uri("http://" + engineServiceHost + "/games/" + id)
      .retrieve()
      .toEntity(GameDTO.class);

    if (response.getStatusCode().isError()) {
      System.out.println("Engine service error: " + response);
      throw new ServiceUnavailableException("Can not create new game. Problem with engine service");
    }

    return response.getBody();
  }

  @Override
  public GameDTO getCurrentGameState(long id) {
    ResponseEntity<GameDTO> response = restClient.get()
      .uri("http://" + engineServiceHost + "/games/" + id)
      .retrieve()
      .toEntity(GameDTO.class);

    if (response.getStatusCode().isError()) {
      System.out.println("Engine service error: " + response);
      throw new ServiceUnavailableException("Can not get game state. Problem with engine service");
    }

    return response.getBody();
  }

  @Override
  public GameStatus makeMove(long id, MoveDTO move) {
    ResponseEntity<GameStatus> response = restClient.post()
      .uri("http://" + engineServiceHost + "/games/" + id + "/move")
      .contentType(MediaType.APPLICATION_JSON)
      .body(move)
      .retrieve()
      .toEntity(GameStatus.class);

    if (response.getStatusCode().isError()) {
      System.out.println("Engine service error: " + response);
      throw new ServiceUnavailableException("Can not make game move. Problem with engine service");
    }

    return response.getBody();
  }
}
