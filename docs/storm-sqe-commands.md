# Commands

SQE currently supports the following commands:

* CreateStream
* Query
* Set

All commands are formatted as a JSON map with a single entry. The key is the name of the command and the value is a map formatted according to the command type. This command map can contain multiple fields, sections or clauses. Commands are typically loaded from one or more JSON files that are an array of commands:

    [
      {"Set": {...}},
      {"Set": {...}},
      {"CreateStream": {...}},
      {"Query": {...}},
      {"Query": {...}}
    ]

## CreateStream

CreateStream allows for creating input streams to the topology. For example, you can create a stream from a topic in a Kafka cluster. The JSON representation looks like:

    {
      "createStream": {
        "streamName": "<STREAM_NAME>",
        "objectName": "<OBJECT_NAME>",
        "spoutName": "<SPOUT_NAME>",
        "spoutType": "<SPOUT_TYPE>",
        "deserializer": "<DESERIALIZER>",
        "options": {
          "option1": value1,
          "optionN": valueN
        }
      }
    }

* streamName - This is the name of the stream used by SQE. Queries reference streams by using this name in the FROM clause.
* objectName - This is the name of the object to read from. For example, with Kafka, this would be the topic. Different spouts will handle this differently, but it's always a reference to some division of data from the data source.
* spoutName - This is the name of the spout to use. Currently, Kafka and Fixed are supported.
* spoutType (optional) - This determines the type of spout, which corresponds to the state types (NON_TRANSACTIONAL, TRANSACTIONAL, OPAQUE). Exactly once processing guarantees are determined by the combination of spout type and state type. Some spouts require this, while others do not. Not all types are supported by all spouts. (see Trident documentation for more information)
* deserializer (optional) - This takes the output of the spout and deserializes it. The only currently supported deserializer is Avro, which takes a byte array, converts it into an Avro record, then adds the required fields (based on supplied queries) to the tuple. If no deserializer is specified, then the tuples emitted by the spout are unchanged. For example, streams from a Kafka spout will typically need a deserializer since data is stored as a single message. Other spouts may return data split into different fields that can be queried against directly.
* options (optional) - This object allows you to provide command level spout options. It can include new options that affect how the spout emits data or overrides for global SQE options that are passed to the spout. For example, you can specify the hostname of the spout you are reading from.

### Example

    {
      "CreateStream": {
        "streamName": "big.query.data",
        "objectName": "big.query.data",
        "spoutName": "FIXED",
        "spoutType": "NON_TRANSACTIONAL",
        "options": {
          "jw.sqe.spout.fixed.fields": ["DateString", "AccountToken", "UserName", "HappyDanceCount"],
          "jw.sqe.spout.fixed.values": [
            ["2015-05-01 00:00", "Account1", "Joe", 1],
            ["2015-05-01 01:00", "Account1", "Bob", -1],
            ["2015-05-01 02:00", "Account1", "Susy", 1],
            ["2015-05-01 03:00", "Account1", "Mr. Fancy Pants", 1],
            ["2015-05-01 04:00", "Account1", "Joe", -1],
            ["2015-05-01 05:00", "Account1", "Bob", 1],
            ["2015-05-01 06:00", "Account1", "Susy", 1],
            ["2015-05-01 07:00", "Account1", "Mr. Fancy Pants", 1],
            ["2015-05-01 08:00", "Account2", "Joe", 1],
            ["2015-05-01 09:00", "Account2", "Bob", 1],
            ["2015-05-01 10:00", "Account2", "Susy", -1],
            ["2015-05-01 11:00", "Account2", "Mr. Fancy Pants", 1],
            ["2015-05-01 12:00", "Account2", "Joe", 1],
            ["2015-05-01 13:00", "Account2", "Bob", 1],
            ["2015-05-01 14:00", "Account2", "Susy", 1],
            ["2015-05-01 15:00", "Account2", "Mr. Fancy Pants", -1]
          ]
        }
      }
    }

## Set

Set adds or overwrites entries in the global config map. This can be used to set config options within a command file. The JSON representation looks lke:

    {"Set": {"key":"<KEY>","value":"<VALUE>"}}

### Example

    {"Set": {"key":"jw.sqe.state.redis.datatype","value":"HASH"}}

## Query

Queries allow you to run SQL like queries against an input stream and to persist/aggregate against a Trident state. The JSON representation of a query looks like:

    {
      "Query": {
        "insertInto":{
          "objectName":"<OBJECT_NAME>",
          "stateName": "<STATE_NAME>",
          "stateType": "<STATE_TYPE>",
          "fields":[<FIELD_LIST>],
          "options": {
            "option1": value1,
            "optionN": valueN
          }
        },
        "select":{"expressions":[<EXPRESSION_LIST>]},
        "from":{"objectName":"<OBJECT_NAME>"},
        "where":<PREDICATE_EXPRESSION>
      }
    }

### InsertInto

This clause tells the query engine what object (table, view, stream, etc.) the results are delivered to, what the fields are named, and if it needs to persist data to a certain state. Similar to SQL, the ordering of the fields in this clause lines up with the expressions in the Select clause. InsertInto contains the following fields:

* objectName - If SQE is sending the results to an output stream, then this is the name of the stream in the output stream map returned by build(). If a state is supplied (see below), then this is the name of table (or other object) the results are persisted to.
* stateName (optional) - If this field is supplied, then SQE will persist the results to a data store using this state. The name here must be a supported state, such as Redis. Otherwise, the results are streamed through the output stream supplied by build().
* stateType (optional) - This determines the type of state (NON_TRANSACTIONAL, TRANSACTIONAL, OPAQUE). It must be supplied if stateName is.
* fields - When persisting to a state, these fields are the names of the keys or columns. When using an output stream, they are the names of the fields in each tuple. The fields here are ordered and line up with the expressions in the Select clause.
* options (optional) - This object allows you to provide query level state options. It can include new options that affect how the state persists data or overrides for global SQE options that are passed to the state. For example, you can specify the hostname of the data store you are persisting data to.

### Select

This clause represents the selection of fields and transformations and aggregations on those fields from the input streams. The only field of the Select clause is a list of expressions. Expressions can come in one of three types:

* Field - Field expressions directly reference fields in the input streams and are represented as a string literal of the field name in JSON
* Constant - A constant value that can be a number, string, boolean, or null. The JSON representation looks like: `{"C":<CONSTANT>}`. Non-string constants can also be represented as just the literal. Internally, numerical constants are represented as either an Integer, Long or Double value.
* Function - Functions are expressions that represent processing of input data. There are two kinds of function expressions: transform and aggregation. There is also a special type of transform function called a predicate expression that always evaluates to true/false. Functions contain an argument list of expressions, which can include fields, constants and transform expressions. Aggregation expressions cannot be contained in another expression. Function expressions are represented in JSON as: `{"<FUNCTION_NAME>":[<EXPRESSION_LIST>]}`.

### From

The From clause tells SQE what input stream the query is referencing. The only field is "objectName." Typically, this is going to be the name of the Avro schema of the input data, but can include named input streams of tuples in the future.

### Where

The Where clause is an optional clause that allows filtering of data in the query by specifying which data should be kept. This clause accepts a single predicate expression, but you can chain together expressions using expressions like AND and OR, similar to other languages.

### No Group By?

SQE automatically determines which expressions are key fields and which expressions are aggregate/value fields. If a top level expression in the Select clause is a field, constant or transform expression, then it is a key field for the purposes of aggregations and persisting state. If a top level expression is an aggregation expression, then it is a value field.

### Example

This is an example of a query of top level device analytics by the minute. It includes sums for embeds, plays, completes. Additionally, it creates a HyperLogLog bitmap of all UserTokens for play events. The input stream is filtered to only include data has positive embeds, plays, or completes. It then uses the transactional RedisMapState to persist the results.

    {
      "Query": {
        "insertInto":{
          "objectName":"MinuteDeviceMeasures",
          "stateName": "REDIS",
          "stateType": "TRANSACTIONAL",
          "fields":["DateTime","Device","Embeds","Plays","Completes","TimeWatched","AdImpressions","HllViewers"]
        },
        "select":{
          "expressions":[
            {"FormatDate":[{"ParseDate":["DateGMTISO",{"C":"yyyy-MM-dd HH:mm:ss Z"}]},{"C":"yyyy-MM-dd'T'HH"}]}, "Device",
            {"Sum":["Embeds"]},{"Sum":["Plays"]},{"Sum":["Completes"]},{"Sum":["TimeWatched"]},{"Sum":["AdImpressions"]},
            {"CreateHll":[{"If": [{"GreaterThan":["Plays",0]}, "UserToken",null]}]}
          ]
        },
        "from":{"objectName":"com.jwplayer.analytics.avro.Ping"},
        "where":{"Or":[{"Or":[{">":["Embeds",0]},{">":["Plays",0]}]},{">":["Completes",0]}]}
      }
    }