/**
 * Copyright 2021 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.netflix.apireadcache;

import com.netflix.apireadcache.metrics.caching.CacheConfig;
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
@ComponentScan("com.netflix.apireadcache.metrics")
@Import({
        HealthEndpointAutoConfiguration.class,
        HttpMessageConvertersAutoConfiguration.class,
        HttpEncodingAutoConfiguration.class,
        WebMvcMetricsAutoConfiguration.class,
        ServletWebServerFactoryAutoConfiguration.class,
        CacheConfig.class,
})
@EnableWebMvc
@EnableAutoConfiguration
@EnableConfigurationProperties(WebMvcProperties.class)
public class ApiReadCache {

    public static void main(String[] args) {
        SpringApplication.run(ApiReadCache.class, args);
    }

}
