package io.pubcrawler.stout.controllers

import akka.util.Timeout
import io.pubcrawler.stout.db.{Crawl, TableDefinitions}
import io.pubcrawler.stout.models.{Participant, Result}
import io.pubcrawler.stout.util.{JsonFormat, UtilAsync}
import org.scalatra.json.JacksonJsonSupport
import org.scalatra.swagger.{Swagger, SwaggerSupport}
import org.scalatra.{FutureSupport, ScalatraServlet}
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Future
import scala.concurrent.duration._


class CrawlController(implicit val db: Database, implicit val swagger: Swagger) extends ScalatraServlet
  with JacksonJsonSupport with FutureSupport with TableDefinitions with JsonFormat with UtilAsync with SwaggerSupport {

  protected implicit val timeout = new Timeout(2.seconds)

  override protected def applicationDescription: String = "Stout Crawl API"

  private val ifNotFoundMessage: String = "crawl not found"

  before() {
    contentType = formats("json")
  }

  val getCrawl = apiOperation[Crawl]("getCrawl")
    .parameters(pathParam[Int]("crawlId").description("id for crawl"))
    .summary("gets a crawl by given id")

  get("/:crawlId", operation(getCrawl)) {
    val result: Future[Option[Crawl]] = db.run(crawls.filter(_.id === params("crawlId").toInt).result.headOption)
    resultOption(result, ifNotFoundMessage)
  }

  val getCrawlParticipants = apiOperation[Participant]("getParticipants")
    .parameters(pathParam[Int]("crawlId").description("id for crawl"))
    .summary("gets all participants of a crawl")

  get("/:crawlId/participants", operation(getCrawlParticipants)) {
    val query = for {
      c <- crawls if c.id === params("crawlId").toInt
      p <- crawlParticipants if c.id === p.crawlId
      u <- users if p.userId === u.id
    } yield (u.username, p.status, p.userId)

    val resultList: Future[Seq[(String, Char, Int)]] = db.run(query.result)
    val result = resultList.map(_.map(Participant tupled _))
    resultSeq(result)
  }

  notFound {
    Result(404, "not a valid endpoint")
  }

}
