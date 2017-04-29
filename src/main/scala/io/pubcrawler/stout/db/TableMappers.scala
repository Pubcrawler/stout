package io.pubcrawler.stout.db

import java.sql.{Date, Timestamp}
import java.time.{LocalDate, LocalDateTime, ZoneOffset}

import slick.jdbc.PostgresProfile.api._

trait TableMappers {

  implicit val genderMapper = MappedColumnType.base[Gender.Gender, String](e => e.toString, s => Gender.withName(s))

  implicit val statusMapper = MappedColumnType.base[Status.Status, String](e => e.toString, s => Status.withName(s))

  implicit val zonedDateTimeMapper = MappedColumnType.base[LocalDateTime, Timestamp](
    ldt => Timestamp.from(ldt.toInstant(ZoneOffset.UTC)),
    t => LocalDateTime.from(t.toLocalDateTime)
  )

  implicit val localDateColumnType = MappedColumnType.base[LocalDate, Date](
    {
      localDate => Date.valueOf(localDate)
    },{
      sqlDate => sqlDate.toLocalDate
    }
  )

}
