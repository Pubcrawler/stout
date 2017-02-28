import javax.servlet.ServletContext

import akka.actor.ActorSystem
import com.typesafe.scalalogging.LazyLogging
import io.pubcrawler.stout.controllers.ProfileController
import io.pubcrawler.stout.db.DbConnection
import org.scalatra._

class ScalatraBootstrap extends LifeCycle with LazyLogging with DbConnection {
  val system = ActorSystem()

  override def init(context: ServletContext) {
//    context mount(new StoutController, "/*")
    context mount(new ProfileController(db, system), "/profile/*")
    logger info "Stout started"
  }

  override def destroy(context: ServletContext): Unit = {
    super.destroy(context)
    system.terminate()
    db.close()
  }
}
