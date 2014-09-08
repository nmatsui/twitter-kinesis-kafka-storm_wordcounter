twitter-kinesis-kafka-storm_wordcounter
====

"Twitter Stream -> **Producer** -> (AWS Kinesis | Apache Kafka) -> Apache Storm[**WordCounter**] -> Redis" example.

This is the "**WordCounter**" part.

## Description

This project is an example program which retrieves tweets form "Apache Kafka" or "AWS Kinesis", executes morphological analysis and counts word frequencies.

## Related Project

* Berksfile and Vagrantfile : https://github.com/nmatsui/twitter-kinesis-kafka-storm_vagrant
* WordCounter(**this project**) : https://github.com/nmatsui/twitter-kinesis-kafka-storm_wordcounter
* Producer : https://github.com/nmatsui/twitter-kinesis-kafka-storm_producer
* Kinesis-storm-spout : https://github.com/nmatsui/kinesis-storm-spout

## Requirements

This program is written in scala 2.10.
You have to install java 1.6 or lator and sbt 0.13.x to build this project.

This program depends on "Apache Storm 0.9.2-incubating", "Apache Kafka 0.8.1.1", "kinesis-storm-spout(forked by nmatsui)", "Kuromoji 0.7.7", and "Redis Client 2.12".

## Prepare

### Construct Apache Zookeeper, Apache Kafka, Apache Storm and Redis

1. Before all, you have to install Apache Zookeeper, Apache Kafka, Apache Storm and Redis, and set hostnames of Zookeeper, Kafka, Storm and Redis server to DNS or `/etc/hosts`.
1. Next, set Zookeeper and Kafka hostname to configuration file of Apache Kafka.
1. Finally, you have to install sbt 0.13.x 

you can use [Berksfile and Vagrantfile](https://github.com/nmatsui/twitter-kinesis-kafka-storm_vagrant) to construct this environments on VirtualBox. See [this Project](https://github.com/nmatsui/twitter-kinesis-kafka-storm_vagrant).

### Create AWS Kinesis Stream

1. Create AWS Kinesis Stream called "tweets".
 - This program can use US EAST(N.Virginia) only.

### Create Apache Kafka Topic

1. Create Apache Kafka Topic called "tweets".

## Install

### Clone this project

1. `git clone https://github.com/nmatsui/twitter-kinesis-kafka-storm_wordcounter.git`

### Set appropriate values to properties

1. Rename configuraition files from `src/main/resoureces/*.properties.template` to `src/main/resources*.properties`.
1. Set an appropriate value for each property.

### Compile & Assembly

1. `sbt compile`
 - The first compile task takes a long time because sbt donwloads many dependent libraries.
2. `sbt assembly`

## Usage

1. Before start, you have to start Apache Zookeeper and Apache Kafka.
1. `java -jar target/scala-2.10/wordcounter.jar (TEST|KAFKA|KINESIS)`

## License
Apache License, Version 2.0
 
## Author
Nobuyuki Matsui (nobuyuki.matsui@gmail.com)
