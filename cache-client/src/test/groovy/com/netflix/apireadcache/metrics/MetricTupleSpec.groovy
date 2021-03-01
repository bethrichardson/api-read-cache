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

import spock.lang.Specification

class MetricTupleSpec extends Specification {

    def "should sort two tuples by count"() {
        when:
        MetricTuple smaller = MetricTuple.builder().count(-1).build()
        MetricTuple larger = MetricTuple.builder().count(5).build()

        then:
        assert smaller < larger

        and:
        MetricTuple same = MetricTuple.builder().count(-1).build()

        then:
        assert smaller == same
    }

    def "should always sort empty results to the bottom"() {
        when:
        MetricTuple emptyResult = MetricTuple.emptyResult()
        MetricTuple other = MetricTuple.builder().count(Integer.MIN_VALUE).build()

        then:
        assert other > emptyResult

        and:
        assert emptyResult < other

        when:
        MetricTuple same = MetricTuple.emptyResult()

        then:
        assert emptyResult == same
    }

    def "should build an empty result if passed a null count"() {
        when:
        MetricTuple metric = MetricTuple.builder().build()

        then:
        assert metric instanceof MetricTuple.EmptyResult
    }
}
