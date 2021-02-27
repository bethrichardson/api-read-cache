package com.netflix.repositories.config;

import com.netflix.repositories.client.MetricsCachingClient;
import feign.Feign;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfig {

    @Bean
    public MetricsCachingClient repositoryMetricsClient() {
        return Feign.builder()
                .decoder(new JsonOrTextDecoder())
                .target(MetricsCachingClient.class, "http://localhost:10000"); // TODO: configurable ports
    }

}
