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
package com.netflix.apireadcache.metrics.repositories

import com.netflix.apireadcache.metrics.MetricTuple
import com.netflix.apireadcache.metrics.ViewType
import com.spotify.github.v3.repos.Repository
import spock.lang.Specification

class RepositoryMetricSpec extends Specification {

    def "should remove all empty results from a set of repository metrics"() {
        given:
        Repository repoWithNull = Mock(Repository)
        repoWithNull.forksCount() >> null
        repoWithNull.fullName() >> "Netflix/null-forks"

        Repository repoWithForks = Mock(Repository)
        repoWithForks.forksCount() >> 1500
        repoWithForks.fullName() >> "Netflix/plastic-fork"

        RepositoryMetric metricWithNullForks = new RepositoryMetric([repoWithForks, repoWithNull])

        when:
        Map<ViewType, List<MetricTuple>> views = metricWithNullForks.getViews()
        List<MetricTuple> forkData = views.get(ViewType.FORKS)

        then:
        assert forkData.size() == 1
        assert forkData.get(0).count == 1500
    }

    def "should remove all repositories with no names from a set of repository metrics"() {
        given:
        Repository repoWithNullName = Mock(Repository)
        repoWithNullName.forksCount() >> 5

        Repository repoWithName = Mock(Repository)
        repoWithName.forksCount() >> 55
        repoWithName.fullName() >> "Netflix/banana-monkey"

        RepositoryMetric metricWithNullForks = new RepositoryMetric([repoWithName, repoWithNullName])

        when:
        Map<ViewType, List<MetricTuple>> views = metricWithNullForks.getViews()
        List<MetricTuple> forkData = views.get(ViewType.FORKS)

        then:
        assert forkData.size() == 1
        assert forkData.get(0).name == "Netflix/banana-monkey"
    }
}
