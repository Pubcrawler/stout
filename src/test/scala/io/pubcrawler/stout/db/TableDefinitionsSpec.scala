package io.pubcrawler.stout.db


import io.pubcrawler.stout.util.JsonSupport
import org.json4s.jackson.JsonMethods._
import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}
import slick.dbio.DBIO
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.io.Source



class TableDefinitionsSpec extends FlatSpec with TableDefinitions with DbConnection with Matchers with JsonSupport with BeforeAndAfter {

  it should "add users" in {
    val action = DBIO.seq(
      users ++= userData
    )
    try {
      Await.result(db.run(action), 1.second)
    }
  }

  it should "add stops" in {
    val action = DBIO.seq(
      stops ++= stopData
    )
    try {
      Await.result(db.run(action), 1.second)
    }
  }

  it should "add routes" in {
    val action = DBIO.seq(
      _routes ++= routeData
    )
    try {
      Await.result(db.run(action), 1.second)
    }
  }

  it should "add crawls" in {
    val action = DBIO.seq(
      crawls ++= crawlData
    )
    try {
      Await.result(db.run(action), 1.second)
    }
  }

  it should "add crawlparticipants" in {
    val action = DBIO.seq(
      crawlParticipants ++= crawlParticipantsData
    )
    try {
      Await.result(db.run(action), 1.second)
    }
  }

  it should "add routestops" in {
    val action = DBIO.seq(
      routeStops ++= routeStopData
    )
    try {
      Await.result(db.run(action), 1.second)
    }
  }

  it should "add wishes" in {
    val action = DBIO.seq(
      wishes ++= wishData
    )
    try {
      Await.result(db.run(action), 1.second)
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
    val raw = Source.fromResource(resource).getLines().mkString
    parse(raw).extract[Seq[T]]
  }

}
