package com.example.tictactoegame.session.service;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketConnectionManager {

  private final Map<String, Set<WebSocketSession>> wsConnections = new ConcurrentHashMap<>();

  public void add(String sessionId, WebSocketSession wsConnection) {
    wsConnections
      .computeIfAbsent(sessionId, k -> ConcurrentHashMap.newKeySet())
      .add(wsConnection);
  }

  public void remove(String sessionId, WebSocketSession wsConnection) {
    Set<WebSocketSession> set = wsConnections.get(sessionId);
    if (set != null) {
      set.remove(wsConnection);
    }
  }

  public Set<WebSocketSession> getWsConnections(String sessionId) {
    return wsConnections.getOrDefault(sessionId, Set.of());
  }
}
