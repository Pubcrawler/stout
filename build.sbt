import org.scalatra.sbt._
import org.scalatra.sbt.PluginKeys._
import ScalateKeys._

val ScalatraVersion = "2.5.0"
val Json4sVersion = "3.5.0"
val AkkaVersion = "2.4.17"

ScalatraPlugin.scalatraSettings

flywayUrl := sys.env.getOrElse("DB_URL", default = "DB_URL_NOT_SPECIFIED")
flywayUser := sys.env.getOrElse("DB_USER", default = "DB_USER_NOT_SPECIFIED")
flywayPassword := sys.env.getOrElse("DB_PASS", default = "DB_PASS_NOT_SPECIFIED")

scalateSettings

organization := "io.pubcrawler"

name := "Stout"

version := "0.0.1-SNAPSHOT"

scalaVersion := "2.12.1"

resolvers += Classpaths.typesafeReleases
resolvers += Resolver.jcenterRepo

libraryDependencies ++= Seq(
  "org.flywaydb" % "flyway-core" % "4.1.1",
  "org.postgresql" % "postgresql" % "9.3-1100-jdbc4",
  "org.scalatra" %% "scalatra" % ScalatraVersion,
  "org.scalatra" %% "scalatra-scalate" % ScalatraVersion,
  "org.scalatra" %% "scalatra-json" % ScalatraVersion,
  "org.json4s"   %% "json4s-jackson" % Json4sVersion,
  "org.scalactic" %% "scalactic" % "3.0.1",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test",
  "ch.qos.logback" % "logback-classic" % "1.2.1" % "runtime",
  "com.spotify" % "docker-client" % "8.0.0",
  "com.typesafe.akka" %% "akka-actor" % AkkaVersion,
  "com.typesafe" % "config" % "1.3.1",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0",
  "com.typesafe.slick" %% "slick" % "3.2.0-M2",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.2.0-M2",
  "org.eclipse.jetty" % "jetty-webapp" % "9.2.15.v20160210" % "container",
  "javax.servlet" % "javax.servlet-api" % "3.1.0" % "provided",
  "org.eclipse.jetty" % "jetty-webapp" % "8.1.8.v20121106" % "container;compile",
  "org.eclipse.jetty" % "jetty-plus" % "9.1.5.v20140505" % "compile;container"
)

// For debugging in IntelliJ IDEA
javaOptions ++= Seq(
  "-Xdebug",
  "-Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"
)

scalateTemplateConfig in Compile := {
  val base = (sourceDirectory in Compile).value
  Seq(
    TemplateConfig(
      base / "webapp" / "WEB-INF" / "templates",
      Seq.empty,  /* default imports should be added here */
      Seq(
        Binding("context", "_root_.org.scalatra.scalate.ScalatraRenderContext", importMembers = true, isImplicit = true)
      ),  /* add extra bindings here */
      Some("templates")
    )
  )
}

enablePlugins(JettyPlugin, JavaAppPackaging)
