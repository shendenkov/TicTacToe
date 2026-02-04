package com.example.tictactoegame.session.controller;

import com.example.tictactoegame.session.service.WebSocketConnectionManager;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {

  private final WebSocketConnectionManager manager;

  @Override
  public void afterConnectionEstablished(@NonNull WebSocketSession wsConnection) {
    String wsSessionId = extractSessionId(wsConnection);
    manager.add(wsSessionId, wsConnection);
  }

  @Override
  public void afterConnectionClosed(@NonNull WebSocketSession wsConnection, @NonNull CloseStatus wsStatus) {
    String sessionId = extractSessionId(wsConnection);
    manager.remove(sessionId, wsConnection);
  }

  private String extractSessionId(WebSocketSession wsConnection) {
    String path = Objects.requireNonNull(wsConnection.getUri()).getPath();
    return path.substring(path.lastIndexOf("/") + 1);
  }
}
