import javax.servlet.ServletContext

import akka.actor.ActorSystem
import com.typesafe.scalalogging.LazyLogging
import io.pubcrawler.stout.controllers.{CrawlController, ProfileController}
import io.pubcrawler.stout.db.DbConnection
import io.pubcrawler.stout.swagger.{ApiDocs, StoutSwagger}
import org.scalatra._

class ScalatraBootstrap extends LifeCycle with LazyLogging with DbConnection {
  val system = ActorSystem()

  implicit val swagger = new StoutSwagger

  override def init(context: ServletContext) {
//    context mount(new StoutController, "/*")
    context.mount(new ProfileController(db, swagger), "/profile/*")
    context.mount(new CrawlController(db, swagger), "/crawl/*")
    context.mount(new ApiDocs(), "/docs")
    logger info "Stout started"
  }

  override def destroy(context: ServletContext): Unit = {
    super.destroy(context)
    system.terminate()
    db.close()
  }
}
