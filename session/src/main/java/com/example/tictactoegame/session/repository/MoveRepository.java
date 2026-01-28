package com.example.tictactoegame.session.repository;

import com.example.tictactoegame.session.model.MoveEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MoveRepository extends JpaRepository<MoveEntity, Long> {
}
