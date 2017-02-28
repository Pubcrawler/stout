package io.pubcrawler.stout.controllers

import akka.actor.ActorSystem
import akka.util.Timeout
import io.pubcrawler.stout.db.{Gender, TableDefinitions, User}
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.json._
import org.scalatra.{FutureSupport, ScalatraServlet}
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.concurrent.duration._
import scala.util.{Failure, Success}
import io.pubcrawler.stout.models.Result


class ProfileController(db: Database, system: ActorSystem) extends ScalatraServlet
  with JacksonJsonSupport with FutureSupport with TableDefinitions {
  protected implicit val timeout = new Timeout(2 seconds)
  protected implicit val jsonFormats: Formats = DefaultFormats
  protected implicit def executor: ExecutionContext = system.dispatcher

  before() {
    contentType = formats("json")
  }

  get("/:username") {
    val result: Future[Option[User]] = db.run(users.filter(_.email === params("username")).result.headOption)
    result onComplete {
      case Success(a) => a match {
        case Some(user) => user
        case None =>
          """
            |{
            |  status: 404
            |  message: "user not found"
            |}
          """.stripMargin
      }
      case Failure(e) => {
        log("exception happened while fetching user", e)
        e.getMessage
      }
    }
  }

  get("/random") {
    val user = User(None,"Kari Nordmann", null, Gender.M, "kari@online.no", 7564)
    log("random")
    Result(200, user)
  }

  notFound {
    Result(404, "not valid endpoint")
  }

}
