# Use In Memory Cache

## Context and Problem Statement

As part of this project, the service must retrieve and cache a set of metrics and views. The cache values and connected views
must update at a set frequency and the cached values are retrieved when requested by a consumer.

The problem statement for the project mentions that we may be hitting this API under heavy load. 
The project requirements ask for a simple solution.

## Considered Options

* [Guava cache](https://github.com/google/guava/wiki/CachesExplained) - Better for data refreshed on demand rather than on a schedule
  and added complexity
* [Document Datastore](https://www.mongodb.com/document-databases) - Slower performance than in memory. Because this cache is
not the source of truth, and the cache can easily be rebuilt with a single set of API calls, there is no need for a durable cache. Also adds
  the need for a docker container for test and deploy, which adds complexity and cost.
  
* [Elasticsearch](https://www.elastic.co/elasticsearch/) - Could speed up on-the-fly aggregations or sorting for the metric views, but
  structured index and resources adds complexity. Current in-memory solution is sufficient for current requirements of single sort order.
* **In-memory cache** - Maintaining AtomicReference to metrics in MetricsCache using a ScheduledExecutorService to start threads according to
a CachingStrategy (number of threads and frequency of refresh)
  
## Decision Outcome

Chosen option: "In-memory cache", because

* Simple, cheap and easy to change. 
* Each metric requires only one very inexpensive call to GitHub to rebuild cache on application restart.
* Other options added complexity for testing and maintenance
* High performance