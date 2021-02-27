package com.netflix.repositories.domain.metrics

import com.netflix.repositories.ComponentTest
import com.netflix.repositories.client.RepositoryMetricsClient
import com.netflix.repositories.domain.metrics.repositories.RepositoryMetricCache
import com.spotify.github.v3.clients.RepositoryClient
import com.spotify.github.v3.repos.Repository
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification

import java.util.concurrent.CompletableFuture

@ComponentTest
class MetricsResourceSpec extends Specification implements MetricsTestingSupport {

    @Autowired
    RepositoryMetricsClient repositoryMetricsClient

    @Autowired
    RepositoryClient gitHubClient

    @Autowired
    RepositoryMetricCache cache

    def "should return a flat list of repos when requested"() {
        given:
        List<Repository> expectedList = buildRepositoryList(10)

        when:
        cache.refreshData()

        then:
        1 * gitHubClient.listOrganizationRepositories() >> CompletableFuture.completedFuture(expectedList)

        when:
        String actualList = repositoryMetricsClient.getOrganizationRepos("Netflix")

        then:
        assert actualList == expectedList.toString()
    }

    def "should return a list of top N forks"() {
        given:
        int numberRepos = 5
        List<Repository> expectedList = buildRepositoryList(10)

        when:
        cache.refreshData()

        then:
        1 * gitHubClient.listOrganizationRepositories() >> CompletableFuture.completedFuture(expectedList)

        when:
        List<List<Object>> actualList = repositoryMetricsClient.getTopRepositoriesByForks(numberRepos)

        then:
        assert actualList.size() == numberRepos
    }

    def "should return an empty list if result is not available"() {
        given:
        int numberRepos = 5

        when:
        cache.refreshData()

        then:
        1 * gitHubClient.listOrganizationRepositories() >> { throw new InterruptedException() }

        when:
        List<List<Object>> actualList = repositoryMetricsClient.getTopRepositoriesByForks(numberRepos)

        then:
        assert actualList.isEmpty()
    }

    def "should return all of the repos if that number is less than number requested"() {
        given:
        int numberRepos = 5
        List<Repository> expectedList = buildRepositoryList(3)

        when:
        cache.refreshData()

        then:
        1 * gitHubClient.listOrganizationRepositories() >> CompletableFuture.completedFuture(expectedList)

        when:
        List<List<Object>> actualList = repositoryMetricsClient.getTopRepositoriesByForks(numberRepos)

        then:
        assert actualList.size() == 3
    }

}