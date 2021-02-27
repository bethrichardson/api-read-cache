package com.netflix.repositories.domain.metrics.github;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

import static com.netflix.repositories.client.ResourcePaths.MEMBERS;
import static com.netflix.repositories.client.ResourcePaths.ORGS;

@Headers({
        "Content-Type: application/json",
        "Accept: application/json",
})
public interface CachingGitHubClient {

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
    @RequestLine("GET " + "{path}")
    Object getUnhandledRoute(@Param("path") String path);

}