package jp.co.tis.stc.example.storm.bolt

import backtype.storm.task.{ OutputCollector, TopologyContext }
import backtype.storm.topology.OutputFieldsDeclarer
import backtype.storm.topology.base.BaseRichBolt
import backtype.storm.tuple.{ Fields, Tuple, Values }

import com.amazonaws.services.kinesis.model.Record
import com.amazonaws.services.kinesis.stormspout.DefaultKinesisRecordScheme

import java.nio.ByteBuffer
import java.nio.charset._
import java.util.{ Map => JMap }

class KinesisDecodeBolt extends BaseRichBolt {
  private var collector:OutputCollector = _
  private var decoder:CharsetDecoder = _

  override def prepare(config:JMap[_, _], context:TopologyContext, collector:OutputCollector) {
    this.collector = collector
    this.decoder = Charset.forName("UTF-8").newDecoder()
  }
  override def execute(tuple:Tuple) {
    if (tuple.contains(DefaultKinesisRecordScheme.FIELD_RECORD)) {
      val record = tuple.getValueByField(DefaultKinesisRecordScheme.FIELD_RECORD).asInstanceOf[Record]
      val buffer = record.getData()
      val data = this.decoder.decode(buffer).toString
      this.collector.emit(tuple, new Values(data))
    }
    else {
      this.collector.emit(tuple, new Values(tuple.getString(0)))
    }
    this.collector.ack(tuple)
  }
  override def declareOutputFields(declarer:OutputFieldsDeclarer) {
    declarer.declare(new Fields("word"))
  }
}


