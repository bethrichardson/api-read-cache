package com.netflix.apireadcache.config;

import com.netflix.apireadcache.client.ApiReadCache;
import feign.Feign;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfig {

    @Value("${server.port}")
    String serverPort;

    @Value("${server.url}")
    String baseUrl;

    @Bean
    public ApiReadCache repositoryMetricsClient() {
        return Feign.builder()
                .decoder(new JsonOrTextDecoder())
                .target(ApiReadCache.class, baseUrl + ":" + serverPort);
    }
}
