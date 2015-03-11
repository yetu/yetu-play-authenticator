package com.yetu.play.authenticator.controllers

import java.util.NoSuchElementException
import javax.inject.Inject

import com.mohiva.play.silhouette.api.{Environment, LoginInfo, LogoutEvent, Silhouette}
import com.mohiva.play.silhouette.impl.authenticators.SessionAuthenticator
import com.yetu.play.authenticator.models.User
import com.yetu.play.authenticator.models.daos.{OAuth2InfoDAO, UserDAO}
import com.yetu.play.authenticator.utils.di.ConfigLoader
import play.api.mvc.{Action, Result}

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

    request.getQueryString("access_token") match {
      case Some(accessToken) => {
        val result = for {
          info: Option[LoginInfo] <- oauth2Dao.findByAccessToken(accessToken)
          user: Option[User] <- userDao.find(loginInfo = info.get)
          removeUser <- userDao.remove(user.get.userUUID)
          removeAuthInfo <- oauth2Dao.remove(info.get)
        } yield removeAuthInfo
        result.map(_ => NoContent).recover(withErrorHandling)
      }
      case _ => Future.successful(NotFound("no access token"))
    }
  }

  /**
   * *
   * @return
   */
  def withErrorHandling: PartialFunction[Throwable, Result] = {
    case x: NoSuchElementException => NotFound("no access token")
    // any unhandled case here will result in play's default 500 or 404 error
  }


  def hello = SecuredAction {
    Ok("hello")
  }
}



