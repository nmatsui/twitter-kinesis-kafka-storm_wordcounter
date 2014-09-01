package jp.co.tis.stc.example.topology

import backtype.storm.{ Config, LocalCluster }
import backtype.storm.topology.TopologyBuilder
import backtype.storm.tuple.Fields

object WordCountTopology {
  def main(args:Array[String]) {
    import jp.co.tis.stc.example.spout.RandomSentenceSpout
    import jp.co.tis.stc.example.bolt.{ MorphologicalAnalysisBolt, WordCountBolt }

    val builder = new TopologyBuilder()
    builder.setSpout("spout", new RandomSentenceSpout())
    builder.setBolt("split", new MorphologicalAnalysisBolt()).shuffleGrouping("spout")
    builder.setBolt("count", new WordCountBolt()).fieldsGrouping("split", new Fields("word"))

    val config = new Config()
    config.setDebug(false)
    config.setMaxTaskParallelism(2)

    val cluster = new LocalCluster()
    cluster.submitTopology("word-count", config, builder.createTopology())
  }
}
