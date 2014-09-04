package jp.co.tis.stc.example.storm.spout


import backtype.storm.spout.SchemeAsMultiScheme
import backtype.storm.topology.IRichSpout

import com.amazonaws.ClientConfiguration
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider
import com.amazonaws.services.kinesis.stormspout.{ KinesisSpout, KinesisSpoutConfig }

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
    case "KINESIS" => {
      val prop = new java.util.Properties()
      prop.load(this.getClass.getClassLoader.getResourceAsStream("kinesis.properties"))
      val conf = prop.asScala

      val clientConfig = new ClientConfiguration()
      conf.get("http.proxyHost").filter(_.nonEmpty).map(v=>clientConfig.setProxyHost(v))
      conf.get("http.proxyPort").filter(_.nonEmpty).map(v=>clientConfig.setProxyPort(v.toInt))
      conf.get("http.proxyUser").filter(_.nonEmpty).map(v=>clientConfig.setProxyUsername(v))
      conf.get("http.proxyPassword").filter(_.nonEmpty).map(v=>clientConfig.setProxyPassword(v))

      val kinesisSpoutConfig = new KinesisSpoutConfig(conf("kinesis.streamName"), conf("storm.zkHosts"))
      new KinesisSpout(kinesisSpoutConfig, new ClasspathPropertiesFileCredentialsProvider(), clientConfig)
    }
    case _ => throw new RuntimeException("invalid type %s".format(spoutType))
  }
}
