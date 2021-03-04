# Interacting with GitHub API

## Context and Problem Statement
The Spotify client did not support paging and structured responses were required with paged responses to get all results.
The GitHub API includes a Links header with a next link when there are more pages of responses and by default only returns
30 items in a page unless more are requested. 

## Considered Options

* [Spotify GitHub Client](https://github.com/spotify/github-java-client) - Java client with pre-defined response objects,
  no paging
  
* [kohsuke](https://github-api.kohsuke.org/) - Java client with paging and predefined response objects
* [https://github.com/eclipse/egit-github/tree/master/org.eclipse.egit.github.core](https://github.com/eclipse/egit-github/tree/master/org.eclipse.egit.github.core) - 
  Another Java client


## Decision Outcome

Chosen option: "kohsuke", because

* Spotify did not appear to support paging
* The eclipse client didn't immediately appear to support paging
  
* The kohsuke library supported paging fairly easily, removing the iteration from my code when using the client
* I chose these options because they were the clients suggested by GitHub API documentation and the kohsuke library appears 
  to be maintained and has a lot of contributors

