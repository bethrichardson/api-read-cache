package com.netflix.repositories.domain.metrics

import com.spotify.github.v3.repos.ImmutableRepository
import com.spotify.github.v3.repos.Repository

trait MetricsTestingSupport {

    List<Repository> buildRepositoryList(int numRepos) {
        List<Repository> expectedList = []
        numRepos.times {
            expectedList.add(ImmutableRepository
                    .builder()
                    .forksCount(it)
                    .build())
        }
        expectedList
    }

}