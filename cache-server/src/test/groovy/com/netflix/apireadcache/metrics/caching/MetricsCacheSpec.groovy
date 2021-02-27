package com.netflix.apireadcache.metrics.caching

import com.netflix.apireadcache.ComponentTest
import com.netflix.apireadcache.metrics.MetricCollector
import com.netflix.apireadcache.metrics.MetricTuple
import com.netflix.apireadcache.metrics.MetricsTestingSupport
import com.netflix.apireadcache.metrics.ViewType
import com.netflix.apireadcache.metrics.repositories.RepositoryMetric
import com.spotify.github.v3.repos.Repository
import spock.lang.Specification
import spock.util.concurrent.PollingConditions

import java.time.Duration

@ComponentTest
class MetricsCacheSpec extends Specification implements MetricsTestingSupport {

    StubMetricTupleCollector metricsCollector
    MetricsCache cache
    List<Repository> expectedList = []
    int numberRepos = 5

    def setup() {
        expectedList = buildRepositoryList(numberRepos)
        metricsCollector = new StubMetricTupleCollector(expectedList)
        cache = new MetricsCache(metricsCollector)
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
        given:
        cache.setRefreshInterval(Duration.ofMillis(500))

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
    }

}