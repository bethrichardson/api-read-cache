package com.netflix.repositories.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetricTuple implements Comparable<MetricTuple> {

    private String name;
    private Long count;

    @Override
    public int compareTo(MetricTuple o) {
        return (int)(count - o.count); // TODO: handle nulls and test that empty results are sorted last
    }

    public List<Object> getAsTuple() {
        List<Object> tuple = new ArrayList<>();
        tuple.add(name);
        tuple.add(count);
        return tuple;
    }

    public static MetricTuple emptyResult() {
        return new EmptyResult();
    }

    public static class EmptyResult extends MetricTuple {
    }

}
