# Using SQE

Using the SQE topology builder is straightforward. You just launch the topology using Storm with appropriate command line options:

    storm jar sqe.jar com.jwplayer.sqe.Topology --config ./conf/conf.yaml --commands ./commands/commands1.json /my/commands/commands2.json --name=my-topology

The locations of config and command files can be specified using a URI. For example

    file:///conf/conf.yaml

You can see all of the command line options by running:

    java jar sqe.jar com.jwplayer.sqe.Topology --help

Refer to the [Commands](storm-sqe-commands.html) section for more information on the JSON command format and the [Options](storm-sqe-options.html) section for more information on the options and config file format.