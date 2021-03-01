/**
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
package com.netflix.apireadcache.metrics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetricTuple implements Comparable<MetricTuple> {

    private String name;
    private Comparable count;

    @Override
    public int compareTo(MetricTuple o) {
        if (o instanceof EmptyResult) {
            return 1;
        }
        return this.count.compareTo(o.getCount());
    }

    public List<Object> getAsTuple() {
        List<Object> tuple = new ArrayList<>();
        tuple.add(name);
        tuple.add(count);
        return tuple;
    }

    public static List<List<Object>> getAsTuples(List<MetricTuple> list) {
        return list.stream().map(MetricTuple::getAsTuple)
                .collect(Collectors.toList());
    }

    public static MetricTupleBuilder builder() {
        return new ValueEnsuringBuilder();
    }

    /**
     * If count is set to null, create an EmptyResult
     */
    private static class ValueEnsuringBuilder extends MetricTupleBuilder {
        @Override
        public MetricTuple build() {
            if (super.count == null) {
                return emptyResult();
            }
            return super.build();
        }
    }

    public static class MetricTupleBuilder {}

    public boolean isValid() {
        return name != null && count != null;
    }

    public static MetricTuple emptyResult() {
        return new EmptyResult();
    }

    public static class EmptyResult extends MetricTuple {
        @Override
        public int compareTo(MetricTuple o) {
            if (o instanceof EmptyResult) {
                return 0;
            } else {
                return -1;
            }
        }
    }



}
