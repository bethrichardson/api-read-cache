package com.netflix.repositories.domain.metrics.caching

import com.netflix.repositories.ComponentTest
import com.netflix.repositories.common.RepositoryMetric
import com.netflix.repositories.domain.metrics.ViewType
import com.netflix.repositories.domain.metrics.MetricsCollector
import com.netflix.repositories.domain.metrics.MetricsTestingSupport
import com.spotify.github.v3.repos.Repository
import spock.lang.Specification
import spock.util.concurrent.PollingConditions

import java.time.Duration

@ComponentTest
class MetricsCacheSpec extends Specification implements MetricsTestingSupport {

    StubMetricsCollector metricsCollector
    MetricsCache cache
    List<Repository> expectedList = []
    int numberRepos = 5

    def setup() {
        expectedList = buildRepositoryList(numberRepos)
        metricsCollector = new StubMetricsCollector(expectedList)
        cache = new MetricsCache(metricsCollector)
    }

    def "should call out to get a list of Netflix repos when initialized and pull value from cache for subsequent requests"() {
        when:
        cache.initializeCache()

        then:
        metricsCollector.interactionCount == 1

        and:
        List<List<Object>> actual = cache.getView(ViewType.FORKS, numberRepos)
        assert actual.size() == numberRepos
    }

    def "should call out to get a list of Netflix repos at the duration set on the cache even if not requested"() {
        given:
        cache.setRefreshInterval(Duration.ofMillis(500))

        when:
        cache.initializeCache()

        then:
        new PollingConditions(delay: 1, timeout: 1).eventually {
            metricsCollector.interactionCount > 2
        }

        cleanup:
        cache.cacheRefresher.cancel()
    }

    private class StubMetricsCollector implements MetricsCollector {

        List<Repository> list
        int interactionCount = 0;

        StubMetricsCollector(List<RepositoryMetric> list) {
            this.list = list
        }

        @Override
        List<RepositoryMetric> getRepositories() {
            interactionCount++;
            return list;
        }
    }

}