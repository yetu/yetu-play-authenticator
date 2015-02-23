package com.yetu.play.authenticator.controllers

import play.api.mvc.{Action, Controller}


//Usage in routes:
// ## Cors Access-Control-Allow-Headers
// OPTIONS        /*all    com.yetu.play.authenticator.controllers.CrossOrigin.preflight(all: String)
object CrossOrigin extends Controller {

  def preflight(all: String) = Action {
    NoContent.withHeaders("Access-Control-Allow-Origin" -> "*",
      "Allow" -> "*",
      "Access-Control-Allow-Methods" -> "POST, GET, PUT, DELETE, OPTIONS",
      "Access-Control-Allow-Headers" -> "Origin, X-Requested-With, Content-Type, Accept, Referer, User-Agent, Accept-Encoding")
  }

}
