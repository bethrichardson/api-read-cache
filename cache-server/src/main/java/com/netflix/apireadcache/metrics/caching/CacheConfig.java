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
package com.netflix.apireadcache.metrics.caching;

import com.netflix.apireadcache.metrics.MetricType;
import com.netflix.apireadcache.metrics.github.GithubConfig;
import com.netflix.apireadcache.metrics.github.ProxiedGitHubClient;
import com.netflix.apireadcache.metrics.github.GitHubPageReader;
import com.netflix.apireadcache.metrics.proxied.ProxiedMetric;
import com.netflix.apireadcache.metrics.proxied.ProxiedMetricCache;
import com.netflix.apireadcache.metrics.proxied.ProxiedMetricCollector;
import com.netflix.apireadcache.metrics.repositories.RepositoryMetricCache;
import com.netflix.apireadcache.metrics.repositories.RepositoryMetricCollector;
import com.spotify.github.v3.clients.GitHubClient;
import com.spotify.github.v3.clients.RepositoryClient;
import feign.Response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Function;

import static com.netflix.apireadcache.metrics.github.GithubConfig.NETFLIX;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Configuration
@Import({
        GithubConfig.class,
})
public class CacheConfig {

    /**
     * New metrics are collected at this frequency
     *
     * Default is 5 minutes
     */
    @Value("${cache.refresh.frequency:PT5M}")
    private Duration refreshFrequency;

    /**
     * The number of threads to use for cache refresh
     *
     * Default is 2
     */
    @Value("${cache.refresh.threads:2}")
    private int corePoolSize;

    @Bean
    public ScheduledExecutorService cacheExecutorService() {
        return Executors.newScheduledThreadPool(corePoolSize);
    }

    @Autowired
    private ScheduledExecutorService cacheExecutorService;

    @Bean
    public ProxiedMetricCache overviewMetricCache(ProxiedGitHubClient client) {
        ProxiedMetricCollector collector = new ProxiedMetricCollector(MetricType.OVERVIEW, ()-> new ProxiedMetric(client.getOverview()));
        ProxiedMetricCache cache = new ProxiedMetricCache(collector, cachingStrategy());
        cache.initializeCache();
        return cache;
    }

    @Bean
    public ProxiedMetricCache organizationMetricCache(ProxiedGitHubClient client) {
        ProxiedMetricCollector collector = new ProxiedMetricCollector(MetricType.ORGANIZATION, ()-> new ProxiedMetric(client.getOrganization(NETFLIX)));
        ProxiedMetricCache cache = new ProxiedMetricCache(collector, cachingStrategy());
        cache.initializeCache();
        return cache;
    }

    @Bean
    public ProxiedMetricCache membersMetricCache(ProxiedGitHubClient client) {
        ProxiedMetricCollector collector = new ProxiedMetricCollector(MetricType.MEMBERS, ()-> new ProxiedMetric(client.getOrganizationMembers(NETFLIX)));
        ProxiedMetricCache cache = new ProxiedMetricCache(collector, cachingStrategy());
        cache.initializeCache();
        return cache;
    }

    @Bean
    public ProxiedMetricCache repositoryViewCache(ProxiedGitHubClient client, GitHubPageReader gitHubPageReader) {
        Function<Integer, Response> getRepositoryPage = (Integer page) -> client.getRepositoryView(NETFLIX, page);
        ProxiedMetricCollector collector = new ProxiedMetricCollector(MetricType.REPOSITORIES, ()->
                new ProxiedMetric(gitHubPageReader.getAll(getRepositoryPage)));
        ProxiedMetricCache cache = new ProxiedMetricCache(collector, cachingStrategy());
        cache.initializeCache();
        return cache;
    }

    @Bean
    RepositoryClient repositoryClient(GitHubClient gitHubClient) {
        return gitHubClient.createRepositoryClient(NETFLIX, null);
    }

    @Bean
    RepositoryMetricCollector repositoryMetricCollector(RepositoryClient repositoryClient) {
        return new RepositoryMetricCollector(repositoryClient);
    }

    @Bean
    public RepositoryMetricCache repositoryMetricCache(RepositoryMetricCollector repositoryMetricsCollector) {
        RepositoryMetricCache cache = new RepositoryMetricCache(repositoryMetricsCollector, cachingStrategy());
        cache.initializeCache();
        return cache;
    }

    @Bean
    public CachingStrategy cachingStrategy() {
        return CachingStrategy
                .builder()
                .executorService(cacheExecutorService)
                .refreshFrequency(refreshFrequency)
                .build();
    }

}
