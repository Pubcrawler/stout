package io.pubcrawler.stout.controllers

import org.scalatra.ScalatraServlet
import org.scalatra.swagger.{ApiInfo, JacksonSwaggerBase, Swagger}

class SwaggerController(implicit val swagger: Swagger) extends ScalatraServlet with JacksonSwaggerBase

object StoutApiInfo extends ApiInfo(
  title = "The Stout API",
  description = "Swagger docs for the Stout API",
  termsOfServiceUrl = "https://github.com/Pubcrawler",
  contact = "apiteam@pubcrawler.io",
  license = "license",
  licenseUrl = "licenseUrl"
)

class StoutSwagger extends Swagger(Swagger.SpecVersion, "1.0.0", StoutApiInfo)
