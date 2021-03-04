/**
 * Copyright 2021 Netflix, Inc.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.netflix.apireadcache.metrics.github;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.apireadcache.config.JsonOrTextDecoder;
import com.spotify.github.v3.clients.GitHubClient;
import feign.Feign;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.net.URI;

@Import({
        ObjectMapperConfig.class
})
@Configuration
public class GithubConfig {

    public static final String NETFLIX = "Netflix";

    @Getter
    @Value("${GITHUB_API_TOKEN}")
    private transient String apiToken;

    @Getter
    @Value("${github.api.url}")
    private transient String apiUrl;

    @Bean
    public GithubCredentials githubCredentials() {
        return GithubCredentials.builder()
                .apiToken(apiToken)
                .apiUrl(apiUrl)
                .build();
    }

    @Bean
    public GitHubPageReader pageTurner(ProxiedGitHubClient client, ObjectMapper mapper) {
        return GitHubPageReader
                .builder()
                .objectMapper(mapper)
                .client(client)
                .build();
    }

    @Bean
    public ProxiedGitHubClient cachingGithubClient() {
        return Feign.builder()
                .decoder(new JsonOrTextDecoder())
                .requestInterceptor(new AuthorizationRequestInterceptor(githubCredentials()))
                .target(ProxiedGitHubClient.class, apiUrl);
    }

    @Bean
    GitHubClient spotifyGithubClient(GithubCredentials credentials) {
        return GitHubClient.create(URI.create(apiUrl), credentials.getApiToken());
    }

}
