package jp.co.tis.stc.example.storm.bolt

import backtype.storm.task.{ OutputCollector, TopologyContext }
import backtype.storm.topology.OutputFieldsDeclarer
import backtype.storm.topology.base.BaseRichBolt
import backtype.storm.tuple.{ Fields, Tuple, Values }

import com.redis._

import java.util.{ Map => JMap }

import jp.co.tis.stc.example.util.PropertyLoader

class WordCountBolt extends BaseRichBolt with PropertyLoader {
  private var collector:OutputCollector = _
  private var redis:RedisClient = _

  override def prepare(config:JMap[_, _], context:TopologyContext, collector:OutputCollector) {
    this.collector = collector
    val redisConf = loadProperties("redis.properties")
    this.redis = new RedisClient(redisConf("redis.host"), redisConf("redis.port").toInt)
  }
  override def execute(tuple:Tuple) {
    this.redis.zincrby("words", 1, tuple.getString(0))
    this.collector.ack(tuple)
  }
  override def declareOutputFields(declarer:OutputFieldsDeclarer) {
    declarer.declare(new Fields("word"))
  }
}
