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
    private Long count;

    @Override
    public int compareTo(MetricTuple o) {
        if (o instanceof EmptyResult) {
            return 1;
        }
        return (int)(count - o.count);
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
