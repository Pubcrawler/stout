import io.pubcrawler.stout._
import org.scalatra._
import javax.servlet.ServletContext

import com.typesafe.scalalogging.LazyLogging

class ScalatraBootstrap extends LifeCycle with LazyLogging {
  override def init(context: ServletContext) {
    context.mount(new PubcrawlController, "/*")
    logger info "Stout started"
  }
}
