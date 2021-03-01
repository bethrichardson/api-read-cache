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
import com.netflix.apireadcache.metrics.MetricType;
import com.netflix.apireadcache.metrics.ViewType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Supplier;

@AllArgsConstructor
public class ProxiedMetricCollector implements MetricCollector<Object> {

    private static final ViewType[] NO_SUPPORTED_VIEWS = {};
    @Getter
    private MetricType metricType;
    Supplier<ProxiedMetric> getMetricProvider;

    @Override
    public ProxiedMetric getMetric() {
        return getMetricProvider.get();
    }

    @Override
    public ViewType[] getSupportedViews() {
        return NO_SUPPORTED_VIEWS;
    }

}
