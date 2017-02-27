package io.pubcrawler.stout.db


import slick.jdbc.PostgresProfile.api._
import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}
import slick.dbio.DBIO

import scala.concurrent.Await
import scala.concurrent.duration._



class TableDefinitionsSpec extends FlatSpec with TableDefinitions with DbConnection with Matchers with BeforeAndAfter {

  it should "add users" in {
    val action = DBIO.seq(
      users ++= userData
    )
    try {
      Await.result(db.run(action), 1.second)
    }
  }

  it should "add stops" in {
    val action = DBIO.seq(
      stops ++= stopData
    )
    try {
      Await.result(db.run(action), 1.second)
    }
  }

  def userData: Seq[User] = Seq(
    User(None,"Sue Birtwistle", null, Gender.F, "debby@aol.com", 1234),
    User(None,"Felicity Gibson", null, Gender.F, "felicity@aol.com", 4321),
    User(None,"Ola Nordmann", null, Gender.M, "ola@nordmann.no", 5432),
    User(None,"Kari Nordmann", null, Gender.M, "kari@online.no", 7564)
  )

  def stopData: Seq[Stop] = Seq(
    Stop(None,"Crowbar", "1 Hacker Way", "Menlo Park", 37.484116, -122.148244),
    Stop(None, "Do Wine Bar", "50E, Amathoundos Ave., Limassol Pearl Building, 4532 Limassol", "Cyprus", 34.707718, 33.122013)
  )

}
