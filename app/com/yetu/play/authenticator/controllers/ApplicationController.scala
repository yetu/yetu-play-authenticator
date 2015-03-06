package com.yetu.play.authenticator.controllers

import javax.inject.Inject

import com.mohiva.play.silhouette.api.{LoginInfo, Environment, LogoutEvent, Silhouette}
import com.mohiva.play.silhouette.impl.authenticators.SessionAuthenticator
import com.mohiva.play.silhouette.impl.providers.OAuth2Info
import com.yetu.play.authenticator.models.User
import com.yetu.play.authenticator.models.daos.{UserDAOImpl, UserDAO, OAuth2InfoDAO}
import com.yetu.play.authenticator.utils.di.ConfigLoader
import play.api.mvc.Action

import scala.concurrent.Future


/**
 * The basic application controller.
 *
 * @param env The Silhouette environment.
 */
class ApplicationController @Inject()(userDao: UserDAO)(implicit val env: Environment[User, SessionAuthenticator], oauth2Dao: OAuth2InfoDAO)
  extends Silhouette[User, SessionAuthenticator] {


  /**
   * Handles the Sign Out action.
   * @return The result to display.
   */
  def signOut = SecuredAction.async { implicit request =>
    val result = Future.successful(Redirect(ConfigLoader.onLogoutGoTo))
    env.eventBus.publish(LogoutEvent(request.identity, request, request2lang))

    request.authenticator.discard(result)
  }

  def hello = SecuredAction {
    Ok("hello")
  }
}



