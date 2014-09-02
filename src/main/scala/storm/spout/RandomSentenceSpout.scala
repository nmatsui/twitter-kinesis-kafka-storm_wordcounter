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
    val sentences = Array("ScalaはJVM上で動作するプログラミング言語で、関数型の特徴とオブジェクト指向の特徴を合わせ持った、欲張りな言語です。", "JVM上で動作するため、既存の膨大なJavaライブラリをそのまま流用でき、JVMのパフォーマンスチューニングノウハウを最大限活用することができます。またJavaよりも豊富な記述形式を持ちながらもJavaオブジェクトをそのまま扱え、強力な型推論を持った静的型付け言語でもあるため、定型的で冗長な記述を省略できる上にコンパイル時のチェックが強力な「Better Java」として利用することも可能です。", "一方でScalaは、「不変なコレクションや再代入を禁じる変数宣言valのみ利用する」など、 参照透過性を保つための一定の紳士協定 に従っている限り、関数型プログラミング言語として振る舞うこともできます。このあたりは「関数型プログラミング」しかできない Haskell などの純粋関数型プログラミング言語とは対照的です。", "それ以外にもActorによる並列処理や、暗黙の型変換による既存クラスの拡張（に見える記法）など、Scalaはとても大きな言語仕様とAPIを提供しています。", "では、 前回 Java8で実装したお題をScalaで実装してみましょう。Java8と同様、簡単に並列化できるでしょうか。")
    val sentence = sentences(rand.nextInt(sentences.length))
    collector.emit(new Values(sentence))
  }
  override def declareOutputFields(declarer:OutputFieldsDeclarer) {
    declarer.declare(new Fields("word"))
  }
}
