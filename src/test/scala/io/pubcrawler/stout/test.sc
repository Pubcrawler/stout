import java.time.LocalDate

import io.pubcrawler.stout.db.{Gender, Stop, User}
import org.json4s.DefaultFormats
import org.json4s.jackson.JsonMethods.parse
import org.json4s.jackson.Serialization

val json = "{\"title\":\"Doyle Group\",\"address\":\"920 Leroy Plaza\",\"city\":\"Costeira do Pirajubae\",\"lat\":\"-27.63586\",\"lng\":\"-48.5212\"}"
implicit val format = DefaultFormats
val a = Stop(Some(12),"Hello", "Hello", "Hello", 432.1213,124.123123)
val b = Serialization.write(a)


val c = User(Some(12), "jonski", Some(LocalDate.now()), Gender.M, "jonski@gmail.com", 123190)
val d = Serialization.write(c)

parse(json)