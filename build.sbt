name := "wordcount"

version := "0.0.1"

scalaVersion := "2.11.2"

sbtVersion := "0.13.5"

fork in run := true

resolvers ++=Seq(
  "clojars.org" at "http://clojars.org/repo"
)

libraryDependencies ++= Seq(
  "org.apache.storm" % "storm-core" % "0.9.2-incubating"
)

mainClass in (Compile, run) := Some("jp.co.tis.stc.example.topology.WordCountTopology")

