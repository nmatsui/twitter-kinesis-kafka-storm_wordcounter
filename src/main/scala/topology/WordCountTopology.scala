package jp.co.tis.stc.example.topology

import backtype.storm.{ Config, LocalCluster }
import backtype.storm.topology.TopologyBuilder
import backtype.storm.tuple.Fields


object WordCountTopology {
  def main(args:Array[String]) {
    import jp.co.tis.stc.example.spout.RandomSentenceSpout
    //import jp.co.tis.stc.example.bolt.{ SplitSentenceBolt, WordCountBolt }
    import jp.co.tis.stc.example.bolt.SplitSentenceBolt

    val builder = new TopologyBuilder()
    builder.setSpout("spout", new RandomSentenceSpout())
    builder.setBolt("split", new SplitSentenceBolt()).shuffleGrouping("spout")
    //builder.setBolt("count", new WordCountBolt()).fieldGrouping("split", new Fields("word"))

    val config = new Config()
    config.setDebug(false)
    config.setMaxTaskParallelism(2)

    val cluster = new LocalCluster()
    cluster.submitTopology("word-count", config, builder.createTopology())
    //Thread.sleep(30000)
    //cluster.shutdown()
  }
}
