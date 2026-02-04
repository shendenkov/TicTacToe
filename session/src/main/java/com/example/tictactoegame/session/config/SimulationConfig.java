package com.example.tictactoegame.session.config;

import com.example.tictactoegame.session.external.GameEngineGateway;
import com.example.tictactoegame.session.service.GameEventPublisher;
import com.example.tictactoegame.session.simulation.GameSimulation;
import com.example.tictactoegame.session.simulation.GameSimulationRandom;
import com.example.tictactoegame.session.service.SessionService;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class SimulationConfig {

  @Bean
  @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  @ConditionalOnProperty(name = "app.simulation.type", havingValue = "random")
  public GameSimulation gameSimulation(SessionService sessionService, GameEngineGateway gameEngineGateway,
                                       GameEventPublisher gameEventPublisher) {
    return new GameSimulationRandom(sessionService, gameEngineGateway, gameEventPublisher);
  }
}
