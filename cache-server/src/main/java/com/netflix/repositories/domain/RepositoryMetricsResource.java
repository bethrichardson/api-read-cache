package com.netflix.repositories.domain;

import com.netflix.repositories.client.ResourcePaths;
import com.netflix.repositories.common.RepositoryMetric;
import com.netflix.repositories.domain.github.GithubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@SuppressWarnings("PMD.BeanMembersShouldSerialize")
@RequestMapping(path = ResourcePaths.VIEW, produces = APPLICATION_JSON_VALUE)
public class RepositoryMetricsResource {

    @Autowired
    private GithubService githubService;

    @GetMapping("/{numRepositories}" + ResourcePaths.FORKS)
    public List<RepositoryMetric> forks(@PathVariable("numRepositories") Integer numRepositories) {
        return githubService.getRepositoryMetrics(numRepositories);
    }

}