package com.yetu.play.authenticator.filters

import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait AllowAllCorsFilter extends CorsFilterHelper {

  override def apply(next: (RequestHeader) => Future[Result])(requestHeader: RequestHeader): Future[Result] = {

    next(requestHeader).map(result => result.withHeaders(corsHeaders: _*))
  }
}


/** usage in Global.scala:
  *
  * import play.api.mvc.EssentialAction
  * import com.yetu.play.authenticator.filters.AllowAllCorsFilter
  * override def doFilter(action: EssentialAction) = AllowAllCorsFilter(action)
  */
object AllowAllCorsFilter extends AllowAllCorsFilter
