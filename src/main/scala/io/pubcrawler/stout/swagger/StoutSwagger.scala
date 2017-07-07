package io.pubcrawler.stout.swagger

import org.scalatra.swagger.{ApiInfo, Swagger}

object StoutApiInfo extends ApiInfo(
  title = "Stout RESTful API",
  description = "Swagger documentation for Stout REST API",
  termsOfServiceUrl = "http://localhost:8080",
  contact = "apiteam@pubcrawler.io",
  license = "license",
  licenseUrl = "licenseUrl"
)

class StoutSwagger extends Swagger(Swagger.SpecVersion, "0.0.1", StoutApiInfo)

