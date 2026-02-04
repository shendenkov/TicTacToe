package com.example.tictactoegame.session.controller;

import com.example.tictactoegame.session.dto.SessionDto;
import com.example.tictactoegame.session.service.SessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sessions")
@RequiredArgsConstructor
public class SessionController {

  private final SessionService sessionService;

  @PostMapping
  @Operation(
    summary = "Creates a new game session",
    description = "Creates a new game session"
  )
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Processed successfully",
      content = {@Content(mediaType = "application/json", schema = @Schema(implementation = SessionDto.class))}),
    @ApiResponse(responseCode = "400", description = "Create failed", content = @Content)
  })
  public ResponseEntity<Object> createSession() {
    SessionDto session = sessionService.createSession();
    return ResponseEntity.ok(session);
  }

  @PostMapping("/{sessionId}/simulate")
  @Operation(
    summary = "Starts game",
    description = "Triggers the automated simulation of a game"
  )
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Processed successfully", content = @Content),
    @ApiResponse(responseCode = "400", description = "Move failed", content = @Content),
    @ApiResponse(responseCode = "404", description = "Game not found", content = @Content)
  })
  public ResponseEntity<Object> startGame(@PathVariable("sessionId") long id) {
    sessionService.startGame(id);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/{sessionId}")
  @Operation(
    summary = "Returns session details",
    description = "Returns current game state and move history"
  )
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Processed successfully",
      content = {@Content(mediaType = "application/json", schema = @Schema(implementation = SessionDto.class))}),
    @ApiResponse(responseCode = "404", description = "Game not found", content = @Content)
  })
  public ResponseEntity<Object> getSession(@PathVariable("sessionId") long id) {
    SessionDto session = sessionService.getSession(id);
    return ResponseEntity.ok(session);
  }
}
