package io.pubcrawler.stout.db

import java.sql.{Date, Time}
import slick.jdbc.PostgresProfile.api._



object Gender extends Enumeration {
  type Gender = Value
  val M = Value("M") // Male
  val F = Value("F") // Female
  val O = Value("O") // Other

  implicit val genderMapper = MappedColumnType.base[Gender.Gender, String](e => e.toString, s => Gender.withName(s))
}

object Status extends Enumeration {
  type Status = Value
  val A = Value('A') // Accepted
  val M = Value('M') // Maybe
  val D = Value('D') // Declined

  implicit val statusMapper = MappedColumnType.base[Status.Status, String](e => e.toString, s => Status.withName(s))
}

case class User(id: Option[Int], username: String, birthdate: Option[Date], gender: Gender.Gender, email: String, facebookUserId: Int)

case class Route(id: Option[Int], ownerId: Int)

case class Crawl(id: Option[Int], title: String, ownerId: Int, routeId: Int, dateTime: Time, address: String, city: String, lat: Double, lng: Double, radius: Float, description: String)

case class CrawlParticipant(crawlId: Int, userId: Int, status: Status.Status)

case class RouteStop(routeId: Int, stopId: Int, order: Int)

case class Stop(id: Option[Int], title: String, address: String, city: String, lat: Double, lng: Double)

case class Wish(userId: Int, stopId: Int)
