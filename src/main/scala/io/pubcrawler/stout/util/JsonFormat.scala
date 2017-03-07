package io.pubcrawler.stout.util

import java.text.SimpleDateFormat

import io.pubcrawler.stout.db.{Gender, Status}
import org.json4s.ext.EnumNameSerializer
import org.json4s.{DefaultFormats, Formats}


trait JsonFormat {
  implicit lazy val formats: Formats = new DefaultFormats {
    override def dateFormatter: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
  } + new EnumNameSerializer(Gender) + new EnumNameSerializer(Status)
}