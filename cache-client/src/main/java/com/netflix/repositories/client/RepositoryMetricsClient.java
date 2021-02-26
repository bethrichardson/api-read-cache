package com.netflix.repositories.client;

import com.netflix.repositories.common.RepositoryMetric;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

import java.util.List;

import static com.netflix.repositories.client.ResourcePaths.FORKS;
import static com.netflix.repositories.client.ResourcePaths.VIEW;


@Headers({
        "Content-Type: application/json",
        "Accept: application/json",
})
public interface RepositoryMetricsClient {

    @RequestLine("GET " + VIEW + "/{numResults}" + FORKS)
    List<List<RepositoryMetric>> getTopRepositoriesByForks(@Param("numResults") Integer numResults);

}