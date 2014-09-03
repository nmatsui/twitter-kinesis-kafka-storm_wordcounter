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
  "net.debasishg" % "redisclient_2.10" % "2.12"
)

lazy val root = project.in(file(".")).dependsOn(kinesisStromSpout)

lazy val kinesisStromSpout = uri("https://github.com/nmatsui/kinesis-storm-spout.git#develop")


mainClass in (Compile, run) := Some("jp.co.tis.stc.example.storm.topology.StreamWordCounterTopology")

