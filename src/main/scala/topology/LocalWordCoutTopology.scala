package jp.co.tis.stc.example.topology

import backtype.storm.{ Config, LocalCluster }
import backtype.storm.topology.TopologyBuilder
import backtype.storm.tuple.Fields

import jp.co.tis.stc.example.spout.RandomSentenceSpout
import jp.co.tis.stc.example.bolt.{ MorphologicalAnalysisBolt, WordCountBolt }

object LocalWordCountTopology {
  def main(args:Array[String]) {

    val builder = new TopologyBuilder()
    builder.setSpout("spout", new RandomSentenceSpout())
    builder.setBolt("split", new MorphologicalAnalysisBolt()).shuffleGrouping("spout")
    builder.setBolt("count", new WordCountBolt()).fieldsGrouping("split", new Fields("word"))

    val config = new Config()
    config.setDebug(true)
    config.setMaxTaskParallelism(2)

    val cluster = new LocalCluster()
    cluster.submitTopology("word-count", config, builder.createTopology())
  }
}
