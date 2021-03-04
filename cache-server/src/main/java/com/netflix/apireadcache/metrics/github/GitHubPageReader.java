/**
 * Copyright 2021 Netflix, Inc.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.netflix.apireadcache.metrics.github;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Builder
@AllArgsConstructor
public class GitHubRepositoryPageReader {

    public static final int FIRST_PAGE = 1;
    public static final String LINK_HEADER = "Link";
    private final ProxiedGitHubClient client;
    private final ObjectMapper objectMapper;

    /**
     * Pulls all pages while the pages have a next Link header
     * @param organizationName the organization for which to fetch repos
     * @return A concatenated list of all repositories for the organization
     */
    public List<Object> getAllRepositoriesForAllPages(String organizationName) {
        List<Object> repositories = new ArrayList<>();
        int page = FIRST_PAGE;
        boolean hasNext = true;
        while (hasNext) {
            hasNext = addRepositoriesFromResponseForPage(organizationName, page, repositories);
            page++;
        }
        return repositories;
    }

    private boolean addRepositoriesFromResponseForPage(String organizationName, int page, List<Object> repositories) {
        List<Object> pageOfRepos;
        try (Response response = client.getRepositoryView(organizationName, page)) {
            if (response != null) {
                pageOfRepos = objectMapper.readValue(response.body().toString(), List.class);
                repositories.addAll(pageOfRepos);

            }
            return hasNext(response);
        } catch (JsonProcessingException e) {
            log.warn("Failed to parse response for paged repositories", e);
        }
        return false;
    }

    /**
     * Is there a next page present in the link header?
     * Solution for parsing link headers based upon
     *
     * @link {https://github.com/hub4j/github-api/blob/master/src/main/java/org/kohsuke/github/GitHubPageIterator.java#L162}
     */
    private boolean hasNext(Response response) {
        if (response != null) {
            String link = response.headers().get(LINK_HEADER).iterator().next();
            if (link != null) {
                for (String token : link.split(", ")) {
                    if (token.endsWith("rel=\"next\"")) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
