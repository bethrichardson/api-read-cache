package com.netflix.repositories;

import com.netflix.repositories.config.RepositoryMetricsConfig;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import spock.mock.DetachedMockFactory;

@Configuration
@EnableAutoConfiguration
@Import({
        RepositoryMetrics.class,
        RepositoryMetricsConfig.class
})
public class ComponentTestConfig {

    private final DetachedMockFactory mockFactory = new DetachedMockFactory();

}
