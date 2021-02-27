package com.netflix.repositories.client;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

import java.util.List;

import static com.netflix.repositories.client.ResourcePaths.*;


@Headers({
        "Content-Type: application/json",
        "Accept: application/json",
})
public interface RepositoryMetricsClient {

    @RequestLine("GET " + VIEW + "/{numResults}" + FORKS)
    List<List<Object>> getTopRepositoriesByForks(@Param("numResults") Integer numResults);

    @Headers({"Content-Type: text/plain", "Accept: text/plain"})
    @RequestLine("GET " + ORGS + "/{organizationName}" + REPOS)
    String getOrganizationRepos(@Param("organizationName") String organizationName);


    @Headers({"Content-Type: text/plain", "Accept: text/plain"})
    @RequestLine("GET " + ORGS + "/{organizationName}" + MEMBERS)
    String getOrganizationMembers(@Param("organizationName") String organizationName);

}