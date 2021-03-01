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