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
public interface MetricsCachingClient {

    @RequestLine("GET ")
    Object getOverview();

    @RequestLine("GET " + "{path}")
    Object getApiEndpoint(@Param("path") String path);

    @RequestLine("GET " + ORGS + "/{organizationName}")
    Object getOrganization(@Param("organizationName") String organizationName);

    @RequestLine("GET " + ORGS + "/{organizationName}" + MEMBERS)
    Object getOrganizationMembers(@Param("organizationName") String organizationName);

    @RequestLine("GET " + ORGS + "/{organizationName}" + REPOS)
    Object getOrganizationRepos(@Param("organizationName") String organizationName);

    @RequestLine("GET " + VIEW + "/{numResults}" + FORKS)
    List<List<Object>> getTopRepositoriesByForks(@Param("numResults") Integer numResults);

    @RequestLine("GET " + VIEW + "/{numResults}" + LAST_UPDATED)
    List<List<Object>> getTopRepositoriesByLastUpdated(@Param("numResults") Integer numResults);

    @RequestLine("GET " + VIEW + "/{numResults}" + OPEN_ISSUES)
    List<List<Object>> getTopRepositoriesByOpenIssueCount(@Param("numResults") Integer numResults);

}