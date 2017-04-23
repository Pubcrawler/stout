package io.pubcrawler.stout.controllers

import akka.util.Timeout
import com.typesafe.scalalogging.LazyLogging
import io.pubcrawler.stout.db.{TableDefinitions, User}
import io.pubcrawler.stout.models.Result
import io.pubcrawler.stout.util.{JWTUtil, JsonFormat, UtilAsync}
import org.scalatra.{FutureSupport, ScalatraServlet}
import org.scalatra.json.JacksonJsonSupport
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._
import org.json4s._
import org.mindrot.jbcrypt.BCrypt

import scala.concurrent.Future
import scala.concurrent.duration._

case class Credentials (username: Option[String], password: Option[String])

class JWTController(db: Database) extends ScalatraServlet
  with JacksonJsonSupport with FutureSupport with TableDefinitions with JsonFormat with UtilAsync with LazyLogging with JWTUtil {
  protected implicit val timeout = new Timeout(2.seconds)

  before() {
    contentType = formats("json")
  }

  post("/login") {
    val json = parse(request.body)
    val authenticationCredentials = json.extract[Credentials]
    authenticationCredentials match {
      case Credentials(Some(username), Some(password)) => {
        val result: Future[Option[User]] = db.run(users.filter(_.username === username).result.headOption)
        result.map(
          _.map(u => authenticate(u, username, password)).getOrElse(Result(401, "Invalid user name or password")))
      }
      case _ => Result(404, "not valid endpoint")
    }

  }

  def authenticate(u: User, username: String, password: String): Result = {
    if(u.username == username && BCrypt.checkpw(password, u.password)) {
      Result(200, createJWTToken(u))
    } else {
      Result(401, "Invalid user name or password")
    }
  }

  def noAccess() {
    Result(403, "No access")
  }

  notFound {
    Result(404, "not valid endpoint")
  }

}
