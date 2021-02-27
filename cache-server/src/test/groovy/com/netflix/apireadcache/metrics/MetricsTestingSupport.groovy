package com.netflix.apireadcache.metrics

import com.spotify.github.GitHubInstant
import com.spotify.github.v3.repos.ImmutableRepository
import com.spotify.github.v3.repos.Repository

import java.time.Instant

trait MetricsTestingSupport {

    List<Repository> buildRepositoryList(int numRepos) {
        List<Repository> expectedList = []
        numRepos.times {
            expectedList.add(ImmutableRepository
                    .builder()
                    .forksCount(it)
                    .updatedAt(GitHubInstant.create(Instant.now()))
                    .openIssuesCount(it)
                    .stargazersCount(it)
                    .build())
        }
        expectedList
    }

}