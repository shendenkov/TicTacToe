package com.example.tictactoegame.session.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class GameEventPublisher {

  private final WebSocketConnectionManager webSocketConnectionManager;
  private final ObjectMapper objectMapper;

  public void publish(String sessionId, Object payload) {
    String json;
    try {
      json = objectMapper.writeValueAsString(payload);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    TextMessage message = new TextMessage(json);

    webSocketConnectionManager.getWsConnections(sessionId)
      .forEach(wsConnection -> {
        try {
          if (wsConnection.isOpen()) {
            wsConnection.sendMessage(message);
          }
        } catch (IOException ignored) {}
      });
  }
}
