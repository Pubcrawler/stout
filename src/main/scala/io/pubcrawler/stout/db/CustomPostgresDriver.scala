package io.pubcrawler.stout.db

import com.github.tminglei.slickpg._

trait CustomPostgresDriver extends ExPostgresProfile with PgDate2Support {

  override val api = CustomAPI
  object CustomAPI extends API with DateTimeImplicits

}

object CustomPostgresDriver extends CustomPostgresDriver