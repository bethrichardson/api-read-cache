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
package com.netflix.apireadcache.metrics

import com.fasterxml.jackson.databind.ObjectMapper
import com.netflix.apireadcache.ComponentTest
import com.netflix.apireadcache.client.ApiReadCache
import com.netflix.apireadcache.metrics.github.ProxiedGitHubClient
import feign.Request
import feign.Response
import org.kohsuke.github.GHOrganization
import org.kohsuke.github.GHRepository
import org.kohsuke.github.GitHub
import org.kohsuke.github.PagedIterable
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification

import static com.netflix.apireadcache.metrics.github.GithubConfig.NETFLIX

@ComponentTest
class MetricsResourceSpec extends Specification {

    @Autowired
    ApiReadCache metricsCachingClient

    @Autowired
    GitHub gitHubClient

    @Autowired
    ProxiedGitHubClient cachingGitHubClient

    @Autowired
    MetricsService metricsService

    @Autowired
    ObjectMapper mapper

    def "should call through to API when unhandled path requested"() {
        given:
        String unhandledPath = "/gists"
        Object fakeResult = '''
            {
              "url": "https://api.github.com/gists/bafa226fdfc58b078276bc741fff82ca"
            }
        '''

        when:
        Object actual = metricsCachingClient.getApiEndpoint(unhandledPath)

        then:
        1 * cachingGitHubClient.getUnhandledRoute(unhandledPath) >> fakeResult

        then:
        assert actual.toString().contains("bafa226fdfc58b078276bc741fff82ca")
    }

    def "should return a root node overview when requested"() {
        given:
        Object fakeResult = '''
            {
                "current_user_url": "https://api.github.com/user",
                "current_user_authorizations_html_url": "https://github.com/settings/connections/applications{/client_id}",
                "authorizations_url": "https://api.github.com/authorizations"
            }
        '''

        when:
        metricsService.refreshAllData()

        then:
        1 * cachingGitHubClient.getOverview() >> fakeResult

        when:
        Object actual = metricsCachingClient.getOverview()

        then:
        assert actual.toString().contains("https://api.github.com")
    }

    def "should return an organization overview when requested"() {
        given:
        Object fakeResult = '''
            {
                "login": "Netflix",
                "id": 913567,
                "email": "netflixoss@netflix.com",
                "type": "Organization"
            }
        '''

        when:
        metricsService.refreshAllData()

        then:
        1 * cachingGitHubClient.getOrganization(NETFLIX) >> fakeResult

        when:
        Object actual = metricsCachingClient.getOrganization(NETFLIX)

        then:
        assert actual.toString().contains("netflixoss@netflix.com")
    }

    def "should return a flat list of members when requested"() {
        given:
        Object fakeResult = '''
            [{
                "login": "person",
                "id": 42,
                "node_id": "fakeid"
            }]
        '''

        when:
        metricsService.refreshAllData()

        then:
        1 * cachingGitHubClient.getOrganizationMembers(NETFLIX) >> fakeResult

        when:
        Object actual = metricsCachingClient.getOrganizationMembers(NETFLIX)

        then:
        assert actual.toString().contains("fakeid")
    }

    def "should return a flat list of paginated repos when requested"() {
        given:
        String firstPage = '''
            [  
             {
                "id": 9533057,
                "full_name": "Netflix/brutal",
                "fork": false,
                "url": "https://api.github.com/repos/Netflix/brutal",
                "stargazers_count": 194,
                "forks": 42,
                "open_issues": 10
             }
            ]
        '''

        String secondPage = '''
            [  
             {
                "id": 9533057,
                "full_name": "Netflix/gruesome",
                "fork": false,
                "url": "https://api.github.com/repos/Netflix/brutal",
                "stargazers_count": 194,
                "forks": 41,
                "open_issues": 10
             }
            ]
        '''

        when:
        metricsService.refreshAllData()

        then:
        1 * cachingGitHubClient.getRepositoryView(NETFLIX, 1) >> createResponse(firstPage, true)
        1 * cachingGitHubClient.getRepositoryView(NETFLIX, 2) >> createResponse(secondPage, false)

        when:
        Object actual = metricsCachingClient.getOrganizationRepos(NETFLIX)

        then:
        assert actual.toString().contains("brutal")
        assert actual.toString().contains("gruesome")
    }

    Response createResponse(String response, boolean hasNext) {
        String linkHeader = hasNext ? linksWithNext : linksNoNext
        Response.builder()
                .status(200)
                .request(Request.create(Request.HttpMethod.GET, "url", [:], null as byte[], null))
                .headers(["Link": [linkHeader]])
                .body(response.bytes)
                .build()
    }

    private String linksNoNext = "<https://api.github.com/organizations/913567/members?page=1&per_page=100>; rel=\"prev\", " +
            "<https://api.github.com/organizations/913567/members?page=1&per_page=100>; rel=\"last\", " +
            "<https://api.github.com/organizations/913567/members?page=1&per_page=100>; rel=\"first\""

    private String linksWithNext = "<https://api.github.com/organizations/913567/repos?page=2&per_page=100>; rel=\"next\", " +
            "<https://api.github.com/organizations/913567/repos?page=2&per_page=100>; rel=\"last\""

    def "should return a list of top N repositories by number of forks"() {
        given:
        int numberRepos = 5
        List<GHRepository> expectedList = buildRepositoryList(10)
        GHOrganization mockOrg = Mock(GHOrganization)
        PagedIterable<GHRepository> mockPage = Mock(PagedIterable)

        when:
        metricsService.refreshAllData()

        then:
        1 * gitHubClient.getOrganization(NETFLIX) >> mockOrg
        1 * mockOrg.listRepositories() >> mockPage
        1 * mockPage.toList() >> expectedList

        when:
        List<List<Object>> actualList = metricsCachingClient.getTopRepositoriesByForkCount(numberRepos)

        then:
        assert actualList.size() == numberRepos
    }

    def "should return an empty list if result is not available"() {
        given:
        int numberRepos = 5

        when:
        metricsService.refreshAllData()

        then:
        1 * gitHubClient.getOrganization(NETFLIX) >> { throw new IOException() }

        when:
        List<List<Object>> actualList = metricsCachingClient.getTopRepositoriesByForkCount(numberRepos)

        then:
        assert actualList.isEmpty()
    }

    def "should return all of the repos if that number is less than number requested"() {
        given:
        int numberRepos = 5
        List<GHRepository> expectedList = buildRepositoryList(3)
        GHOrganization mockOrg = Mock(GHOrganization)
        PagedIterable<GHRepository> mockPage = Mock(PagedIterable)

        when:
        metricsService.refreshAllData()

        then:
        1 * gitHubClient.getOrganization(NETFLIX) >> mockOrg
        1 * mockOrg.listRepositories() >> mockPage
        1 * mockPage.toList() >> expectedList

        when:
        List<List<Object>> actualList = metricsCachingClient.getTopRepositoriesByForkCount(numberRepos)

        then:
        assert actualList.size() == 3
    }

    def "should return a list of top N repositories by last updated time"() {
        given:
        int numberRepos = 5
        List<GHRepository> expectedList = buildRepositoryList(10)
        GHOrganization mockOrg = Mock(GHOrganization)
        PagedIterable<GHRepository> mockPage = Mock(PagedIterable)

        when:
        metricsService.refreshAllData()

        then:
        1 * gitHubClient.getOrganization(NETFLIX) >> mockOrg
        1 * mockOrg.listRepositories() >> mockPage
        1 * mockPage.toList() >> expectedList

        when:
        List<List<Object>> actualList = metricsCachingClient.getTopRepositoriesByLastUpdated(numberRepos)

        then:
        assert actualList.size() == numberRepos
    }

    def "should return a list of top N repositories by number of open issues"() {
        given:
        int numberRepos = 5
        List<GHRepository> expectedList = buildRepositoryList(10)
        GHOrganization mockOrg = Mock(GHOrganization)
        PagedIterable<GHRepository> mockPage = Mock(PagedIterable)

        when:
        metricsService.refreshAllData()

        then:
        1 * gitHubClient.getOrganization(NETFLIX) >> mockOrg
        1 * mockOrg.listRepositories() >> mockPage
        1 * mockPage.toList() >> expectedList

        when:
        List<List<Object>> actualList = metricsCachingClient.getTopRepositoriesByOpenIssueCount(numberRepos)

        then:
        assert actualList.size() == numberRepos
    }

    def "should return a list of top N repositories by number of stars"() {
        given:
        int numberRepos = 5
        List<GHRepository> expectedList = buildRepositoryList(10)
        GHOrganization mockOrg = Mock(GHOrganization)
        PagedIterable<GHRepository> mockPage = Mock(PagedIterable)

        when:
        metricsService.refreshAllData()

        then:
        1 * gitHubClient.getOrganization(NETFLIX) >> mockOrg
        1 * mockOrg.listRepositories() >> mockPage
        1 * mockPage.toList() >> expectedList

        when:
        List<List<Object>> actualList = metricsCachingClient.getTopRepositoriesByStarCount(numberRepos)

        then:
        assert actualList.size() == numberRepos
    }

    /**
     * Duplicated because mocks cannot be updated in traits
     */
    List<GHRepository> buildRepositoryList(int numRepos) {
        List<GHRepository> expectedList = []

        numRepos.times {
            GHRepository repo = Mock(GHRepository)
            repo.getFullName() >> "repo-number-" + it
            repo.getForksCount() >> it
            repo.getUpdatedAt() >> new Date()
            repo.getOpenIssueCount() >> it
            repo.getStargazersCount() >> it
            expectedList.add(repo)
        }
        expectedList
    }

}