package com.netflix.repositories;

import com.netflix.repositories.domain.metrics.proxied.ProxiedMetricsConfig;
import com.netflix.repositories.domain.metrics.repositories.RepositoryMetricsConfig;
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

@SuppressWarnings("PMD.UseUtilityClass")
@Configuration
@ComponentScan("com.netflix.repositories.domain")
@Import({
        HealthEndpointAutoConfiguration.class,
        HttpMessageConvertersAutoConfiguration.class,
        HttpEncodingAutoConfiguration.class,
        WebMvcMetricsAutoConfiguration.class,
        ServletWebServerFactoryAutoConfiguration.class,
        RepositoryMetricsConfig.class,
        ProxiedMetricsConfig.class,
})
@EnableWebMvc
@EnableAutoConfiguration
@EnableConfigurationProperties(WebMvcProperties.class)
public class MetricsCacher {

    public static void main(String[] args) {
        SpringApplication.run(MetricsCacher.class, args);
    }

}
