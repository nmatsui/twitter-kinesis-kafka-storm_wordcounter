name := "wordcount"

version := "0.0.1"

scalaVersion := "2.11.2"

sbtVersion := "0.13.5"

fork in run := true

resolvers ++=Seq(
  "clojars.org" at "http://clojars.org/repo",
  "Atilika Open Source repository" at "http://www.atilika.org/nexus/content/repositories/atilika"
)

libraryDependencies ++= Seq(
  "org.apache.storm" % "storm-core" % "0.9.2-incubating",
  "org.atilika.kuromoji" % "kuromoji" % "0.7.7",
  "net.debasishg" % "redisclient_2.10" % "2.12"
)

mainClass in (Compile, run) := Some("jp.co.tis.stc.example.topology.LocalWordCountTopology")

