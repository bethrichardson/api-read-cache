package com.netflix.apireadcache.metrics.github;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

import static com.netflix.apireadcache.client.ResourcePaths.*;

@Headers({
        "Content-Type: application/json",
        "Accept: application/json",
})
public interface ProxiedGitHubClient {

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @RequestLine("GET ")
    Object getOverview();

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @RequestLine("GET " + ORGS + "/{organizationName}")
    Object getOrganization(@Param("organizationName") String organizationName);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @RequestLine("GET " + ORGS + "/{organizationName}" + MEMBERS)
    Object getOrganizationMembers(@Param("organizationName") String organizationName);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @RequestLine("GET " + ORGS + "/{organizationName}" + REPOS)
    Object getRepositoryView(@Param("organizationName") String organizationName);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @RequestLine("GET " + "{path}")
    Object getUnhandledRoute(@Param("path") String path);

}