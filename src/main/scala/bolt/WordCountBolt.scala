package jp.co.tis.stc.example.bolt

import backtype.storm.task.{ OutputCollector, TopologyContext }
import backtype.storm.topology.OutputFieldsDeclarer
import backtype.storm.topology.base.BaseRichBolt
import backtype.storm.tuple.{ Fields, Tuple, Values }


import java.util.{ Map => JMap }

import java.io.PrintWriter

class WordCountBolt extends BaseRichBolt {
  var collector:OutputCollector = _
  var out:PrintWriter = _
  val words = scala.collection.mutable.Map[String, Int]()

  override def prepare(config:JMap[_, _], context:TopologyContext, collector:OutputCollector) {
    this.collector = collector
    this.out = new PrintWriter("./hogehoge.txt", "UTF-8")
  }
  override def execute(tuple:Tuple) {
    println("###")
    val word = tuple.getString(0)
    words += word -> (words.getOrElseUpdate(word, 0) + 1)
    this.out.println("%s : %d".format(word, words(word)))
    this.out.flush
    this.collector.ack(tuple)
  }
  override def declareOutputFields(declarer:OutputFieldsDeclarer) {
    declarer.declare(new Fields("word"))
  }
}
