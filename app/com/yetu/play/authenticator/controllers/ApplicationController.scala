package com.yetu.play.authenticator.controllers

import javax.inject.Inject

import com.mohiva.play.silhouette.api.{Environment, LogoutEvent, Silhouette}
import com.mohiva.play.silhouette.impl.authenticators.SessionAuthenticator
import com.yetu.play.authenticator.models.User
import com.yetu.play.authenticator.utils.di.ConfigLoader

import scala.concurrent.Future


/**
 * The basic application controller.
 *
 * @param env The Silhouette environment.
 */
class ApplicationController @Inject()(implicit val env: Environment[User, SessionAuthenticator])
  extends Silhouette[User, SessionAuthenticator] {


  /**
   * Handles the Sign Out action.
   * @return The result to display.
   */
  def signOut = SecuredAction.async { implicit request =>
    //TODO: parametrize this redirect link or get rid of logout functionality?
    val result = Future.successful(Redirect(ConfigLoader.indexUrl))
    env.eventBus.publish(LogoutEvent(request.identity, request, request2lang))

    request.authenticator.discard(result)
  }

  def hello = SecuredAction {
    Ok("hello")
  }
}



