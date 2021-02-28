# All View Data is Stored and Presented as a Long

## Context and Problem Statement
Repository metrics are stored and returned as a list of MetricTuples, which are serialized as a list of lists. Each
list consists of a repository name and the value for that measurement. So for example, when requesting forks, a list including 
the value `["Netflix/Hystrix",4313]` is a metric tuple indicating that the Netflix/Hystrix repository has 4313 forks.

There are 4 views in this project. 3 are count metrics (forks, stars, and open issues) and one is a timestamp (last updated).
I had to decide how to store and return these values consistently.

## Considered Options

* Long values - Using consistent metric types across different metrics converting timestamp to the nanoseconds for an instant
* Timestamp and integers - Using different types per metric type based on type (count or timestamp)


## Decision Outcome

Chosen option: "Consistent use of Long", because

* It simplified my API and data structures in a short term to use the same type throughout and allowed all sorting to be
done with one comparable object that uses the long value to sort.

* I wanted to make a decision that was simple and met the current requirements, which is only that the values are sortable.
The instant as a long works to provide a timestamp, but probably it would be more expressive to store it as a timestamp.