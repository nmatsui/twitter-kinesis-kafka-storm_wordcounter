package jp.co.tis.stc.example.storm.spout


import backtype.storm.spout.SchemeAsMultiScheme
import backtype.storm.topology.IRichSpout

import storm.kafka.{ KafkaSpout, SpoutConfig, ZkHosts, StringScheme }

import scala.collection.JavaConverters._

object StreamSpoutFactory {
  def getInstance(spoutType:String):IRichSpout = spoutType match {
    case "TEST" => new RandomSentenceSpout()
    case "KAFKA" => {
      val prop = new java.util.Properties()
      prop.load(this.getClass.getClassLoader.getResourceAsStream("kafka.properties"))
      val conf = prop.asScala

      val zkHosts = new ZkHosts(conf("kafka.zkHosts"))
      val topic = conf("kafka.topic")
      val spoutConfig = new SpoutConfig(zkHosts, topic, "/kafkastorm", topic)
      spoutConfig.scheme = new SchemeAsMultiScheme(new StringScheme())
      new KafkaSpout(spoutConfig)
    }
    case _ => throw new RuntimeException("invalid type %s".format(spoutType))
  }
}
