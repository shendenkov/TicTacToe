package com.example.tictactoegame.session.integration;

import com.example.tictactoegame.session.model.GameStatus;
import com.example.tictactoegame.session.dto.SessionDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

//@Testcontainers
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GameFlowIntegrationTest {

  @Container
  private static final GenericContainer<?> engineService =
    new GenericContainer<>(
      new ImageFromDockerfile()
        .withDockerfile(Path.of("../engine/Dockerfile"))
    );

  /*@Container
  private static final GenericContainer<?> engineService =
    new GenericContainer<>("ttt/engine-service:test")
      .withExposedPorts(8081);*/

  @Autowired
  private TestRestTemplate rest;

  @DynamicPropertySource
  private static void engineProps(DynamicPropertyRegistry registry) {
    registry.add("app.engine-service.host", () -> "http://" + engineService.getHost() + ":" + engineService.getMappedPort(8081));
  }

  //@Test
  void fullGameFlow() {
    ResponseEntity<SessionDTO> sessionResponse = rest.postForEntity("/sessions", HttpEntity.EMPTY, SessionDTO.class);

    assertEquals(HttpStatus.OK, sessionResponse.getStatusCode());
    assertNotNull(sessionResponse.getBody());
    assertEquals(GameStatus.IN_PROGRESS, sessionResponse.getBody().getGame().getStatus());

    long sessionId = sessionResponse.getBody().getSessionId();
    ResponseEntity<Void> response = rest.postForEntity("/sessions/" + sessionId + "/simulate", HttpEntity.EMPTY, Void.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());

    GameStatus status = sessionResponse.getBody().getGame().getStatus();
    while (status == GameStatus.IN_PROGRESS) {
      sessionResponse = rest.getForEntity("/sessions/" + sessionId, SessionDTO.class);

      assertEquals(HttpStatus.OK, sessionResponse.getStatusCode());
      assertNotNull(sessionResponse.getBody());

      status = sessionResponse.getBody().getGame().getStatus();
    }
  }
}
