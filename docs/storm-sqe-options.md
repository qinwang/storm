# Options

## Topology Config

The config file for the SQE Topology class allows you to specify both Storm and SQE options using a YAML file. The format of this file looks like:

    Storm:
        topology.workers: 2
        other.storm.option: value
    SQE:
        jw.sqe.parallelism.hint: 10
        jw.sqe.spout.kafka.zkhosts:
            - zookeeper:2181
        other.sqe.option: value

The key/value pairs in the Storm section are added to the Topology config. Pairs in the SQE section are added to the SQE config. Options specified in a command file (either through the SET command or within an options section) override options specified in the config file.

## Query Engine

* jw.sqe.parallelism.hint - This determines the parallelism for each input stream. So a paralellism hint of 20 with an input stream plus the primary processing bolt would translate into 40 executors. This does not affect the number of executors for downstream global aggregations or bolts used for persisting state. These bolts currently get a single executor each.