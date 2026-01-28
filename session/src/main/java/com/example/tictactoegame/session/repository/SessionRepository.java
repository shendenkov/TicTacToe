package com.example.tictactoegame.session.repository;

import com.example.tictactoegame.session.model.SessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<SessionEntity, Long> {
}