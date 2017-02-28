package io.pubcrawler.stout.db

import slick.jdbc.JdbcBackend.Database


trait DbConnection {
  val db: Database = Database.forConfig("postgres")
}
