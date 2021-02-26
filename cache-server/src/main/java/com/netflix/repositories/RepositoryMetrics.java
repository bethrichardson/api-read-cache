package com.netflix.repositories;

import com.netflix.repositories.config.RepositoryMetricsConfig;
import com.netflix.repositories.domain.github.GithubConfig;
import com.netflix.repositories.domain.metrics.caching.CachingConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.health.HealthEndpointAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.metrics.web.servlet.WebMvcMetricsAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.HttpEncodingAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@ComponentScan("com.netflix.repositories.domain")
@Import({
        RepositoryMetricsConfig.class,
        CachingConfig.class,
        HealthEndpointAutoConfiguration.class,
        HttpMessageConvertersAutoConfiguration.class,
        HttpEncodingAutoConfiguration.class,
        WebMvcMetricsAutoConfiguration.class,
        ServletWebServerFactoryAutoConfiguration.class,
        GithubConfig.class
})
@EnableWebMvc
@EnableAutoConfiguration
@EnableConfigurationProperties(WebMvcProperties.class)
public class RepositoryMetrics {

    public static void main(String[] args) {
        SpringApplication.run(RepositoryMetrics.class, args);
    }

}
