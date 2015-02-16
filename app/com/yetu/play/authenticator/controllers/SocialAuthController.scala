package com.yetu.play.authenticator.controllers

import javax.inject.Inject

import com.mohiva.play.silhouette.api._
import com.mohiva.play.silhouette.api.exceptions.AuthenticationException
import com.mohiva.play.silhouette.api.services.AuthInfoService
import com.mohiva.play.silhouette.impl.authenticators.SessionAuthenticator
import com.mohiva.play.silhouette.impl.providers._
import com.yetu.play.authenticator.models.User
import com.yetu.play.authenticator.models.services.UserService
import play.api.libs.concurrent.Execution.Implicits._
import play.api.mvc.Action
import com.yetu.play.authenticator.utils.di.{YetuProfileBuilder, ConfigLoader}

import scala.concurrent.Future

/**
 * The social auth controller.
 *
 * @param env The Silhouette environment.
 */
class SocialAuthController @Inject() (
  val env: Environment[User, SessionAuthenticator],
  val userService: UserService,
  val authInfoService: AuthInfoService) extends Silhouette[User, SessionAuthenticator] {

  /**
   * Authenticates a user against a social provider.
   *
   * @param provider The ID of the provider to authenticate against.
   * @return The result to display.
   */
  def authenticate(provider: String) = Action.async { implicit request =>
    (env.providers.get(provider) match {
      case Some(p: SocialProvider with YetuProfileBuilder) =>
        p.authenticate().flatMap {
          case Left(result) => Future.successful(result)
          case Right(authInfo) => for {
            profile <- p.retrieveProfile(authInfo)
            user <- userService.save(profile)
            authInfo <- authInfoService.save(profile.loginInfo, authInfo)
            authenticator <- env.authenticatorService.create(user.loginInfo)
            value <- env.authenticatorService.init(authenticator)
            result <- env.authenticatorService.embed(value, Future.successful(
            //TODO: parametrise the redirect location?
              Redirect(ConfigLoader.indexUrl)
            ))
          } yield {
            env.eventBus.publish(LoginEvent(user, request, request2lang))
            result
          }
        }
      case _ => Future.failed(new AuthenticationException(s"Cannot authenticate with unexpected social provider $provider"))
    }).recoverWith(exceptionHandler)
  }
}
