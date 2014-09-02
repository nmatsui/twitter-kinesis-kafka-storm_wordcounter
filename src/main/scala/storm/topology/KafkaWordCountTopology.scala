package jp.co.tis.stc.example.topology

import backtype.storm.{ Config, LocalCluster }
import backtype.storm.topology.TopologyBuilder
import backtype.storm.tuple.Fields

import backtype.storm.spout.SchemeAsMultiScheme

import storm.kafka.{ KafkaSpout, SpoutConfig, ZkHosts, StringScheme }

import jp.co.tis.stc.example.bolt.{ MorphologicalAnalysisBolt, WordCountBolt }

object KafkaWordCountTopology {
  def main(args:Array[String]) {
    val zkHosts = new ZkHosts("kafka")
    val topic = "tweets"
    val kafkaConfig = new SpoutConfig(zkHosts, topic, "/kafkastorm", topic)
    kafkaConfig.scheme = new SchemeAsMultiScheme(new StringScheme())
    val kafkaSpout = new KafkaSpout(kafkaConfig)

    val builder = new TopologyBuilder()
    builder.setSpout("spout", kafkaSpout)
    builder.setBolt("split", new MorphologicalAnalysisBolt()).shuffleGrouping("spout")
    builder.setBolt("count", new WordCountBolt()).fieldsGrouping("split", new Fields("word"))

    val config = new Config()
    config.setDebug(true)
    config.setMaxTaskParallelism(2)

    val cluster = new LocalCluster()
    cluster.submitTopology("word-count", config, builder.createTopology())
  }
}
