# States

## Kafka

SQE uses a slightly modified version of the storm-kafka project for its Kafka state implementation. The objectName in the InsertInto clause is the topic that messages are written to. Depending on the particular key type that is used, this state accept one or two fields which correspond to the key and value written to the Kafka topic. 

### Options

Most, but not all, of these options map to the underlying Kafka producer options.

* jw.sqe.state.kafka.brokers - An array of Kafka brokers. Maps to metadata.broker.list as a comma delimited string.
* jw.sqe.state.kafka.serializerClass (default: org.apache.kafka.common.serialization.ByteArraySerializer) - Maps to value.serializer
* jw.sqe.state.kafka.key.serializerClass (default: org.apache.kafka.common.serialization.ByteArraySerializer) - Maps to key.serializer
* jw.sqe.state.kafka.partitionClass (default: org.apache.kafka.clients.producer.internals.DefaultPartitioner) - Maps to partitioner.class
* jw.sqe.state.kafka.producerType (default: async) - Maps to producer.type
* jw.sqe.state.kafka.request.requiredAcks (default: 1) - Maps to acks
* jw.sqe.state.kafka.keytype (default: MessageHash) - This determines how the key for each Kafka record is created. The three options are:
    * Field - Normally, only one field is allowed, which is written as the value, and the key is generated. If the field key type is used, then two fields are required. The first is the key and the second is the value.
    * MessageHash - Generates the key by hashing (using Murmur2) the value 
    * StreamMetadata - Generates a 20 byte key from the internal metadata SQE attaches to each tuple. This is useful for lightweight replay filtering in downstream SQE topologies. See [Replay Filtering](https://github.com/jwplayer/sqe/wiki/replay-filtering) for more information.

### Example

    "insertInto": {
      "objectName":"ping-avro-sqe-all-pings-test",
      "stateName": "Kafka",
      "stateType": "NON_TRANSACTIONAL",
      "fields" : ["message"],
      "jw.sqe.state.kafka.keytype": "StreamMetadata",
      "jw.sqe.state.kafka.producerType": "sync",
      "jw.sqe.state.kafka.partitionClass": "com.jwplayer.sqe.language.state.kafka.SourcePartitionPartitioner",
      "jw.sqe.state.kafka.brokers": ["mykafka-01", "mykafka-02"]
    }

## MongoDB

SQE uses an internal map state implementation to persist data to Mongo DB. The objectName in the InsertInto clause is the collection to write to and the fields are the fields names in each record. One record is written per value field for each set of keys. The _id field is generated from a hash of the keys plus the value field name written into a byte array. The order of the keys here matches the order of the key field names in the InsertInto clause. For transactional and opaque states, value fields are written as sub-documents containing the appropriate transaction ID and other fields. 

### Options

* jw.sqe.state.mongodb.cachesize (default: 5000) - The maximum number of objects in the map cache
* jw.sqe.state.mongodb.db - The database to write to
* jw.sqe.state.mongodb.hosts - A list of hostnames to connect to
* jw.sqe.state.mongodb.password - The password
* jw.sqe.state.mongodb.port (default: 27017) - The port
* jw.sqe.state.mongodb.replicaSet - The replica set name
* jw.sqe.state.mongodb.userName - The username

### Example

    "insertInto":{
      "objectName":"DailyAcctEPCUniq",
      "stateName": "MONGO",
      "stateType": "TRANSACTIONAL",
      "fields":["date","accounttoken","ttlmarker","uniques","embeds","plays","completes","tw"]
      "options": {
        "jw.sqe.state.mongodb.db": "mydb", 
        "jw.sqe.state.mongodb.hosts": ["mymongodb.com"],
        "jw.sqe.state.mongodb.password": "password",
        "jw.sqe.state.mongodb.replicaSet": "myreplicaset", 
        "jw.sqe.state.mongodb.userName": "bob"
      }
    }

## Redis

SQE uses a modified version of the storm-redis project for its Redis map state. Query data can be persisted as Redis strings or hashes, with string being the default. SQE uses a custom serializer using Gson. For transactional states, the format is in JSON: `[<TXID>,<VALUE_TYPE>,<VALUE>]`. Opaque states are formatted like: `[<TXID>,<VALUE_TYPE>,<VALUE>]`. Currently supported value types are:

* N - null
* I - Integer
* L - Long
* F - Float
* D - Double
* S - String
* B - Byte Array

An example transactional value is: `[6491,"I",0]`

How the InsertInto fields are translated into Redis data structures depends on the Redis data type being used. The two supported types are string and hash. 

### String

String is the default data type used by SQE. A custom KeyFactory is used that formats the aggregated data like: `<OBJECT_NAME>:<KEY_FIELD1>:...:<KEY_FIELDN>:<VALUE_FIELD>`. ':' is the default delimiter, but it can be configured (see the Options section below). For example, if you have the key field "Timestamp" and value field "AdImpressions" in your InsertInto clause, a key/value pair in Redis could look like: 

Key: `HourlyEvents:2015-05-18T20:AdImpressions`
Value: `[1234,"I",5678]`

This is the simplest way to send data to Redis. However, for large amounts of data, it's often better to format the data using the hash data type. 

### Hash

This data type splits the InsertInto key fields between the hash's key name and field name based on the "jw.sqe.state.redis.keyname.fields" and "jw.sqe.state.redis.fieldname.fields" options in the query (see the Options section below). For example, if you have the following InsertInto clause in your query:

    "insertInto":{
      "objectName":"TestQuery",
      "stateName": "REDIS",
      "stateType": "TRANSACTIONAL",
      "fields":["Account","Timestamp","Domain","Views","Clicks"],
      "options": {
        "jw.sqe.state.redis.datatype": "HASH",
        "jw.sqe.state.redis.keyname.fields": ["Account","Timestamp"],
        "jw.sqe.state.redis.fieldname.fields": ["Domain"]
      }
    }

The data in Redis would look like:

Key Name: `TestQuery:ABCD-1234:2015-05-18T20:`
Field Name: `bob.com:Views`
Value: `[1234,"I",5678]`

Another way to think of this, is that you are splitting the standard Redis string representation in two. The first half is used for the Redis key name, the second for the Redis field name. The objectName always appears first in the Redis key name and the value field name always appears at the end of the Redis field name. The same delimiter option is used as for Redis strings.

### Options

* jw.sqe.state.redis.database (default: 0) - The Redis DB to write to
* jw.sqe.state.redis.datatype (default: STRING) - The Redis data type to use for storing the queried data in Redis. The currently available options are STRING and HASH. When using the HASH data type, you should also include the "jw.sqe.state.redis.fieldname.fields" and "jw.sqe.state.redis.keyname.fields" options so the RedisMapState knows how to build the key names and field names from the given keys from the query.
* jw.sqe.state.redis.delimiter (default: :) - The delimiter used by the KeyFactory
* jw.sqe.state.redis.expireintervalsec (default: 0) - Sets the expiration TTL for all keys. No TTL is set if this is 0.
* jw.sqe.state.redis.fieldname.fields - A list of key fields used to create the field name in a Redis hash
* jw.sqe.state.redis.host - The host of the Redis server
* jw.sqe.state.redis.keyname.fields - A list of key fields used to create the key name in a Redis hash
* jw.sqe.state.redis.port (default: 6379) - The port of the Redis server