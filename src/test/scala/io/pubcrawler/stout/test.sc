import java.time.{LocalDate, LocalDateTime}

import io.pubcrawler.stout.db.{Crawl, Gender, Stop, User}
import org.json4s.DefaultFormats
import org.json4s.jackson.JsonMethods.parse
import org.json4s.jackson.Serialization

val json = "{\"title\":\"Doyle Group\",\"address\":\"920 Leroy Plaza\",\"city\":\"Costeira do Pirajubae\",\"lat\":\"-27.63586\",\"lng\":\"-48.5212\"}"
implicit val format = DefaultFormats
val a = Stop(Some(12),"Hello", "Hello", "Hello", 432.1213,124.123123)
val b = Serialization.write(a)


val c = User(Some(12), "jonski", Some(LocalDate.now()), Gender.M, "jonski@gmail.com", 123190)
val d = Serialization.write(c)

val crawl = Crawl(Some(12), "HelloWorld", 120, 12, LocalDateTime.now(), "Fredensborgveien 11B",
  "Oslo", 123.128133, 123.120312, 0.5F, "Ipsum")

Serialization.write(crawl)

parse(json)