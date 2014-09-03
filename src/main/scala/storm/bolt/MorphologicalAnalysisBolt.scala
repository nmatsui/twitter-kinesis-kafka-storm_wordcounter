package jp.co.tis.stc.example.storm.bolt

import backtype.storm.task.{ OutputCollector, TopologyContext }
import backtype.storm.topology.OutputFieldsDeclarer
import backtype.storm.topology.base.BaseRichBolt
import backtype.storm.tuple.{ Fields, Tuple, Values }

import java.util.{ Map => JMap }

import org.atilika.kuromoji.Tokenizer

import scala.collection.JavaConverters._

class MorphologicalAnalysisBolt extends BaseRichBolt {
  private var collector:OutputCollector = _
  private var tokenizer:Tokenizer = _

  override def prepare(config:JMap[_, _], context:TopologyContext, collector:OutputCollector) {
    this.collector = collector
    this.tokenizer = Tokenizer.builder.build
  }
  override def execute(tuple:Tuple) {
    val tweet = tuple.getString(0)
    tokenizer.tokenize(tweet).asScala.toList
      .filter(token => List("名詞", "動詞", "形容詞").contains(token.getPartOfSpeech.split(",")(0)))
      .map(token => if (token.isKnown) token.getBaseForm else token.getSurfaceForm)
      .filter(word => word.length >= 5)
      .map(word => this.collector.emit(tuple, new Values(word)))
    this.collector.ack(tuple)
  }
  override def declareOutputFields(declarer:OutputFieldsDeclarer) {
    declarer.declare(new Fields("word"))
  }
}
