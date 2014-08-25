package jp.co.tis.stc.example.spout

import backtype.storm.spout.SpoutOutputCollector
import backtype.storm.task.TopologyContext
import backtype.storm.topology.OutputFieldsDeclarer
import backtype.storm.topology.base.BaseRichSpout
import backtype.storm.tuple.{ Fields, Values }

import java.util.Random
import java.util.{ Map => JMap }

class RandomSentenceSpout extends BaseRichSpout {
  var collector:SpoutOutputCollector = _
  var rand:Random = _

  override def open(config:JMap[_, _], context:TopologyContext, collector:SpoutOutputCollector) {
    this.collector = collector
    this.rand = new Random()
  }
  override def nextTuple() {
    Thread.sleep(3000)
    val sentences = Array("the cow jumped over the moon", "an apple a day keeps the doctor away", "four score and seven years ago", "snow white and the seven dwarfs", "i am at two with nature")
    val sentence = sentences(rand.nextInt(sentences.length))
    collector.emit(new Values(sentence))
  }
  override def declareOutputFields(declarer:OutputFieldsDeclarer) {
    declarer.declare(new Fields("word"))
  }
}
