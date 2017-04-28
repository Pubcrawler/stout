package io.pubcrawler.stout.controllers

import akka.util.Timeout
import io.pubcrawler.stout.db.{Gender, TableDefinitions, User}
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
case class SignupCredentials(username: Option[String], email: Option[String], password: Option[String], passwordConfirmation: Option[String])

class JWTController(db: Database) extends ScalatraServlet
  with JacksonJsonSupport with FutureSupport with TableDefinitions with JsonFormat with UtilAsync with JWTUtil {
  protected implicit val timeout = new Timeout(2.seconds)

  before() {
    contentType = formats("json")
  }

  post("/signup") {
    val json = parse(request.body)
    val credentials = json.extract[SignupCredentials]
    credentials match {
      case SignupCredentials(Some(username), Some(email), Some(passwd), Some(passwdC)) => {
        val result: Future[Option[User]] = db.run(users.filter(_.username === username).result.headOption)
        result.map(
          _.map(
            u => noAccess("User already exists")
          ).getOrElse(
            if(passwd == passwdC) {
              val insertQuery = users returning users.map(_.id) into ((user, id) => user.copy(id = Some(id)))
              val insertUser = insertQuery += User(None, username, BCrypt.hashpw(passwd, BCrypt.gensalt()), None, Gender.O, email, -1)
              db.run(insertUser).map {
                case res: User => Result(201, "User created: " + res.username)
                case _ => Result(500, "Server error")
              }
            } else {
              noAccess("Password confirmation invalid")
            }
          )
        )
      }
      case _ => Result(404, "not valid endpoint")
    }
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

  def noAccess(reason: String): Result = {
    Result(403, reason)
  }

  notFound {
    Result(404, "not valid endpoint")
  }

}
