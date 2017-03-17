package io.pubcrawler.stout.models

import io.pubcrawler.stout.db.Status

case class Participant(username: String, status: Status.Status, userId: Int)
