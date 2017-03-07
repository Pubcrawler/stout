package io.pubcrawler.stout.util

import java.time.{LocalDate, LocalDateTime}
import java.sql.Timestamp
import java.sql.Date

import slick.jdbc.PostgresProfile.api._

object DateUtils {
  implicit def dateTimeColumnType = MappedColumnType.base[LocalDateTime, Timestamp](
    ldt => Timestamp.valueOf(ldt),
    ts => ts.toLocalDateTime
  )

  implicit def optionalDateColumnType = MappedColumnType.base[Option[LocalDate], Option[Date]](
    {
      case Some(ld) => Some(Date.valueOf(ld))
      case None => None
    },
    {
      case Some(dt) => Some(dt.toLocalDate)
      case None => None
    }
  )
}
