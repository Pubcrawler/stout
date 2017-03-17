package io.pubcrawler.stout.db

import java.time.{LocalDate, LocalDateTime}


object Gender extends Enumeration {
  type Gender = Value
  val M = Value("M") // Male
  val F = Value("F") // Female
  val O = Value("O") // Other
}

object Status extends Enumeration {
  type Status = Value
  val A = Value('A') // Accepted
  val M = Value('M') // Maybe
  val D = Value('D') // Declined
}

case class User(id: Option[Int], username: String, birthdate: Option[LocalDate], gender: Gender.Gender, email: String, facebookId: Int)

case class Route(id: Option[Int], ownerId: Int)

case class Crawl(id: Option[Int], title: String, ownerId: Int, routeId: Int, dateTime: LocalDateTime, address: String, city: String, lat: Double, lng: Double, radius: Float, description: String)

case class CrawlParticipant(crawlId: Int, userId: Int, status: Status.Status)

case class RouteStop(routeId: Int, stopId: Int, order: Int)

case class Stop(id: Option[Int], title: String, address: String, city: String, lat: Double, lng: Double)

case class Wish(userId: Int, stopId: Int)
