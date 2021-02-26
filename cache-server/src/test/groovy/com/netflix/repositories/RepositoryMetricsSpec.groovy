package com.netflix.repositories

import com.netflix.repositories.client.RepositoryMetricsClient
import com.netflix.repositories.common.RepositoryMetric
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification

@ComponentTest
class RepositoryMetricsSpec extends Specification  {

    @Autowired
    RepositoryMetricsClient client

    def "should call out to get a list of Netflix repos"() {
        given:
        int numberRepos = 5

        when:
        List<List<RepositoryMetric>> list = client.getTopRepositoriesByForks(numberRepos)

        then:
        list.isEmpty()
    }

}