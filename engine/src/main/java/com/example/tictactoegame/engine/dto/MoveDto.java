package com.example.tictactoegame.engine.dto;

import com.example.tictactoegame.engine.model.Player;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class MoveDto {

  private Player symbol;

  @Min(0)
  @Max(8)
  private int position;
}
