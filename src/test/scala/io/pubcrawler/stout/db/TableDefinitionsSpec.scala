package io.pubcrawler.stout.db


import com.typesafe.scalalogging.LazyLogging
import io.pubcrawler.stout.util.JsonFormat
import org.json4s.jackson.JsonMethods.parse
import org.scalatest.{FlatSpec, Matchers}
import slick.dbio.DBIO
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.io.{Codec, Source}


class TableDefinitionsSpec extends FlatSpec with LazyLogging with TableDefinitions with DbConnection with Matchers with JsonFormat {

  it should "add users" in {
    val action = DBIO.seq(
      users ++= userData
    )
    try {
      Await.result(db.run(action), 1.second)
    } catch {
      case e: Throwable => logger.error("Adding users to database failed", e)
    }
  }

  it should "add stops" in {
    val action = DBIO.seq(
      stops ++= stopData
    )
    try {
      Await.result(db.run(action), 1.second)
    } catch {
      case e: Throwable => logger.error("Adding stops to database failed", e)
    }
  }

  it should "add routes" in {
    val action = DBIO.seq(
      _routes ++= routeData
    )
    try {
      Await.result(db.run(action), 1.second)
    } catch {
      case e: Throwable => logger.error("Adding routes to database failed", e)
    }
  }

  it should "add crawls" in {
    val action = DBIO.seq(
      crawls ++= crawlData
    )
    try {
      Await.result(db.run(action), 1.second)
    } catch {
      case e: Throwable => logger.error("Adding crawls to database failed", e)
    }
  }

  it should "add crawlparticipants" in {
    val action = DBIO.seq(
      crawlParticipants ++= crawlParticipantsData
    )
    try {
      Await.result(db.run(action), 1.second)
    } catch {
      case e: Throwable => logger.error("Adding crawl participants to database failed", e)
    }
  }

  it should "add routestops" in {
    val action = DBIO.seq(
      routeStops ++= routeStopData
    )
    try {
      Await.result(db.run(action), 1.second)
    } catch {
      case e: Throwable => logger.error("Adding routestops to database failed", e)
    }
  }

  it should "add wishes" in {
    val action = DBIO.seq(
      wishes ++= wishData
    )
    try {
      Await.result(db.run(action), 1.second)
    } catch {
      case e: Throwable => logger.error("Adding wishes to database failed", e)
    }
  }

  def userData: Seq[User] = createJsonFromResource[User]("data/users.json")

  def stopData: Seq[Stop] = createJsonFromResource[Stop]("data/stops.json")

  def routeData: Seq[Route] = createJsonFromResource[Route]("data/routes.json")

  def crawlData: Seq[Crawl] = createJsonFromResource[Crawl]("data/crawls.json")

  def crawlParticipantsData: Seq[CrawlParticipant] = createJsonFromResource[CrawlParticipant]("data/crawlparticipants.json")

  def routeStopData: Seq[RouteStop] = createJsonFromResource[RouteStop]("data/routestops.json")

  def wishData: Seq[Wish] = createJsonFromResource[Wish]("data/wishes.json")

  def createJsonFromResource[T](resource: String)(implicit m: Manifest[T]): Seq[T] = {
    val raw = Source.fromResource(resource)(Codec.UTF8).getLines().mkString
    parse(raw).extract[Seq[T]]
  }

}
