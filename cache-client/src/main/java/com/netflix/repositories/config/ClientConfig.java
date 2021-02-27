package com.netflix.repositories.config;

import com.netflix.repositories.client.RepositoryMetricsClient;
import feign.Feign;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfig {

    @Bean
    public RepositoryMetricsClient repositoryMetricsClient() {
        return Feign.builder()
                .decoder(new JsonOrTextDecoder())
                .target(RepositoryMetricsClient.class, "http://localhost:10000"); // TODO: configurable ports
    }

}
