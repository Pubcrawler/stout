package io.pubcrawler.stout.controllers

import akka.actor.ActorSystem
import akka.util.Timeout
import io.pubcrawler.stout.db.{Gender, TableDefinitions, User}
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.json._
import org.scalatra.{AsyncResult, FutureSupport, ScalatraServlet}
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.concurrent.duration._
import scala.util.{Failure, Success, Try}
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
    val result: Future[Option[User]] = db.run(users.filter(_.username === params("username")).result.headOption)
    async(result)
  }

  get("/email/:email") {
    val result: Future[Option[User]] = db.run(users.filter(_.email === params("email")).result.headOption)
    async(result)
  }

  notFound {
    Result(404, "not valid endpoint")
  }

  def async(result: Future[Option[User]]): AsyncResult = {
    val prom = Promise[Result]()
    result onComplete {
      case Success(a) => a match {
        case Some(data) => prom.complete(Try(Result(200, data)))
        case None => prom.complete(Try(Result(404, "user not found")))
      }
      case Failure(e) => {
        log(e.getMessage, e)
        prom.complete(Try(Result(500, "something wrong happen")))
      }
    }
    new AsyncResult() {
      override val is: Future[Result] = prom.future
    }
  }

}
