package com.netflix.repositories.domain.metrics.caching

import com.netflix.repositories.ComponentTest
import com.netflix.repositories.client.RepositoryMetricsClient
import com.netflix.repositories.common.RepositoryMetric
import com.netflix.repositories.domain.github.GithubService
import com.netflix.repositories.domain.metrics.MetricType
import com.spotify.github.v3.clients.GitHubClient
import com.spotify.github.v3.clients.RepositoryClient
import com.spotify.github.v3.repos.Repository
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification
import spock.util.concurrent.PollingConditions

import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

@ComponentTest
class MetricsCacheSpec extends Specification  {

    GithubService mockGithubService

    MetricsCache cache
    TimeProvider timeProvider

    def setup() {
        mockGithubService = Mock(GithubService)
        timeProvider = new StubTimeProvider()
        cache = new MetricsCache(mockGithubService, timeProvider)
    }

    def "should call out to get a list of Netflix repos when initialized and pull value from cache for subsequent requests"() {
        given:
        int numberRepos = 5
        List<RepositoryMetric> expectedList = []
        numberRepos.times {
            RepositoryMetric repository = Mock(RepositoryMetric)
            expectedList.add(repository)
        }

        when:
        cache.initializeCache()

        then:
        1 * mockGithubService.getForkMetrics() >> expectedList

        and:
        List<RepositoryMetric> actual = cache.getMetric(MetricType.FORKS)
        assert actual.size() == numberRepos
    }

    def "should call out to get a list of Netflix repos if a value in the key is older than 5 minutes"() {
        given:
        int numberRepos = 5
        List<RepositoryMetric> expectedList = []
        numberRepos.times {
            RepositoryMetric repository = Mock(RepositoryMetric)
            expectedList.add(repository)
        }

        when:
        cache.initializeCache()

        then:
        1 * mockGithubService.getForkMetrics() >> expectedList

        when:
        timeProvider.nanoTime += MetricsCache.REFRESH_INTERVAL.toNanos() + 1
        cache.getMetric(MetricType.FORKS)

        then:
        new PollingConditions(timeout: 30).eventually {
            1 * mockGithubService.getForkMetrics() >> expectedList
        }
    }

    static class StubTimeProvider extends TimeProvider {
        long nanoTime = System.nanoTime()

        long getNanoTime() {
            nanoTime
        }
    }


}