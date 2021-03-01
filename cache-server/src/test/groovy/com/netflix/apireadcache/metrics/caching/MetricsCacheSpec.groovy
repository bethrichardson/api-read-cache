/*
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
package com.netflix.apireadcache.metrics.caching

import com.netflix.apireadcache.ComponentTest
import com.netflix.apireadcache.metrics.MetricCollector
import com.netflix.apireadcache.metrics.MetricTuple
import com.netflix.apireadcache.metrics.MetricType
import com.netflix.apireadcache.metrics.MetricsTestingSupport
import com.netflix.apireadcache.metrics.ViewType
import com.netflix.apireadcache.metrics.repositories.RepositoryMetric
import com.spotify.github.v3.repos.Repository
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification
import spock.util.concurrent.PollingConditions

import java.time.Duration
import java.util.concurrent.ExecutorService
import java.util.concurrent.ScheduledExecutorService

@ComponentTest
class MetricsCacheSpec extends Specification implements MetricsTestingSupport {

    @Autowired
    ScheduledExecutorService scheduledExecutorService

    StubMetricTupleCollector metricsCollector
    MetricsCache cache
    List<Repository> expectedList = []
    int numberRepos = 5

    def setup() {
        expectedList = buildRepositoryList(numberRepos)
        metricsCollector = new StubMetricTupleCollector(expectedList)
        CachingStrategy cachingStrategy = CachingStrategy.builder()
                                            .executorService(scheduledExecutorService)
                                            .refreshFrequency(Duration.ofMillis(500))
                                            .build()
        cache = new MetricsCache(metricsCollector, cachingStrategy)
    }

    def "should call out to get a list of Netflix repos when initialized and pull value from cache for subsequent requests"() {
        when:
        cache.initializeCache()

        then:
        metricsCollector.interactionCount == 1

        and:
        List<MetricTuple> actual = cache.getView(ViewType.FORKS, numberRepos)
        assert actual.size() == numberRepos
    }

    def "should call out to get a list of Netflix repos at the duration set on the cache even if not requested"() {
        when:
        cache.initializeCache()

        then:
        new PollingConditions(delay: 1, timeout: 2).eventually {
            metricsCollector.interactionCount > 2
        }

        cleanup:
        cache.cacheRefresher.cancel()
    }

    private class StubMetricTupleCollector implements MetricCollector<List<Repository>> {

        RepositoryMetric value
        int interactionCount = 0;

        StubMetricTupleCollector(List<Repository> list) {
            value = new RepositoryMetric(list)
            value
        }

        @Override
        RepositoryMetric getMetric() {
            interactionCount++;
            return value;
        }

        @Override
        ViewType[] getSupportedViews() {
            return ViewType.values()
        }

        @Override
        MetricType getMetricType() {
            return MetricType.REPOSITORY_METRICS
        }
    }

}