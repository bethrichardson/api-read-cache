# Testing Strategy

## Context and Problem Statement
There are a few levels of testing that needed to happen as part of this project. I needed to ensure that my service
responded to requests with the expected content based on GitHub API responses. I also needed to validate that my cache
was refreshing data according to the CachingStrategy configuration.
Part of the challenge for testing with this project is that the data from GitHub may change shape on a regular basis
and any current values are quickly invalidated.

## Considered Options

* [SpringBootTest](https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/test/context/SpringBootTest.html) - 
  tests that execute against the running SpringBoot application
* Detached Mock clients - Injecting mock beans for clients to external systems like GitHub
  
* Integration tests - Calling GitHub in my application with a simple http client and then comparing service response to API response
* [curl | jq provided recordings](/api-suite-fixed.sh) - Validating the results via bash script against a moment in time recording
  
* [Spock framework](http://spockframework.org/spock/docs/1.3/index.html) - Framework built upon JUnit runner for writing tests in groovy with very expressive
  language. Also supports very clear interaction-based testing and data driven testing.
* [JUnit5](https://junit.org/junit5/docs/current/user-guide/) - The defacto Java standard


## Decision Outcome

Chosen option: "SpringBootTest with Spock and detached mocks", because

* **_Recorded tests are flaky_**. 

  External API recordings do not have the need to change much as the code changes. However, they suffer from some short-comings.

  The provided recordings are a snapshot in time, and the values are wrong almost as soon as they are captured in the 
  repositories where changes are happening rapidly. Even if I were to record the results within a given test execution, the results could change
  while the test is running leading to a lot of false failures. 
  
  I literally watched a star count become invalidated within a minute 
  while I was "fixing" the test script included in this project. Acceptable ranges are better for counts that monotonically increase
  (take a snapshot at start of test and then assert the value returned from the service is greater than that value); 
  
* Loose coupling - Requiring the API to be available during test time also leads to coupling for the service to be unable to be verified for correctness if the API is offline.
I prefer to have tests able to execute in isolation in build and deploy contexts especially so that results are fairly easy to reproduce with controlled and known input.
  
* Easy to maintain - Dependency injection with detached mocks is a simple and easy to maintain way to inject mocks into test context without a lot of overhead compared
to creating and maintaining a set of external test scripts 
  
* Self-documenting - Spock test names are generally more expressive than JUnit method test names and given/when/then blocks provide clear structure to each test.
By using these kinds of tests we are able to communicate design decisions and requirements very clearly between engineers and others reviewing the code.
  
* Groovy tests - Allows for rapid development and expressive tests. Although allowing for very quick construction of test data and methods, it can also
directly interact with the Java code under test. It also is a good choice to have the tests in a language close to the language of the application under
  test, so they can easily be maintained along with the code.
  
* Support for data-driven testing - Spock would make this very easy. This seems like it would be an improvement
to current tests as they are fairly focused on more happy-path cases and basic metric ranges.
  
* TDD - I prefer to create red tests and then fix them to help me work quickly and keep myself from doing too much for my current goal. By using SpringBootTests, it's very
easy to kick off tests directly in my IDE as I am working and click through directly from the test code to the code under test. This also supports refactoring where the tests, 
  and the code both have to change together, without having to leave the context of the current code. Also, because my tests are very fast to create and run and easy to maintain
  I am more likely to test edge cases, exceptions, logging validation, etc that I wouldn't test with harder to maintain tests or external recording-based tests.

  
