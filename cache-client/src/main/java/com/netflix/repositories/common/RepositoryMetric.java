package com.netflix.repositories.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RepositoryMetric implements Comparable<RepositoryMetric> {

    private String name;
    private Integer count;

    @Override
    public int compareTo(RepositoryMetric o) {
        return count - o.count;
    }
}
