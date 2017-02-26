package io.pubcrawler.stout.db

import com.typesafe.config.ConfigFactory
import slick.jdbc.JdbcBackend.Database


trait DbConnection {
  var config = ConfigFactory.load()
  val db = Database.forConfig("postgres")
}
