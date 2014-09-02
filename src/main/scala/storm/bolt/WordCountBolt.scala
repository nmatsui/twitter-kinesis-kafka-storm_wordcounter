package jp.co.tis.stc.example.bolt

import backtype.storm.task.{ OutputCollector, TopologyContext }
import backtype.storm.topology.OutputFieldsDeclarer
import backtype.storm.topology.base.BaseRichBolt
import backtype.storm.tuple.{ Fields, Tuple, Values }

import com.redis._

import java.util.{ Map => JMap }

class WordCountBolt extends BaseRichBolt {
  private var collector:OutputCollector = _
  private var redis:RedisClient = _

  override def prepare(config:JMap[_, _], context:TopologyContext, collector:OutputCollector) {
    this.collector = collector
    this.redis = new RedisClient("localhost", 6379)
  }
  override def execute(tuple:Tuple) {
    this.redis.zincrby("words", 1, tuple.getString(0))
    this.collector.ack(tuple)
  }
  override def declareOutputFields(declarer:OutputFieldsDeclarer) {
    declarer.declare(new Fields("word"))
  }
}
