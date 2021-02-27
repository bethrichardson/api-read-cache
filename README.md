This project provides views for a set of repository metrics for a selected organization (Netflix) using the
github API.

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
[GitHub Developer Settings](https://docs.github.com/en/github/authenticating-to-github/creating-a-personal-access-token).

To start the application, run the following command, supplying the port on which you
want to accept requests:
```
./gradlew bootRun --args='--server.port=<port_number>'
```
**Note**: To use the default 8080 port, leave off the args after `bootRun`

### Configuration Options
You can configure the following options by either passing them to the bootRun process
or by setting them using the usage format in the following table in the
`application.properties` file.

| Option           | Description                   | Usage              |
| ---------------- | -------------------------------------------------- | -------------------
|port              | Configure the port used by the running application. Default is 8080| `server.port=<port_number>` |
|organization (_Optional_)| Configure the root organization used for all metricTuples from GitHub. Default is Netflix| `github.organization=<org_name>` |


## Testing

Component tests are included that will execute tests against the running application.

To run all the tests, run the following command:
```
./gradlew check
```

## API Reference

### Caching APIs
The service collects metrics every 5 minutes from the GitHub API
and provides cached metrics at the following URLs:

| Endpoint             | Description                                   |
| -------------------- | --------------------------------------------- |
|/orgs/Netflix/repos   | List of repositories for Netflix organization |
|/orgs/Netflix/members | List of members for Netflix organization      |

### Metric View Endpoints
The service provides a set of views for the latest repository metrics 
that can be accessed at the following URLs:

| Endpoint         | Description                   |
| ---------------- | ----------------------------- |
|/view/top/N/forks | Top-N repos by number of forks|

### Healthcheck
The application will respond on `/healthcheck` with a 200 status when it is ready to receive requests.


