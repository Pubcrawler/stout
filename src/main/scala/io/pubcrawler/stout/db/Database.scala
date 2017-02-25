package io.pubcrawler.stout.db

import com.typesafe.config.ConfigFactory
import slick.basic.DatabaseConfig


trait Database {
  var config = ConfigFactory.load()
  lazy val db = DatabaseConfig.forConfig("postgres", config)
}
