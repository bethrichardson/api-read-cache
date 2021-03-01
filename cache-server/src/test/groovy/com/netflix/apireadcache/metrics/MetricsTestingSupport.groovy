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
                    .fullName("repo-number-" + it)
                    .forksCount(it)
                    .updatedAt(GitHubInstant.create(Instant.now()))
                    .openIssuesCount(it)
                    .stargazersCount(it)
                    .build())
        }
        expectedList
    }

}