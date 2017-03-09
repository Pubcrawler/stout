package io.pubcrawler.stout.util

import java.time.format.DateTimeFormatter
import java.time.temporal.{TemporalAccessor, TemporalQuery}
import java.time.{LocalDate, LocalDateTime, LocalTime}

import io.pubcrawler.stout.db.{Gender, Status}
import org.json4s.JsonAST.JString
import org.json4s.ext.EnumNameSerializer
import org.json4s.{CustomSerializer, DefaultFormats, Formats}


trait JsonFormat {
  implicit lazy val formats: Formats = DefaultFormats ++ JavaTimeSerializers.defaults ++ EnumNameSerializers.defaults
}

object EnumNameSerializers {
  val defaults = new EnumNameSerializer(Gender) :: new EnumNameSerializer(Status) :: Nil
}

object JavaTimeSerializers {
  // Borrowed from https://github.com/meetup/json4s-java-time/../JavaTimeSerializers.scala

  val defaults =
    LocalTimeSerializer :: LocalDateSerializer :: LocalDateTimeSerializer :: Nil

  def asQuery[A](f: TemporalAccessor => A): TemporalQuery[A] =
    new TemporalQuery[A] {
      override def queryFrom(temporal: TemporalAccessor): A = f(temporal)
    }

  class LocalTimeSerializer private[JavaTimeSerializers](val format: DateTimeFormatter) extends CustomSerializer[LocalTime](_ => ( {
    case JString(s) => format.parse(s, asQuery(LocalTime.from))
  }, {
    case t: LocalTime => JString(format.format(t))
  }
  ))

  class LocalDateTimeSerializer private[JavaTimeSerializers](val format: DateTimeFormatter) extends CustomSerializer[LocalDateTime](_ => ( {
    case JString(s) => format.parse(s, asQuery(LocalDateTime.from))
  }, {
    case t: LocalDateTime => JString(format.format(t))
  }
  ))

  class LocalDateSerializer private[JavaTimeSerializers](val format: DateTimeFormatter) extends CustomSerializer[LocalDate](_ => ( {
    case JString(s) => format.parse(s, asQuery(LocalDate.from))
  }, {
    case d: LocalDate => JString(format.format(d))
  }
  ))

  object LocalTimeSerializer extends LocalTimeSerializer(DateTimeFormatter.ISO_LOCAL_TIME)

  object LocalDateSerializer extends LocalDateSerializer(DateTimeFormatter.ISO_LOCAL_DATE)

  object LocalDateTimeSerializer extends LocalDateTimeSerializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
}