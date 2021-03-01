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
package com.netflix.apireadcache.metrics;

import com.netflix.apireadcache.client.ResourcePaths;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.netflix.apireadcache.client.ResourcePaths.NETFLIX;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping(path = "/", produces = APPLICATION_JSON_VALUE)
public class MetricsResource {

    @Autowired
    private MetricsService metricsService;

    @GetMapping
    public Object overview() {
        return metricsService.getOverview();
    }

    @GetMapping(path = ResourcePaths.ORGS + NETFLIX)
    public Object organization() {
        return metricsService.getOrganization();
    }

    @SneakyThrows
    @GetMapping(path = ResourcePaths.ORGS + NETFLIX + ResourcePaths.REPOS)
    public Object repositories() {
        return metricsService.getRepositories();
    }

    @GetMapping(path = ResourcePaths.ORGS + NETFLIX + ResourcePaths.MEMBERS)
    public Object members() {
        return metricsService.getMembers();
    }

    @GetMapping(ResourcePaths.VIEW + "/{numRepositories}" + ResourcePaths.FORKS)
    public List<List<Object>> forks(@PathVariable("numRepositories") Integer numRepositories) {
        return MetricTuple.getAsTuples(metricsService.getTopMetricsByForkCount(numRepositories));
    }

    @GetMapping(ResourcePaths.VIEW + "/{numRepositories}" + ResourcePaths.LAST_UPDATED)
    public List<List<Object>> lastUpdated(@PathVariable("numRepositories") Integer numRepositories) {
        return MetricTuple.getAsTuples(metricsService.getTopMetricsByLastUpdated(numRepositories));
    }

    @GetMapping(ResourcePaths.VIEW + "/{numRepositories}" + ResourcePaths.OPEN_ISSUES)
    public List<List<Object>> openIssues(@PathVariable("numRepositories") Integer numRepositories) {
        return MetricTuple.getAsTuples(metricsService.getMetricsByOpenIssues(numRepositories));
    }

    @GetMapping(ResourcePaths.VIEW + "/{numRepositories}" + ResourcePaths.STARS)
    public List<List<Object>> stars(@PathVariable("numRepositories") Integer numRepositories) {
        return MetricTuple.getAsTuples(metricsService.getTopMetricsByStars(numRepositories));
    }

    @RequestMapping(value="**",method = RequestMethod.GET)
    public Object proxyOtherRequests(final HttpServletRequest request){
        String path = request.getRequestURI();
        return metricsService.getProxiedResponse(path);
    }

}