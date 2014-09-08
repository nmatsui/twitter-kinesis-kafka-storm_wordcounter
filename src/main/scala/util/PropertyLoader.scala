package jp.co.tis.stc.example.util

import java.util.Properties

import scala.collection.JavaConverters._

trait PropertyLoader {
  def loadProperties(filename:String):Map[String, String] = {
    val prop = new Properties()
    prop.load(this.getClass.getClassLoader.getResourceAsStream(filename))
    Map(prop.asScala.toSeq:_*)
  }
}
