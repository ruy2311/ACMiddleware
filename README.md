# ACMiddleware
 Middleware
|       |-- AMQCommon
|       |   |-- build
|       |   |   |-- classes
|       |   |   |   `-- common
|       |   |   |       |-- RegistrationInformation.class
|       |   |   |       |-- Wrapper.class
|       |   |   |       |-- agent
|       |   |   |       |   |-- Agent.class
|       |   |   |       |   |-- AgentConnector.class
|       |   |   |       |   `-- AgentDefinition.class
|       |   |   |       |-- command
|       |   |   |       |   `-- Command.class
|       |   |   |       `-- events
|       |   |   |           |-- QueueEvent.class
|       |   |   |           |-- QueueListener.class
|       |   |   |           |-- RegistrationEvent.class
|       |   |   |           `-- RegistrationListener.class
|       |   |   `-- empty
|       |-- AMQServer
|       |   |-- build
|       |   |   |-- classes
|       |   |   |   `-- Server
|       |   |   |       |-- AMQSInterface.class
|       |   |   |       |-- AMQServer.class
|       |   |   |       |-- AgentConnection.class
|       |   |   |       |-- DataStructure
|       |   |   |       |   |-- AgentQueue.class
|       |   |   |       |   |-- GroupDefinition.class
|       |   |   |       |   |-- GroupQueue.class
|       |   |   |       |   |-- MessageQueue.class
|       |   |   |       |   `-- RegistrationMap.class
|       |   |   |       |-- EventAdapter.class
|       |   |   |       `-- XMLParser.class
|       |   |   `-- empty
|       |   |-- build-before-profiler.xml
|       |   |-- build.xml
|       |   |-- config
|       |   |   |-- SystemAgents.xml
|       |   |   |-- SystemAgents.xml~
|       |   |   |-- SystemGroups.xml
|       |   |   `-- SystemGroups.xml~
        |-- AgentContainer
|           |-- build
|           |   |-- classes
|           |   |   `-- Client
|           |   |       |-- AgentLoader$ListFilter.class
|           |   |       |-- AgentLoader.class
|           |   |       |-- Container.class
|           |   |       |-- ContainerConnection.class
|           |   |       |-- ContainerInterface.class
|           |   |       |-- EventAdapter.class
|           |   |       |-- GUI
|           |   |       |   |-- AgentFrame.class
|           |   |       |   |-- AplicationLoader$1.class
|           |   |       |   |-- AplicationLoader$2.class
|           |   |       |   |-- AplicationLoader$3.class
|           |   |       |   |-- AplicationLoader.class
|           |   |       |   |-- Plugin$1.class
|           |   |       |   `-- Plugin.class
|           |   |       `-- XMLParser.class
