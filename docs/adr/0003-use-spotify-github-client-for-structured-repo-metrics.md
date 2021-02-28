# Interacting with GitHub Client

## Context and Problem Statement
I needed to either find or create a client to interact with the GitHub API in my service. I needed a way to get structured
results from the repos endpoint to provide the data for the views. However, I also needed to exactly proxy the results when
a consumer hits one of my endpoints.

## Considered Options

* [Spotify GitHub Client](https://github.com/spotify/github-java-client) - Java client with pre-defined response objects
* [Swagger Codegen](https://swagger.io/tools/swagger-codegen/) - generated clients from Swagger docs
* [Feign Client](https://github.com/OpenFeign/feign) - Hand-rolling a GitHub API client


## Decision Outcome

Chosen option: "Spotify and Feign", because

* The GitHub API returns some very large API objects for some endpoints that I was hitting. So creating a set of defined
  Java objects for each of these did not seem appealing or fit with the request to Keep It Simple.
  
* I needed structured responses for the repos endpoint for the metrics for which I am providing views. So I used this
client to retrieve the metrics that back the views for repository data (top N forks, etc). However, because it transformed the
  data from the original shape of the object, I couldn't use it for returning the cached repository responses. Additionally,
  it did not have any support for members and the other endpoints requested.
  
* For the cached responses that are cached as plain Objects (thereby caching the proxied response from GitHub API without mutating it),
I used a Feign client to GitHub with an Object response type and added paths for those that needed caching to be called but the 
  cache reloader. I also added a passthrough for all other endpoints.

* I am completely uncoupled to the structure of the API responses for the majority of my caching strategy 
* Less work to get started
* Generated code is not great to look at and can add to testing complexity and maintenance burden.
