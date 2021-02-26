package com.netflix.repositories.domain;

import com.netflix.repositories.client.ResourcePaths;
import com.netflix.repositories.common.RepositoryMetric;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = ResourcePaths.VIEW, produces = APPLICATION_JSON_VALUE)
public class RepositoryMetricsResource {

    @GetMapping("/{numRepositories}" + ResourcePaths.FORKS)
    public List<List<RepositoryMetric>> forks(@PathVariable("numRepositories") Integer numRepositories) {
        return new ArrayList<>();
    }

}