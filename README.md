# API Read Cache

A GitHub API read caching service for Netflix organization data. The API Read Cache service caches selected GitHub API
endpoints periodically to avoid overloading the GitHub API with requests. The service proxies all GitHub API endpoints
outside this set to GitHub.

## Overview of Design Decisions

The following design decisions are documented as part of this project:
- [Using an in-memory cache](docs/adr/0000-use-in-memory-cache.md)
- [Using Spring Boot and Java](docs/adr/0001-springboot.md)
- [Using the Netflix OSS Gradle template](docs/adr/0002-built-on-netflix-gradle-templates.md)
- [Using Spotify GitHub Client and a Feign Client to interact with GitHub](docs/adr/0003-use-spotify-github-client-for-structured-repo-metrics.md)
- [Testing with SpringBootTest and Spock](docs/adr/0004-testing-strategy.md)
- [Healthcheck with Spring Boot Actuator](docs/adr/0005-actuator-healthcheck.md)
- [Using New GitHub Client for Paging](docs/adr/0006-use-kohsuke-github-client-for-paging.md)

## Concepts
### Metric
The data that the service retrieves and caches from the GitHub API is referred to as "metrics" throughout this project. They are
a snapshot of a set of repository, organization, or other details taken at a moment in time. A lot of the details in the 
cached data could be seen as configuration (for example, "full name" or access URLs); however, because they are being used
as a source for reporting views, and generally providing a measurement of the organization's GitHub usage or usage for a given
repository, all of this data is referred to as metrics.

### View
The repository metrics are presented as a set of views that are sorted as presenting the Top N of a set of values of a given type.
Repository metrics are stored and returned as a list of MetricTuples, which are serialized as a list of lists. Each
list consists of a repository name and the value for that measurement. So for example, when requesting forks, a list including
the value `["Netflix/Hystrix",4313]` is a metric tuple indicating that the Netflix/Hystrix repository has 4313 forks.

See the Metric View Endpoint reference below for a full overview of all views that the service provides.

### Cache
The service stores a single snapshot for each metric and updates the metric value according to the refresh frequency defined
in the caching strategy. The entity that stores the metrics is a cache. Each type of metric has a distinct cache, but all caches 
share a common thread pool for updating the cache over time.

A cache can have a set of supported views for the data represented within that cache. When the value for the metric is refreshed,
the view data is pre-aggregated and stored at the same time in the same cache.

The ProxiedMetricCache is a simple type of cache for data that has no pre-determined schema.
This cache type does not support views, but provides cached storage for a piece of data from the 
GitHub API, without requiring knowledge of the shape of that data.

## API Reference

### Caching APIs

The service collects metrics according to the configured refresh frequency minutes (default 5 minutes) from the GitHub API and provides cached metrics at the following URLs:

| Endpoint               | Description                                   |
| ---------------------- | --------------------------------------------- |
|`/`                     | Root node data for the github API             |
|`/orgs/Netflix`         | Overview of data for the Netflix organization |
|`/orgs/Netflix/repos`   | List of repositories for Netflix organization |
|`/orgs/Netflix/members` | List of members for Netflix organization      |

The service proxies all other paths directly to the GitHub API without caching values.

### Metric View Endpoints

The service provides a set of views for the latest repository metrics that can be accessed at the following URLs:

| Endpoint                  | Description                                    |
| ------------------------- | ---------------------------------------------- |
|`/view/top/N/forks`        | Top-N repos by number of forks                 |
|`/view/top/N/last_updated` | Top-N repos by updated time (most recent first)|
|`/view/top/N/open_issues ` | Top-N repos by number of open issues           |
`/view/top/N/stars`         | Top-N repos by number of stars                 |

### Healthcheck

The application will respond on `/healthcheck` with a 200 status when it is ready to receive requests.

## Building

This project is built using gradle 6. It was originally built using
the [Netflix/gradle-template](https://github.com/Netflix/gradle-template) project.

To execute a build, run the following command:

```
./gradlew build
```

To view all available gradle tasks, run the following command:

```
./gradlew tasks
```

## Running

To start the application locally:
Set the `GITHUB_API_TOKEN` environment variable containing your GitHub API token

```
export GITHUB_API_TOKEN=<your_token_here>
```

You can get a new Personal Access Token from your
[GitHub Developer Settings](https://docs.github.com/en/github/authenticating-to-github/creating-a-personal-access-token)
.

To start the application, run the following command, supplying the port on which you want to accept requests:

```
./gradlew bootRun --args='--server.port=<port_number>'
```

**Note**: To use the default 8080 port, leave off the args after `bootRun`

### Configuration Options

You can configure the following options by either passing them to the bootRun process or by setting them using the usage
format in the following table in the
`application.properties` file.

| Option                    | Description                                                                           | Usage                         |
| ------------------------- | ------------------------------------------------------------------------------------- | ----------------------------- |
| `github.api.url`          | The URL for the GitHub API. Default is https://api.github.com                         | `github.api.url=<api_url>`    |
| `server.port`             | Configure the port used by the running application. Default is 8080                   | `server.port=<port_number>`   |
| `cache.refresh.frequency` | New metrics are collected at this frequency. In Duration format. Default is 5 minutes.| `cache.refresh.frequency=PT5M`|
| `cache.refresh.threads`   | The number of threads used to refresh the cache. Default is 2 threads.                | `cache.refresh.threads=2`     |

## Testing

There is a set of component tests that execute tests against the running application using the provided Java client. 
The service is configured to start up on localhost at port 10000 for tests.  
The config for the test client is in application-test.properties as `server.port` and `server.url`.

To run all the tests, run the following command:

```
./gradlew check
```

Alternatively, to run the provided curl/jq specs, execute one of the following commands while running the application
locally.

```
sh ./api-suite.sh <port_number>
sh ./api-suite-fixed.sh <port_number> 
```

Note that these specs use static values that change over time and thus have unreliable results. The "fixed" results has
metrics recorded at the time of its creation.
