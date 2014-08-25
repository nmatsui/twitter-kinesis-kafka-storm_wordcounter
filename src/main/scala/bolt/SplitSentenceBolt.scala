package jp.co.tis.stc.example.bolt

import backtype.storm.task.{ OutputCollector, TopologyContext }
import backtype.storm.topology.OutputFieldsDeclarer
import backtype.storm.topology.base.BaseRichBolt
import backtype.storm.tuple.{ Fields, Tuple, Values }

import java.util.{ Map => JMap }

class SplitSentenceBolt extends BaseRichBolt {
  var collector:OutputCollector = _

  override def prepare(config:JMap[_, _], context:TopologyContext, collector:OutputCollector) {
    this.collector = collector
  }
  override def execute(tuple:Tuple) {
    println("*****%s*****".format(tuple))
    this.collector.emit(tuple, new Values(tuple.getString(0)))
    this.collector.ack(tuple)
  }
  override def declareOutputFields(declarer:OutputFieldsDeclarer) {
    declarer.declare(new Fields("word"))
  }
}
