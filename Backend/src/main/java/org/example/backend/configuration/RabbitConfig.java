package org.example.backend.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    Queue newMeasurementsQueue(){
        return new Queue("q.new_measurements");
    }
    @Bean
    Queue errorQueue(){
        return new Queue("q.errors");
    }
}

