package com.netflix.apireadcache.config;

import com.netflix.apireadcache.client.ApiReadCache;
import feign.Feign;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfig {

    @Bean
    public ApiReadCache repositoryMetricsClient() {
        return Feign.builder()
                .decoder(new JsonOrTextDecoder())
                .target(ApiReadCache.class, "http://localhost:10000"); // TODO: configurable ports
    }

}
