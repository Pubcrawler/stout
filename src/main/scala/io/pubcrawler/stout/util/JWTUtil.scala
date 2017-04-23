package io.pubcrawler.stout.util

import java.time.LocalDateTime

import org.json4s.JsonAST.JObject
import io.pubcrawler.stout.db.User
import pdi.jwt.{JwtJson4s, JwtAlgorithm}
import org.json4s.JsonDSL.WithBigDecimal._

trait JWTUtil {

  val JwtSecret = "secret"
  val HashAlgorithm = JwtAlgorithm.HS256

  def createJWTToken(user: User) : String = {
    val header = JObject(("typ", "JWT"), ("alg", "HS256"))
    val claim = JObject(
      ("iss", "localhost"),
      ("sub", user.id),
      ("iat", LocalDateTime.now().toString),
      ("exp", LocalDateTime.now().plusHours(1).toString)
    )
    JwtJson4s.encode(header, claim, JwtSecret)
  }
}

object JWTUtil
