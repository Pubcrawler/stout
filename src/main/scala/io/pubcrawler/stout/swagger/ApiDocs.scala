package io.pubcrawler.stout.swagger

import org.scalatra.ScalatraServlet
import org.scalatra.swagger.{JacksonSwaggerBase, Swagger}


class ApiDocs(implicit val swagger: Swagger) extends ScalatraServlet with JacksonSwaggerBase

