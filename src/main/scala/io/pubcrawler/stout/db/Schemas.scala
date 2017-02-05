package io.pubcrawler.stout.db

import slick.jdbc.PostgresProfile.api._
import java.sql.Date
import java.sql.Time


class Stop(tag: Tag) extends Table[(Int, String, String, Float, Float)](tag, "STOP") {
  def id = column[Int]("STOP_ID", O.PrimaryKey)
  def title = column[String]("TITLE")
  def address = column[String]("ADDRESS")
  def lat = column[Float]("LAT")
  def lng = column[Float]("LNG")
  def * = (id, title, address, lat, lng)
}

class Route(tag: Tag) extends Table[(Int, Int)](tag, "ROUTE") {
  def id = column[Int]("ROUTE_ID", O.PrimaryKey)
  def ownerId = column[Int]("OWNER_ID")
  def * = (id, ownerId)
}

class RouteStop(tag: Tag, stops: TableQuery[Stop], routes: TableQuery[Route]) extends Table[(Int, Int, Int)](tag, "ROUTE_STOP") {
  def routeId = column[Int]("ROUTE_STOP_ID")
  def stopId = column[Int]("STOP_ID")
  def order = column[Int]("ORDER")
  def * = (routeId, stopId, order)

  def stop = foreignKey("STOP_FK", stopId, stops)(_.id)
  def route = foreignKey("ROUTE_FK", routeId, routes)(_.id)
}

class Crawl(tag: Tag, stops: TableQuery[Stop]) extends Table[(Int, String, Int, Time, Float, Float, Int, String, String)](tag, "CRAWL") {
  def id = column[Int]("CRAWL_ID", O.PrimaryKey)
  def title = column[String]("TITLE")
  def ownerId = column[Int]("OWNER_ID")
  def dateTime = column[Time]("DATE_TIME")
  def lat = column[Float]("LAT")
  def lng = column[Float]("LNG")
  def radius = column[Int]("RADIUS")
  def city = column[String]("CITY")
  def description = column[String]("DESCRIPTION")
  def * = (id, title, ownerId, dateTime, lat, lng, radius, city, description)
}

class CrawlParticipants(tag: Tag) extends Table[(Int, Int, Boolean)](tag, "CRAWL_PARTICIPANTS") {
  def crawlId = column[Int]("CRAWL_ID")
  def userId = column[Int]("USER_ID")
  def status = column[Boolean]("STATUS")
  def * = (crawlId, userId, status)
}

class User(tag: Tag, facebookUsers: TableQuery[Stop]) extends Table[(Int, String, Date, String, Int)](tag, "USER") {
  def id = column[Int]("USER_ID")
  def username = column[String]("USERNAME")
  def birthdate = column[Date]("BIRTHDATE")
  def gender = column[String]("GENDER")
  def facebookUserId = column[Int]("FACEBOOK_ID")
  def * = (id, username, birthdate, gender, facebookUserId)

  def facebookUser = foreignKey("FACEBOOK_USER_FK", facebookUserId, facebookUsers)(_.id)
}

class FacebookUser(tag: Tag) extends Table[(Int, String, Date, String, String, Int)](tag, "FACEBOOK_USER") {
  def id = column[Int]("FACEBOOK_USER_ID", O.PrimaryKey)
  def name = column[String]("NAME")
  def birthdate = column[Date]("BIRTHDATE")
  def location = column[String]("LOCATION")
  def gender = column[String]("GENDER")
  def facebookId = column[Int]("FACEBOOK_ID")
  def * = (id, name, birthdate, location, gender, facebookId)
}

class Wishes(tag: Tag, stops: TableQuery[Stop], users: TableQuery[User]) extends Table[(Int, Int, Int)](tag, "WISHES") {
  def id = column[Int]("WISHES", O.PrimaryKey)
  def userId = column[Int]("USER_ID")
  def stopId = column[Int]("STOP_ID")
  def * = (id, userId, stopId)

  def user = foreignKey("USER_FK", userId, users)(_.id)
  def stop = foreignKey("STOP_FK", stopId, stops)(_.id)
}
