package io.pubcrawler.stout.controllers

import math._
import akka.util.Timeout
import io.pubcrawler.stout.db.{Stop, TableDefinitions}
import io.pubcrawler.stout.models.{Participant, Result}
import io.pubcrawler.stout.util.{JsonFormat, UtilAsync}
import org.scalatra.json.JacksonJsonSupport
import org.scalatra.swagger.{Swagger, SwaggerSupport}
import org.scalatra.{FutureSupport, ScalatraServlet}
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Future
import scala.concurrent.duration._


class StopController(implicit val db: Database, implicit val swagger: Swagger) extends ScalatraServlet
  with JacksonJsonSupport with FutureSupport with TableDefinitions with JsonFormat with UtilAsync with SwaggerSupport {

  protected implicit val timeout = new Timeout(2.seconds)

  override protected def applicationDescription: String = "Stout Stop API"

  private val R: Double = 6378.137 // earth radius in km
  private val b: Double = 0.00892857142 // absolute maximum distance in lat or lng pr km

  private val ifNotFoundMessage: String = "stop not found"

  before() {
    contentType = formats("json")
  }

  val getStop = apiOperation[Stop]("getStop")
    .parameters(pathParam[Int]("stopId").description("id for stop"))
    .summary("gets a stop by given id")

  get("/:stopId", operation(getStop)) {
    val result: Future[Option[Stop]] = db.run(stops.filter(_.id === params("stopId").toInt).result.headOption)
    resultOption(result, ifNotFoundMessage)
  }

  val getStopsWithinRadius = apiOperation[Participant]("getStopWithinRadius")
    .parameters(
      pathParam[Double]("radius").description("radius for the search given in km"),
      pathParam[Double]("lat").description("position in latitude"),
      pathParam[Double]("lng").description("position in longitude")
    )
    .summary("gets all stops within radius of given position")

  get("/within/radius", operation(getStopsWithinRadius)) {
    val radius: Double = params("radius").toDouble
    val lat: Double = params("lat").toDouble
    val lng: Double = params("lng").toDouble
    val bound: Double = radius * b

    val query = for {
      s <- stops if s.lat.between(lat - bound, lat + bound) && s.lng.between(lng - bound, lng + bound)
    } yield s

    val resultList: Future[Seq[Stop]] = db.run(query.result)
    val result = resultList.map(_.filter(s => haversine(lat, lng, s.lat, s.lng) <= radius))
    resultSeq(result)
  }

  def haversine(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Double = {
    val dlat = (lat2 - lat1).toRadians
    val dlng = (lng2 - lng1).toRadians

    val a = pow(sin(dlat/2.0),2.0) + pow(sin(dlng/2.0),2.0) * cos(lat1.toRadians) * cos(lat2.toRadians)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    R * c
  }

  notFound {
    Result(404, "not a valid endpoint")
  }

}
