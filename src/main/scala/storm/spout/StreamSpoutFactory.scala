package jp.co.tis.stc.example.storm.spout


import backtype.storm.spout.SchemeAsMultiScheme
import backtype.storm.topology.IRichSpout

import com.amazonaws.ClientConfiguration
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider
import com.amazonaws.services.kinesis.stormspout.{ KinesisSpout, KinesisSpoutConfig }

import storm.kafka.{ KafkaSpout, SpoutConfig, ZkHosts, StringScheme }

import jp.co.tis.stc.example.util.PropertyLoader

object StreamSpoutFactory extends PropertyLoader {
  def getInstance(spoutType:String):IRichSpout = spoutType match {
    case "TEST" => new RandomSentenceSpout()
    case "KAFKA" => {
      val kafkaConf = loadProperties("kafka.properties")

      val zkHosts = new ZkHosts(kafkaConf("kafka.zkHosts"))
      val topic = kafkaConf("kafka.topic")
      val spoutConfig = new SpoutConfig(zkHosts, topic, "/kafkastorm", topic)
      spoutConfig.scheme = new SchemeAsMultiScheme(new StringScheme())
      new KafkaSpout(spoutConfig)
    }
    case "KINESIS" => {
      val kinesisConf = loadProperties("kinesis.properties")

      val clientConfig = new ClientConfiguration()
      kinesisConf.get("http.proxyHost").filter(_.nonEmpty).map(v=>clientConfig.setProxyHost(v))
      kinesisConf.get("http.proxyPort").filter(_.nonEmpty).map(v=>clientConfig.setProxyPort(v.toInt))
      kinesisConf.get("http.proxyUser").filter(_.nonEmpty).map(v=>clientConfig.setProxyUsername(v))
      kinesisConf.get("http.proxyPassword").filter(_.nonEmpty).map(v=>clientConfig.setProxyPassword(v))

      val kinesisSpoutConfig = new KinesisSpoutConfig(kinesisConf("kinesis.streamName"), kinesisConf("storm.zkHosts"))
      new KinesisSpout(kinesisSpoutConfig, new ClasspathPropertiesFileCredentialsProvider(), clientConfig)
    }
    case _ => throw new RuntimeException("invalid type %s".format(spoutType))
  }
}
