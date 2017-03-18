package io.pubcrawler.stout.controllers

import akka.util.Timeout
import io.pubcrawler.stout.db.{TableDefinitions, User}
import io.pubcrawler.stout.models.Result
import io.pubcrawler.stout.util.{JsonFormat, UtilAsync}
import org.scalatra.json._
import org.scalatra.swagger.{Api, Swagger, SwaggerEngine, SwaggerSupport}
import org.scalatra.{FutureSupport, ScalatraServlet}
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Future
import scala.concurrent.duration._


class ProfileController(db: Database, implicit val swagger: Swagger) extends ScalatraServlet
  with JacksonJsonSupport with FutureSupport with ProfileControllerSwaggerDef with TableDefinitions with JsonFormat with UtilAsync {
  protected implicit val timeout = new Timeout(2.seconds)

  private val ifNotFoundMessage: String = "user not found"

  before() {
    contentType = formats("json")
  }

  get("/:username", operation(getUser)) {
    val result: Future[Option[User]] = db.run(users.filter(_.username === params("username")).result.headOption)
    resultOption(result, ifNotFoundMessage)
  }

  get("/email/:email", operation(getUserByEmail)) {
    val result: Future[Option[User]] = db.run(users.filter(_.email === params("email")).result.headOption)
    resultOption(result)
  }

  notFound {
    Result(404, "not valid endpoint")
  }

}

trait ProfileControllerSwaggerDef extends SwaggerSupport {
  protected val applicationDescription = "Profile RESTful API"

  val getUser = apiOperation[User]("getUser")
    .parameter(queryParam[String]("username").description("username for user to lookup"))
    .summary("gets user by given username")

  val getUserByEmail = apiOperation[User]("getUserByEmail")
    .parameter(queryParam[String]("email").description("email for user"))
    .summary("gets user with given email")
}
