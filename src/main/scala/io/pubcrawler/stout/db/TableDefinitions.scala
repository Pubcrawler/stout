package io.pubcrawler.stout.db

import java.time.{LocalDate, LocalDateTime}

import slick.jdbc.PostgresProfile.api._
import slick.lifted.TableQuery

trait TableDefinitions extends TableMappers {

  class UserTable(tag: Tag) extends Table[User](tag, "users") {
    def id = column[Int]("user_id", O.PrimaryKey, O.AutoInc)
    def username = column[String]("username")
    def birthdate = column[Option[LocalDate]]("birthdate")
    def gender = column[Gender.Gender]("gender")
    def email = column[String]("email")
    def facebookId = column[Int]("facebook_id")
    def * = (id.?, username, birthdate, gender, email, facebookId) <> (User.tupled, User.unapply)
  }

  val users: TableQuery[UserTable] = TableQuery[UserTable]

  class RouteTable(tag: Tag) extends Table[Route](tag, "routes") {
    def id = column[Int]("route_id", O.PrimaryKey, O.AutoInc)
    def ownerId = column[Int]("owner_id")
    def * = (id.?, ownerId) <> (Route.tupled, Route.unapply)
  }

  val _routes: TableQuery[RouteTable] = TableQuery[RouteTable]

  class CrawlTable(tag: Tag) extends Table[Crawl](tag, "crawls") {
    def id = column[Int]("crawl_id", O.PrimaryKey, O.AutoInc)
    def title = column[String]("title")
    def ownerId = column[Int]("owner_id")
    def routeId = column[Int]("route_id")
    def dateTime = column[LocalDateTime]("date_time")
    def address = column[String]("address")
    def city = column[String]("city")
    def lat = column[Double]("lat")
    def lng = column[Double]("lng")
    def radius = column[Float]("radius")
    def description = column[String]("description")
    def * = (id.?, title, ownerId, routeId, dateTime, address, city, lat, lng, radius, description) <> (Crawl.tupled, Crawl.unapply)

    def owner = foreignKey("crawls_owner_id_fkey", ownerId, users)(_.id)
    def route = foreignKey("crawls_route_id_fkey", routeId, _routes)(_.id)
  }

  val crawls: TableQuery[CrawlTable] = TableQuery[CrawlTable]

  class CrawlParticipantTable(tag: Tag) extends Table[CrawlParticipant](tag, "crawl_participants") {
    def crawlId = column[Int]("crawl_id")
    def userId = column[Int]("user_id")
    def status = column[Status.Status]("status")
    def * = (crawlId, userId, status) <> (CrawlParticipant.tupled, CrawlParticipant.unapply)

    def crawl = foreignKey("crawl_participants_crawl_id_fkey", crawlId, crawls)(_.id)
    def user = foreignKey("crawl_participants_user_id_fkey", userId, users)(_.id)
  }

  val crawlParticipants: TableQuery[CrawlParticipantTable] = TableQuery[CrawlParticipantTable]

  class StopTable(tag: Tag) extends Table[Stop](tag, "stops") {
    def id = column[Int]("stop_id", O.PrimaryKey, O.AutoInc)
    def title = column[String]("title")
    def address = column[String]("address")
    def city = column[String]("city")
    def lat = column[Double]("lat")
    def lng = column[Double]("lng")
    def * = (id.?, title, address, city, lat, lng) <> (Stop.tupled, Stop.unapply)
  }

  val stops: TableQuery[StopTable] = TableQuery[StopTable]

  class RouteStopTable(tag: Tag) extends Table[RouteStop](tag, "route_stops") {
    def routeId = column[Int]("route_id")
    def stopId = column[Int]("stop_id")
    def order = column[Int]("order")
    def * = (routeId, stopId, order) <> (RouteStop.tupled, RouteStop.unapply)

    def route = foreignKey("route_stops_route_id_fkey", routeId, _routes)(_.id)
    def stop = foreignKey("route_stops_stop_id_fkey", stopId, stops)(_.id)
  }

  val routeStops: TableQuery[RouteStopTable] = TableQuery[RouteStopTable]

  class WishTable(tag: Tag) extends Table[Wish](tag, "wishes") {
    def userId = column[Int]("user_id")
    def stopId = column[Int]("stop_id")
    def * = (userId, stopId) <> (Wish.tupled, Wish.unapply)

    def stop = foreignKey("wishes_stop_id_fkey", stopId, stops)(_.id)
    def user = foreignKey("wishes_user_id_fkey", userId, users)(_.id)
  }

  val wishes: TableQuery[WishTable] = TableQuery[WishTable]
}
