package com.example.tictactoegame.engine.controller;

import com.example.tictactoegame.engine.dto.GameDTO;
import com.example.tictactoegame.engine.model.GameStatus;
import com.example.tictactoegame.engine.dto.MoveDTO;
import com.example.tictactoegame.engine.service.GameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/games")
public class GameController {

  private final GameService gameService;

  public GameController(GameService gameService) {
    this.gameService = gameService;
  }

  @PostMapping("/{gameId}")
  @Operation(
    summary = "Creates a new game",
    description = "Creates a new game with specified gameId"
  )
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Processed successfully",
      content = {@Content(mediaType = "application/json", schema = @Schema(implementation = GameDTO.class))}),
    @ApiResponse(responseCode = "400", description = "Create failed", content = @Content),
    @ApiResponse(responseCode = "409", description = "Game already exists", content = @Content)
  })
  public ResponseEntity<Object> createGame(@PathVariable("gameId") long id) {
    GameDTO game = gameService.createGame(id);
    return ResponseEntity.ok(game);
  }

  @PostMapping("/{gameId}/move")
  @Operation(
    summary = "Makes the game move",
    description = "Makes the game move by symbol (X|O) and position ([0-8])"
  )
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Processed successfully",
      content = {@Content(mediaType = "application/json", schema = @Schema(implementation = GameStatus.class))}),
    @ApiResponse(responseCode = "400", description = "Move failed", content = @Content),
    @ApiResponse(responseCode = "404", description = "Game not found", content = @Content)
  })
  public ResponseEntity<Object> makeMove(@PathVariable("gameId") long id, @RequestBody @Valid MoveDTO move) {
    GameStatus status = gameService.makeMove(id, move);
    return ResponseEntity.ok(status);
  }

  @GetMapping("/{gameId}")
  @Operation(
    summary = "Returns short game state",
    description = "Returns game board and status"
  )
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Processed successfully",
      content = {@Content(mediaType = "application/json", schema = @Schema(implementation = GameDTO.class))}),
    @ApiResponse(responseCode = "404", description = "Game not found", content = @Content)
  })
  public ResponseEntity<Object> getGame(@PathVariable("gameId") long id) {
    GameDTO game = gameService.getGame(id);
    return ResponseEntity.ok(game);
  }
}
