package io.pubcrawler.stout.controllers

import akka.util.Timeout
import io.pubcrawler.stout.db.{TableDefinitions, User}
import io.pubcrawler.stout.models.Result
import io.pubcrawler.stout.util.{JsonFormat, UtilAsync}
import org.scalatra.json._
import org.scalatra.swagger.{Swagger, SwaggerSupport}
import org.scalatra.{FutureSupport, ScalatraServlet}
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Future
import scala.concurrent.duration._


class ProfileController(implicit val db: Database, implicit val swagger: Swagger) extends ScalatraServlet
  with JacksonJsonSupport with FutureSupport with SwaggerSupport with TableDefinitions with JsonFormat with UtilAsync {
  protected implicit val timeout = new Timeout(2.seconds)

  private val ifNotFoundMessage: String = "user not found"
  override protected def applicationDescription: String = "Stout Profile API"

  before() {
    contentType = formats("json")
  }

  val getUser = apiOperation[User]("getUser")
    .parameters(pathParam[String]("username").description("username for user to lookup"))
    .summary("gets user by given username")

  get("/:username", operation(getUser)) {
    val result: Future[Option[User]] = db.run(users.filter(_.username === params("username")).result.headOption)
    resultOption(result, ifNotFoundMessage)
  }

  val getUserByEmail = apiOperation[User]("getUserByEmail")
    .parameters(pathParam[String]("email").description("email for user"))
    .summary("gets user with given email")

  get("/email/:email", operation(getUserByEmail)) {
    val result: Future[Option[User]] = db.run(users.filter(_.email === params("email")).result.headOption)
    resultOption(result)
  }

  notFound {
    Result(404, "not a valid endpoint")
  }

}
