package io.pubcrawler.stout.db

import slick.jdbc.JdbcBackend.Database


trait DbConnection {
  implicit val db: Database = Database.forConfig("postgres")
}
