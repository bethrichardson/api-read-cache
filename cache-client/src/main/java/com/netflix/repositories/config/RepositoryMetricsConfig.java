package com.netflix.repositories.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.repositories.client.RepositoryMetricsClient;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryMetricsConfig {

    @Autowired
    ApplicationContext context;

    @Bean
    public RepositoryMetricsClient repositoryMetricsClient() {
        return Feign.builder()
                .decoder(new JacksonDecoder(new ObjectMapper()))
                .target(RepositoryMetricsClient.class, "http://localhost:10000"); // TODO: configurable ports
    }

}
