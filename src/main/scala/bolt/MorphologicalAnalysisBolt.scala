package jp.co.tis.stc.example.bolt

import backtype.storm.task.{ OutputCollector, TopologyContext }
import backtype.storm.topology.OutputFieldsDeclarer
import backtype.storm.topology.base.BaseRichBolt
import backtype.storm.tuple.{ Fields, Tuple, Values }

import org.atilika.kuromoji.Tokenizer

import java.util.{ Map => JMap }
import scala.collection.JavaConverters._

class MorphologicalAnalysisBolt extends BaseRichBolt {
  var collector:OutputCollector = _
  var tokenizer:Tokenizer = _

  override def prepare(config:JMap[_, _], context:TopologyContext, collector:OutputCollector) {
    this.collector = collector
    this.tokenizer = Tokenizer.builder.build
  }
  override def execute(tuple:Tuple) {
    println("***")
    tokenizer.tokenize(tuple.getString(0)).asScala.toList
      .filter(token => List("名詞", "動詞", "形容詞", "副詞").contains(token.getPartOfSpeech.split(",")(0)))
      .map(token => if (token.isKnown) token.getBaseForm else token.getSurfaceForm)
      .map(word => this.collector.emit(tuple, new Values(word)))
    this.collector.ack(tuple)
  }
  override def declareOutputFields(declarer:OutputFieldsDeclarer) {
    declarer.declare(new Fields("word"))
  }
}
