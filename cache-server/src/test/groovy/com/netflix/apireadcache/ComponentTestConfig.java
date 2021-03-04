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

import com.netflix.apireadcache.config.ClientConfig;
import com.netflix.apireadcache.metrics.github.ProxiedGitHubClient;
import org.kohsuke.github.GitHub;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import spock.mock.DetachedMockFactory;

@Configuration
@EnableAutoConfiguration
@Import({
        ApiReadCache.class,
        ClientConfig.class
})
public class ComponentTestConfig {

    private final DetachedMockFactory mockFactory = new DetachedMockFactory();

    @Bean
    @Primary
    public GitHub mockGitHubClient() {
        return mockFactory.Mock(GitHub.class);
    }

    @Bean
    @Primary
    public ProxiedGitHubClient mockCachingGitHubClient() {
        return mockFactory.Mock(ProxiedGitHubClient.class);
    }

}
