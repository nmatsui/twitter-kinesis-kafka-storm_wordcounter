name := "wordcount"

version := "0.0.1"

scalaVersion := "2.10.4"

sbtVersion := "0.13.5"

fork in run := true

resolvers ++=Seq(
  "clojars.org" at "http://clojars.org/repo",
  "Atilika Open Source repository" at "http://www.atilika.org/nexus/content/repositories/atilika"
)

libraryDependencies ++= Seq(
  "org.apache.storm" % "storm-core" % "0.9.2-incubating",
  "org.apache.storm" % "storm-kafka" % "0.9.2-incubating"
    exclude("org.apache.zookeeper", "zookeeper"),
  "org.apache.kafka" % "kafka_2.10" % "0.8.1.1"
    exclude("org.apache.zookeeper", "zookeeper")
    exclude("log4j", "log4j"),
  "org.atilika.kuromoji" % "kuromoji" % "0.7.7",
  "net.debasishg" % "redisclient_2.10" % "2.12",
  "com.amazonaws" % "aws-java-sdk" % "1.8.9.1",
  "org.apache.commons" % "commons-lang3" % "3.3.2",
  "com.google.guava" % "guava" % "18.0",
  "com.netflix.curator" % "curator-framework" % "1.3.3"
)

mainClass in (Compile, run) := Some("jp.co.tis.stc.example.storm.topology.StreamWordCounterTopology")

