package io.pubcrawler.stout.db

import slick.jdbc.PostgresProfile.api._
import java.sql.Date
import java.sql.Time


class Crawls(tag: Tag) extends Table[Crawl](tag, "crawl") {
  def id = column[Int]("crawl_id", O.PrimaryKey, O.AutoInc)
  def title = column[String]("title")
  def ownerId = column[Int]("owner_id")
  def dateTime = column[Time]("date_time")
  def lat = column[Float]("lat")
  def lng = column[Float]("lng")
  def radius = column[Int]("radius")
  def city = column[String]("city")
  def description = column[String]("description")
  def * = (id.?, title, ownerId, dateTime, lat, lng, radius, city, description) <> (Crawl.tupled, Crawl.unapply)
}

class CrawlParticipants(tag: Tag) extends Table[CrawlParticipant](tag, "crawl_participants") {
  def crawlId = column[Int]("crawl_id")
  def userId = column[Int]("user_id")
  def status = column[Status.Status]("status")
  def * = (crawlId, userId, status) <> (CrawlParticipant.tupled, CrawlParticipant.unapply)
}

class Routes(tag: Tag) extends Table[Route](tag, "route") {
  def id = column[Int]("route_id", O.PrimaryKey, O.AutoInc)
  def ownerId = column[Int]("owner_id")
  def * = (id.?, ownerId) <> (Route.tupled, Route.unapply)
}

class RouteStops(tag: Tag, stops: TableQuery[Stops], routes: TableQuery[Routes]) extends Table[RouteStop](tag, "route_stop") {
  def routeId = column[Int]("route_stop_id")
  def stopId = column[Int]("stop_id")
  def order = column[Int]("order")
  def * = (routeId, stopId, order) <> (RouteStop.tupled, RouteStop.unapply)

  def stop = foreignKey("stop_fk", stopId, stops)(_.id)
  def route = foreignKey("route_fk", routeId, routes)(_.id)
}

class Stops(tag: Tag) extends Table[Stop](tag, "stop") {
  def id = column[Int]("stop_id", O.PrimaryKey, O.AutoInc)
  def title = column[String]("title")
  def address = column[String]("address")
  def lat = column[Float]("lat")
  def lng = column[Float]("lng")
  def * = (id.?, title, address, lat, lng) <> (Stop.tupled, Stop.unapply)
}

class Users(tag: Tag, facebookUsers: TableQuery[Stops]) extends Table[User](tag, "users") {
  def id = column[Int]("user_id", O.PrimaryKey, O.AutoInc)
  def username = column[String]("username")
  def birthdate = column[Date]("birthdate")
  def gender = column[Gender.Gender]("gender")
  def facebookUserId = column[Int]("facebook_id")
  def * = (id.?, username, birthdate, gender, facebookUserId) <> (User.tupled, User.unapply)

  def facebookUser = foreignKey("facebook_user_fk", facebookUserId, facebookUsers)(_.id)
}

class Wishes(tag: Tag, stops: TableQuery[Stops], users: TableQuery[Users]) extends Table[Wish](tag, "wishes") {
  def id = column[Int]("wish_id", O.PrimaryKey, O.AutoInc)
  def userId = column[Int]("user_id")
  def stopId = column[Int]("stop_id")
  def * = (id.?, userId, stopId) <> (Wish.tupled, Wish.unapply)

  def user = foreignKey("user_fk", userId, users)(_.id)
  def stop = foreignKey("stop_fk", stopId, stops)(_.id)
}
