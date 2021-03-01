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
package com.netflix.apireadcache.metrics.proxied;

import com.netflix.apireadcache.metrics.MetricCollector;
import com.netflix.apireadcache.metrics.caching.CachingStrategy;
import com.netflix.apireadcache.metrics.caching.MetricsCache;

/**
 * Stores unstructured data from GitHub API to support a cached proxy. Does not allow for views on the data as
 * the data is stored as it is received from GitHub as a plain Object.
 *
 * Compare with RepositoryMetricCache, which has a set of supported views and stores a structured data objet from
 * the GitHub API for GitHub repositories.
 * @see com.netflix.apireadcache.metrics.repositories.RepositoryMetricCache
 */
public class ProxiedMetricCache extends MetricsCache<Object> {

    public ProxiedMetricCache(MetricCollector<Object> metricCollector, CachingStrategy cachingStrategy) {
        super(metricCollector, cachingStrategy);
    }

}
