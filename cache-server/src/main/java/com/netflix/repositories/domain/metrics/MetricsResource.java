package com.netflix.repositories.domain.metrics;

import com.netflix.repositories.client.ResourcePaths;
import com.netflix.repositories.common.MetricTuple;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@SuppressWarnings("PMD.BeanMembersShouldSerialize")
@RequestMapping(path = "/", produces = APPLICATION_JSON_VALUE)
public class MetricsResource {

    @Autowired
    private MetricsService metricsService;


    @GetMapping
    public Object overview() {
        return metricsService.getOverview();
    }

    /**
     * Only returns data for Netflix repositories.
     */
    @GetMapping(path = ResourcePaths.ORGS + "/Netflix")
    public Object organization() {
        return metricsService.getOrganization();
    }

    /**
     * Only returns data for Netflix repositories.
     */
    @GetMapping(path = ResourcePaths.ORGS + "/Netflix" + ResourcePaths.REPOS, produces = MediaType.TEXT_PLAIN_VALUE)
    public String repositories() {
        return metricsService.getRepositories().toString();
    }

    /**
     * Only returns data for Netflix repositories.
     */
    @GetMapping(path = ResourcePaths.ORGS + "/Netflix" + ResourcePaths.MEMBERS)
    public Object members() {
        return metricsService.getMembers();
    }

    @GetMapping(ResourcePaths.VIEW + "/{numRepositories}" + ResourcePaths.FORKS)
    public List<List<Object>> forks(@PathVariable("numRepositories") Integer numRepositories) {
        return metricsService.getForkMetrics(numRepositories)
                .stream()
                .map(MetricTuple::getAsTuple)
                .collect(Collectors.toList());
    }

    @RequestMapping(value="**",method = RequestMethod.GET)
    public Object proxyOtherRequests(final HttpServletRequest request){
        String path = request.getRequestURI();
        log.info("Proxying unhandled route url=" + path);
        return metricsService.getProxiedResponse(path);
    }

}