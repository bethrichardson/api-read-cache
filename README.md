This project intends to provide view for a set of repository metrics for a selected organization (Netflix) using the
github API.

It is currently a work in progress and does not provide any functionality.

## Building

This project is built using gradle 6. It was built using
the [Netflix/gradle-template](https://github.com/Netflix/gradle-template) project.

To execute a build, run the following command:
`./gradlew build`

To view all available gradle tasks, run the following command:
`./gradlew tasks`

## Running

To start the application locally, run the following command:
`./gradlew bootRun`

## Testing

Component tests are included that will execute tests against the running application.

To run all the tests, run the following command:
`./gradlew check`

## API Reference

| Endpoint         | Description                   |
| ---------------- | ----------------------------- |
|/view/top/N/forks | Top-N repos by number of forks|


