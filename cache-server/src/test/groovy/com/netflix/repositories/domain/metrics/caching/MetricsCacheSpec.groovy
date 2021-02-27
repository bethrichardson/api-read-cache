package com.netflix.repositories.domain.metrics.caching

import com.netflix.repositories.ComponentTest
import com.netflix.repositories.common.RepositoryMetric
import com.netflix.repositories.domain.metrics.MetricType
import com.netflix.repositories.domain.metrics.MetricsCollector
import spock.lang.Specification
import spock.util.concurrent.PollingConditions

import java.time.Duration

@ComponentTest
class MetricsCacheSpec extends Specification  {

    StubMetricsCollector metricsCollector
    MetricsCache cache
    TimeProvider timeProvider
    List<RepositoryMetric> expectedList = []
    int numberRepos = 5

    def setup() {
        numberRepos.times {
            RepositoryMetric repository = Mock(RepositoryMetric)
            expectedList.add(repository)
        }
        metricsCollector = new StubMetricsCollector(expectedList)
        timeProvider = new StubTimeProvider()
        cache = new MetricsCache(metricsCollector, timeProvider)
    }

    def "should call out to get a list of Netflix repos when initialized and pull value from cache for subsequent requests"() {
        when:
        cache.initializeCache()

        then:
        metricsCollector.interactionCount == 1

        and:
        List<RepositoryMetric> actual = cache.getMetric(MetricType.FORKS)
        assert actual.size() == numberRepos
    }

    def "should call out to get a list of Netflix repos if a value in the key is older than 5 minutes"() {
        when:
        cache.initializeCache()

        then:
        metricsCollector.interactionCount == 1

        when:
        timeProvider.nanoTime += cache.getRefreshInterval().toNanos() + 1
        cache.getMetric(MetricType.FORKS)

        then:
        new PollingConditions(timeout: 30).eventually {
            metricsCollector.interactionCount == 2
        }
    }

    def "should call out to get a list of Netflix repos at the duration set on the cache even if not requested"() {
        given:
        cache.setRefreshInterval(Duration.ofMillis(500))

        when:
        cache.initializeCache()
        timeProvider.nanoTime += cache.getRefreshInterval().toNanos() + 1

        then:
        new PollingConditions(delay: 1, timeout: 1).eventually {
            metricsCollector.interactionCount == 2
        }

        cleanup:
        cache.cacheRefresher.cancel()
    }

    private class StubMetricsCollector implements MetricsCollector {

        List<RepositoryMetric> list
        int interactionCount = 0;

        StubMetricsCollector(List<RepositoryMetric> list) {
            this.list = list
        }

        @Override
        List<RepositoryMetric> getForkMetrics() {
            interactionCount++;
            return list;
        }
    }

    static class StubTimeProvider extends TimeProvider {
        long nanoTime = System.nanoTime()

        long getNanoTime() {
            nanoTime
        }
    }


}