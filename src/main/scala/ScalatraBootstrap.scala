import javax.servlet.ServletContext

import akka.actor.ActorSystem
import com.typesafe.scalalogging.LazyLogging
import io.pubcrawler.stout.controllers.{CrawlController, ProfileController, StoutSwagger, SwaggerController}
import io.pubcrawler.stout.db.DbConnection
import org.scalatra._

class ScalatraBootstrap extends LifeCycle with LazyLogging with DbConnection {
  val system = ActorSystem()

  implicit val swagger = new StoutSwagger

  override def init(context: ServletContext) {
    context.mount(new ProfileController, "/profile/*")
    context.mount(new CrawlController, "/crawl/*")
    context.mount(new SwaggerController, "/docs")

    logger info "Stout started"
  }

  override def destroy(context: ServletContext): Unit = {
    super.destroy(context)
    system.terminate()
    db.close()
  }
}
