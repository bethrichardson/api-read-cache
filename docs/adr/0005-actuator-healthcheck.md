# Healthcheck with Actuator

## Context and Problem Statement
The service must respond to health checks when it is online and ready for requests. I assumed that it would be preferable
to build the cache on startup prior to receiving requests. However, a different decision could be made to build the cache
at request time if the application has started, and the cache is not yet fully populated. Especially since this cache is
so inexpensive to rebuild. As it is, I am not exposing the healthcheck endpoint until the application is started and
has built the cache and started the threads that will maintain the cache over time.

## Considered Options

* [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-features.html) - 
  Exposes a basic health endpoint after the application is started
* Post-construct - I could implement my own service to check for readiness on startup, including checking that I have
retrieved cache values after startup


## Decision Outcome

Chosen option: "Spring Boot Actuator", because

* It is maintained by a large community and is the standard way to expose a health endpoint for my chosen framework
* I chose to include cache population in startup time, so the full Spring application will not be online until that process 
has completed. This makes this a very easy way to get the health endpoint. If the cache population was a longer running process,
  or I needed to wait until after application startup, I would still prefer to use the actuator way to implementing my own
  HealthIndicator to report when all these processes have completed.
  
* It is very simple to use and cheap and easy to maintain.