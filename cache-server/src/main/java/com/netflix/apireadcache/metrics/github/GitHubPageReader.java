/**
 * Copyright 2021 Netflix, Inc.
 * <p>
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Slf4j
@Builder
@AllArgsConstructor
public class GitHubPageReader {

    public static final int FIRST_PAGE = 1;
    public static final String LINK_HEADER = "Link";
    public static final String LINK_SEPARATOR = ", ";
    public static final String NEXT_LINK = "rel=\"next\"";
    private final ProxiedGitHubClient client;
    private final ObjectMapper objectMapper;

    /**
     * Pulls all pages while the pages have a next Link header
     * @param pagedResponse Function to retrieve a new page of entities for a given page number
     * @return A concatenated list of all entities
     */
    public List<Object> getAll(Function<Integer, Response> pagedResponse) {
        List<Object> repositories = new ArrayList<>();
        int page = FIRST_PAGE;
        boolean hasNext = true;
        while (hasNext) {
            hasNext = addEntitiesFromResponseForPage(pagedResponse, page, repositories);
            page++;
        }
        return repositories;
    }

    private boolean addEntitiesFromResponseForPage(Function<Integer, Response> pagedResponse, int page, List<Object> repositories) {
        List<Object> pageOfRepos;
        try (Response response = pagedResponse.apply(page)) {
            if (response != null) {
                pageOfRepos = objectMapper.readValue(response.body().toString(), List.class);
                repositories.addAll(pageOfRepos);
            }
            return hasNext(response);
        } catch (JsonProcessingException e) {
            log.warn("Failed to parse response for paged read", e);
        }
        return false;
    }

    /**
     * Checks for a next link in the Links header
     * Solution for parsing link headers based upon
     * @link {https://github.com/hub4j/github-api/blob/master/src/main/java/org/kohsuke/github/GitHubPageIterator.java#L162}
     */
    private boolean hasNext(Response response) {
        if (response != null) {
            String link = response.headers().get(LINK_HEADER).iterator().next();
            if (link != null) {
                for (String token : link.split(LINK_SEPARATOR)) {
                    if (token.endsWith(NEXT_LINK)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
