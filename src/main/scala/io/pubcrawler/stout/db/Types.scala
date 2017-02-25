package io.pubcrawler.stout.db

import java.sql.{Date, Time}
import slick.jdbc.PostgresProfile.api._


object Gender extends Enumeration {
  type Gender = Value
  val Male = Value('M')
  val Female = Value('F')
  val Other = Value('O')

  implicit val genderMapper = MappedColumnType.base[Gender.Gender, String](e => e.toString, s => Gender.withName(s))
}

object Status extends Enumeration {
  type Status = Value
  val Accepted = Value('A')
  val Maybe = Value('M')
  val Declined = Value('D')

  implicit val statusMapper = MappedColumnType.base[Status.Status, String](e => e.toString, s => Status.withName(s))
}

case class Crawl(id: Option[Int], title: String, ownerId: Int, dateTime: Time, lat: Float, lng: Float, radius: Int, city: String, description: String)

case class CrawlParticipant(crawlId: Int, userId: Int, status: Status.Status)

case class Route(id: Option[Int], ownerId: Int)

case class RouteStop(routeId: Int, stopId: Int, order: Int)

case class Stop(id: Option[Int], title: String, address: String, lat: Float, lng: Float)

case class User(id: Option[Int], username: String, birthdate: Date, gender: Gender.Gender, facebookUserId: Int)

case class Wish(id: Option[Int], userId: Int, stopId: Int)
