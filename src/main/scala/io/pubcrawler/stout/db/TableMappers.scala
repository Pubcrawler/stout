package io.pubcrawler.stout.db

import java.sql.{Date, Timestamp}
import java.time.{LocalDate, LocalDateTime, ZoneOffset}

import slick.jdbc.PostgresProfile.api._

trait TableMappers {

  implicit val zonedDateTimeMapper = MappedColumnType.base[LocalDateTime, Timestamp](
    ldt => Timestamp.from(ldt.toInstant(ZoneOffset.UTC)),
    t => LocalDateTime.from(t.toLocalDateTime)
  )

  implicit val localDateMapper = MappedColumnType.base[LocalDate, Date](
    ld => Date.valueOf(ld),
    d => LocalDate.from(d.toInstant)
  )
}
