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
    private Integer count;

    @Override
    public int compareTo(MetricTuple o) {
        return count - o.count; // TODO: handle nulls
    }

    public List<Object> getAsTuple() {
        List<Object> tuple = new ArrayList<>();
        tuple.add(name);
        tuple.add(count);
        return tuple;
    }

}
