# Streaming Query Engine

SQE is a query engine written using Storm's Trident framework that takes a set of SQL-like commands, including queries, and runs them against one or more input streams. By using Trident, input streams are processed in micro-batches with both good latency and high throughput while guaranteeing "exactly-once" processing. Results can be returned through a list of output streams or SQE can handle persisting to a data store directly using one of its supported Trident states. SQE is designed to make it easy to query against large streams of data with good performance for many different use cases.

* [Using SQE](storm-sqe-using.html)
* [Options](storm-sqe-options.html)
* [Commands](storm-sqe-commands.html)
* [Expressions](storm-sqe-expressions.html)
* [States](storm-sqe-states.html)
* [Streams](storm-sqe-streams.html)
* [Replay Filtering](storm-sqe-replay-filtering.html)

## Potential Future Features

* More expressions - String expressions, Average, etc.
* Query planning and optimization
* Better state/stream support and optimization
* More options
* Support for joins
* Support for sub-queries, in-line queries and query chaining
* Split out streams, states and other hard coded factories to configuration files. Factories should use the appropriate file to create appropriate objects. Then users can add additional expressions, states, etc. by building a jar with SQE as a dependency, then including their own versions of the configuration files with their new expressions, states, etc. (Note: we do this for expressions now)

## Links

* [Trident State](http://storm.apache.org/documentation/Trident-state.html) - Very useful for understanding how state and persisting data works in Trident
* [Trident API Overview](http://storm.apache.org/documentation/Trident-API-Overview.html) - Basic overview of Trident. Not necessary to know to use SQE, but still helpful.
* [Squall](https://github.com/epfldata/squall) - "A streaming / online query processing / analytics engine based on Apache Storm" - Another project from EPFL Data that does complex SQL like queries on top of Storm. Definitely something to keep an eye on, though use cases may be different. Doesn't support "exactly-once" processing yet, something we do by using Trident.

