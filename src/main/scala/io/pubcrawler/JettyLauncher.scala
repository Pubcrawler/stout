package io.pubcrawler
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.{DefaultServlet, ServletContextHandler}
import org.eclipse.jetty.webapp.WebAppContext
import org.scalatra.servlet.ScalatraListener


object JettyLauncher {

  private val DefaultPort = 8080

  def main(args: Array[String]) {
    val port = Option(System.getProperty("http.port")).map(_.toInt).getOrElse(DefaultPort)

    val server = new Server(port)
    val context = new WebAppContext()
    context.setContextPath("/")
    context.setResourceBase("src/main/webapp")
    context.setEventListeners(Array(new ScalatraListener))

    server.setHandler(context)

    server.start
    server.join
  }
}
