package com.example.tictactoegame.engine.repository;

import com.example.tictactoegame.engine.model.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<GameEntity, Long> {
}
