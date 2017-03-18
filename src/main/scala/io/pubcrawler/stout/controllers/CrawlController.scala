package io.pubcrawler.stout.controllers

import akka.util.Timeout
import io.pubcrawler.stout.db.{Crawl, Status, TableDefinitions}
import io.pubcrawler.stout.models.{Participant, Result}
import io.pubcrawler.stout.util.{JsonFormat, UtilAsync}
import org.scalatra.json.JacksonJsonSupport
import org.scalatra.swagger.{Api, Swagger, SwaggerEngine, SwaggerSupport}
import org.scalatra.{FutureSupport, ScalatraServlet}
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Future
import scala.concurrent.duration._


class CrawlController(db: Database, implicit val swagger: Swagger) extends ScalatraServlet
  with JacksonJsonSupport with FutureSupport with CrawlControllerSwaggerDef with TableDefinitions with JsonFormat with UtilAsync {
  protected implicit val timeout = new Timeout(2.seconds)

  private val ifNotFoundMessage: String = "crawl not found"

  before() {
    contentType = formats("json")
  }

  get("/:crawlId", operation(getCrawl)) {
    val result: Future[Option[Crawl]] = db.run(crawls.filter(_.id === params("crawlId").toInt).result.headOption)
    resultOption(result, ifNotFoundMessage)
  }

  get("/:crawlId/participants", operation(getCrawlParticipants)) {
    val query = for {
      c <- crawls if c.id === params("crawlId").toInt
      p <- crawlParticipants if c.id === p.crawlId
      u <- users if p.userId === u.id
    } yield (u.username, p.status, p.userId)

    val resultList: Future[Seq[(String, Status.Status, Int)]] = db.run(query.result)
    val result = resultList.map(_.map(Participant tupled _))
    resultSeq(result)
  }

  notFound {
    Result(404, "not valid endpoint")
  }

}

trait CrawlControllerSwaggerDef extends SwaggerSupport {
  protected val applicationDescription = "Crawl RESTful API"

  val getCrawl = apiOperation[Crawl]("getCrawl")
    .parameter(queryParam[Int]("crawlId").description("id for crawl"))
    .summary("gets a crawl by given id")

  val getCrawlParticipants = apiOperation[Participant]("getParticipants").parameter(queryParam[Int]("crawlId")
    .description("id for crawl"))
    .summary("gets all participants of a crawl")
}
