package io.pubcrawler.stout.db

import java.time.{LocalDate, LocalDateTime}

/**
  * @param id
  * @param username
  * @param birthdate
  * @param gender Gender can be one of the following:
  *               F - Female
  *               M - Male
  *               O - Other
  * @param email
  * @param facebookId
  */
case class User(id: Option[Int], username: String, birthdate: Option[LocalDate], gender: Char, email: String, facebookId: Int)

case class Route(id: Option[Int], ownerId: Int)

case class Crawl(id: Option[Int], title: String, ownerId: Int, routeId: Int, dateTime: LocalDateTime, address: String, city: String, lat: Double, lng: Double, radius: Float, description: String)

/**
  * @param crawlId
  * @param userId
  * @param status Status can be one of the following:
  *               A - Accepted
  *               D - Declined
  *               M - Maybe
  */
case class CrawlParticipant(crawlId: Int, userId: Int, status: Char)

case class RouteStop(routeId: Int, stopId: Int, order: Int)

case class Stop(id: Option[Int], title: String, address: String, city: String, lat: Double, lng: Double)

case class Wish(userId: Int, stopId: Int)
