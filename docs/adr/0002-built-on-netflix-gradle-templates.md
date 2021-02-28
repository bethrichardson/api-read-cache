# Gradle Build

## Context and Problem Statement
I needed a build mechanism for pulling in dependencies, building jars, running tests, etc. 

## Considered Options

* [gradle-templates](https://github.com/Netflix/gradle-template) Provided as a base repo for NetflixOSS projects, including some out of the box plugins
* Gradle self-built - I could have started from scratch and just collected the plugin and configuration I wanted


## Decision Outcome

Chosen option: "gradle-templates", because

* I thought it would be interesting to work within the checkstyles and PMD rules that are used for at least some Netflix projects
* It got me started quickly
* I was still able to customize the build quite a bit by updating some of the rules and adding additional plugins