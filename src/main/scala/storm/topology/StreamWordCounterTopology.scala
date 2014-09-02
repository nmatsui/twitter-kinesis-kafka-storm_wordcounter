package jp.co.tis.stc.example.storm.topology

import backtype.storm.{ Config, LocalCluster }
import backtype.storm.topology.TopologyBuilder
import backtype.storm.tuple.Fields

import backtype.storm.spout.SchemeAsMultiScheme

import storm.kafka.{ KafkaSpout, SpoutConfig, ZkHosts, StringScheme }

import jp.co.tis.stc.example.storm.bolt.{ MorphologicalAnalysisBolt, WordCountBolt }
import jp.co.tis.stc.example.storm.spout.StreamSpoutFactory

object StreamWordCounterTopology {
  def main(args:Array[String]) {
    val spout = StreamSpoutFactory.getInstance(args(0))

    val builder = new TopologyBuilder()
    builder.setSpout("spout", spout)
    builder.setBolt("split", new MorphologicalAnalysisBolt()).shuffleGrouping("spout")
    builder.setBolt("count", new WordCountBolt()).fieldsGrouping("split", new Fields("word"))

    val config = new Config()
    config.setDebug(true)
    config.setMaxTaskParallelism(2)

    val cluster = new LocalCluster()
    cluster.submitTopology("word-count", config, builder.createTopology())
  }
}
