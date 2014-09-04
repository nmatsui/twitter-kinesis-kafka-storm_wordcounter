package jp.co.tis.stc.example.storm.topology

import backtype.storm.{ Config, LocalCluster }
import backtype.storm.topology.TopologyBuilder
import backtype.storm.tuple.Fields

import jp.co.tis.stc.example.storm.bolt.{ KinesisDecodeBolt, MorphologicalAnalysisBolt, WordCountBolt }
import jp.co.tis.stc.example.storm.spout.StreamSpoutFactory

object StreamWordCounterTopology {
  def main(args:Array[String]) {
    if (args.length < 1) {
      println("Usage: java -jar wordcounter.jar (TEST|KAFKA|KINESIS)")
      sys.exit(1)
    }
    val spout = StreamSpoutFactory.getInstance(args.head)

    val builder = new TopologyBuilder()
    builder.setSpout("spout", spout)
    builder.setBolt("decode", new KinesisDecodeBolt()).shuffleGrouping("spout")
    builder.setBolt("split", new MorphologicalAnalysisBolt()).shuffleGrouping("decode")
    builder.setBolt("count", new WordCountBolt()).fieldsGrouping("split", new Fields("word"))

    val config = new Config()
    config.setDebug(true)
    config.setMaxTaskParallelism(2)

    val cluster = new LocalCluster()
    cluster.submitTopology("word-count", config, builder.createTopology())
  }
}
