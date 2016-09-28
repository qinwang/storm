# Streams

Streams are ways of accessing data from data stores as streams of tuples. Some spouts emit data that may not be immediately useful to a SQE query. For example, Kafka writes tuples that contain a message field. These messages may be serialized data or otherwise contain complex information and objects you want to access. Deserializers allow us to access this data and pull fields out of them that can then be queried against. See more information about deserializers below.

## Fixed

SQE uses the FixedBatchSpout provided by Storm for testing. This allows you to write a list of of fields and values that will be emitted onto the stream as part of a query.

### Options

* jw.sqe.spout.fixed.fields - The fields that are emitted onto the stream as an array 
* jw.sqe.spout.fixed.values - An array of value arrays that the spout emits onto the stream. Each array is emitted as a tuple and the order of values in each array corresponds to the order of the fields.

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

## Kafka

The Kafka stream type allows you to read data from a Kafka topic onto a stream. Typically, though not necessarily, you will use a deserializer on the message to create the appropriate fields on the stream. The object name in the CreateStream command is the topic you are reading from. By default, without using a deserializer, the key and value are outputted under the fields _key and _value, respectively.

### Options

* jw.sqe.spout.kafka.zkhosts - An array of Zookeeper hosts, including the port. This is used to locate the Kafka cluster.
* jw.sqe.spout.kafka.clientid - The client ID used by the Kafka spout
* jw.sqe.spout.kafka.bufferSizeBytes (optional)
* jw.sqe.spout.kafka.fetchSizeBytes (optional)
* jw.sqe.spout.kafka.maxOffsetBehind (optional)
* jw.sqe.spout.kafka.filterReplays (default: false) - Enables replay filtering based on stream metadata recorded in the key of each record. See [Replay Filtering](https://github.com/jwplayer/sqe/wiki/replay-filtering) for more information.
* jw.sqe.spout.kafka.filterReplays.metadata.ttl (default 172800) - The TTL (in seconds) of any individual metadata/highwater marks recorded by the replay filtering functionality. This prevents new PIDs from accumulating over time.

### Example

    {
      "CreateStream": {
        "streamName": "big.query.data",
        "objectName": "my-topic",
        "spoutName": "KAFKA",
        "spoutType": "TRANSACTIONAL",
        "deserializer": "avro"
        "options": {
          "jw.sqe.spout.kafka.zkhosts": ["zk-01.host.com:2181","zk-02.host.com:2181"],
          "jw.sqe.spout.kafka.clientid": "my-client-id",
          "jw.sqe.spout.deserializer.avro.schemaname": "my.avro.schema"
        }
      }
    }

# Deserializers

Deserializers allow us to take serialized or otherwise packed data that is emitted by a spout, parse it, and split its constituent parts into fields on the tuple. Kafka is a good example since it stores data packed as a message that can be formatted in any number of ways such as Avro. If no deserializer is specified, then the data remains on the stream as it is emitted by the spout without an pre-processing between the spout and the queries.

## Avro

The Avro deserializer takes an Avro record as a byte array along with a schema. Based on the fields needed by the queries, they are pulled from each record and added to each tuple. For example, if messages are stored in Kafka in Avro with the following schema:

    {
      "fields": [
        {"name": "DateTime", "type": "string"},
        {"name": "Embeds", "type": "int"},
        {"name": "Plays", "type": "int"},
        {"name": "Completes", "type": "int"},
        {"name": "AdImpressions", "type": "int"},
        {"name": "TimeWatched", "type": "long"}
      ],
      "name": "HourlyMeasures",
      "namespace": "com.jwplayer.analytics.avro",
      "type": "record"
    }

We then have a query that references the DateTime, Embeds, and Plays fields. The byte array message is then deserialized into an Avro record, then those 3 fields are added to the tuple as fields with the same name as the fields in the Avro schema.

### Options

* jw.sqe.spout.deserializer.avro.schemaname - The name of the avro schema to use to deserialize the byte array. The full namespace should be included. This should point to a Java object that implements SpecificRecord.
**TODO: Allow specifying avro schemas either inline or from another file as another way of deserializing Avro objects**