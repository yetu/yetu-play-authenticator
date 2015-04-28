package com.yetu.play.authenticator

import com.google.inject.Guice
import com.mohiva.play.silhouette.api.{Logger, SecuredSettings}
import com.yetu.play.authenticator.controllers.routes
import com.yetu.play.authenticator.utils.di.{SilhouetteModule, YetuProvider}
import play.api.GlobalSettings
import play.api.i18n.Lang
import play.api.mvc.Results._
import play.api.mvc.{RequestHeader, Result}

import com.yetu.play.authenticator.controllers._

import scala.concurrent.Future

/*
 * The global object.
 */
object AuthenticatorGlobal extends AuthenticatorGlobal

/*
 * The global configuration.
 */
trait AuthenticatorGlobal extends GlobalSettings with SecuredSettings with Logger {

  /*
   * The Guice dependencies injector.
   */
  val injector = Guice.createInjector(new SilhouetteModule)

  /*
   * Loads the controller classes with the Guice injector,
   * in order to be able to inject dependencies directly into the controller.
   *
   * @param controllerClass The controller class to instantiate.
   * @return The instance of the controller class.
   * @throws Exception if the controller couldn't be instantiated.
   */
  override def getControllerInstance[A](controllerClass: Class[A]) = injector.getInstance(controllerClass)

  /*
   * Called when a user is not authenticated.
   *
   * As defined by RFC 2616, the status code of the response should be 401 Unauthorized.
   *
   * @param request The request header.
   * @param lang The currently selected language.
   * @return The result to send to the client.
   */
   override def onNotAuthenticated(request: RequestHeader, lang: Lang): Option[Future[Result]] = {
    println("LIBRARY ON NOT AUTHENTICATED CALLED")

    Some(Future.successful(Redirect(routes.SocialAuthController.authenticate(YetuProvider.Yetu))))
  }


}
