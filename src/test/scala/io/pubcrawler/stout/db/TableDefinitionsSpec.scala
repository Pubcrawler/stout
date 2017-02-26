package io.pubcrawler.stout.db


import slick.jdbc.PostgresProfile.api._
import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}
import slick.dbio.DBIO

import scala.concurrent.Await
import scala.concurrent.duration.Duration



class TableDefinitionsSpec extends FlatSpec with TableDefinitions with DbConnection with Matchers with BeforeAndAfter {

  it should "add users" in {
    val action = DBIO.seq(
      users ++= userData
    )
    try {
      Await.result(db.run(action), Duration.Inf)
    } finally db.close()
  }

  def userData: Seq[User] = Seq(
    User(None,"Sue Birtwistle", null, Gender.F, "debby@aol.com", 1234),
    User(None,"Felicity Gibson", null, Gender.F, "felicity@aol.com", 4321),
    User(None,"Ola Nordmann", null, Gender.M, "ola@nordmann.no", 5432),
    User(None,"Kari Nordmann", null, Gender.M, "kari@online.no", 7564)
  )

}
