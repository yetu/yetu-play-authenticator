package com.yetu.play.authenticator.controllers

import javax.inject.Inject

import com.mohiva.play.silhouette.api.{LoginInfo, Environment, LogoutEvent, Silhouette}
import com.mohiva.play.silhouette.impl.authenticators.SessionAuthenticator
import com.mohiva.play.silhouette.impl.providers.OAuth2Info
import com.yetu.play.authenticator.models.User
import com.yetu.play.authenticator.models.daos.{UserDAOImpl, UserDAO, OAuth2InfoDAO}
import com.yetu.play.authenticator.utils.di.ConfigLoader
import play.api.mvc.Action

import scala.collection.mutable
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

  def apiLogout = Action.async { request =>
    import scala.concurrent.ExecutionContext.Implicits.global

    val accessToken = request.getQueryString("access_token").get

    // Does this make sense? This could delete token in the backend part of the app even though
    // we cannot access the env.authenticator to discard the result (as that requires telling the client that the cookie is not valid)
    // so the app will feel broken, as on the one hand there is no more access token,
    // but on the other hand the user still has a valid cookie.
    //
    val result = for {
      info: Option[LoginInfo] <- oauth2Dao.findByAccessToken(accessToken)
      user: Option[User] <- userDao.find(loginInfo = info.get)

    // delete access token from oauth2Dao or delete user from userDao for this user
    } yield ()

    OAuth2InfoDAO.data = mutable.HashMap()
    UserDAOImpl.users = mutable.HashMap()

    result.map(_ => NoContent)
  }

  def hello = SecuredAction {
    Ok("hello")
  }
}



