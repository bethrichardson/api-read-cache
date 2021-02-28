package com.netflix.apireadcache.metrics.github;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
public class GithubCredentials {

    @Getter
    private final transient String apiToken;

    @Getter
    private final transient String apiUrl;

}
