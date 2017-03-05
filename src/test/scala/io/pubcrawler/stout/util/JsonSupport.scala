package io.pubcrawler.stout.util


import java.text.SimpleDateFormat

import org.json4s.{DefaultFormats, Formats}

trait JsonSupport {

  implicit def json4sFormats: Formats = new DefaultFormats {
    override def dateFormatter: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
  }

}
