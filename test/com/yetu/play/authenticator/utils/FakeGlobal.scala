package com.yetu.play.authenticator.utils

import java.util.UUID
import com.google.inject.{AbstractModule, Guice}
import com.google.inject.util.Modules
import com.mohiva.play.silhouette.api.{LoginInfo, Environment}
import com.mohiva.play.silhouette.impl.authenticators.SessionAuthenticator
import com.mohiva.play.silhouette.impl.providers.OAuth2Info
import com.mohiva.play.silhouette.test.FakeEnvironment
import com.yetu.play.authenticator.AuthenticatorGlobal
import com.yetu.play.authenticator.controllers.{ApplicationController, SocialAuthController}
import com.yetu.play.authenticator.models.User
import com.yetu.play.authenticator.models.daos.{UserDAOImpl, OAuth2InfoDAO}
import net.codingwell.scalaguice.ScalaModule
import com.yetu.play.authenticator.utils.di.SilhouetteModule

import FakeGlobal._
import play.api.mvc.RequestHeader
import play.api.mvc.Handler

/**
 * Provides a fake global to override the Guice injector.
 */
class FakeGlobal extends AuthenticatorGlobal {

  /**
   * Overrides the Guice injector.
   */
  override val injector = Guice.createInjector(Modules.`override`(new SilhouetteModule).`with`(new FakeModule))
  val oauth2InfoDao = injector.getInstance[OAuth2InfoDAO](classOf[OAuth2InfoDAO])
  val userDao  = injector.getInstance[UserDAOImpl](classOf[UserDAOImpl])
  oauth2InfoDao.save(FakeGlobal.identity.loginInfo, FakeGlobal.oauth2Info)
  userDao.save(identity)

  /**
   * A fake Guice module.
   */
  class FakeModule extends AbstractModule with ScalaModule {
    def configure() = {
      bind[Environment[User, SessionAuthenticator]].toInstance(env)
    }
  }

  val socialAuthController = injector.getInstance[SocialAuthController](classOf[SocialAuthController])
  val applicationController = injector.getInstance[ApplicationController](classOf[ApplicationController])

  override def onRouteRequest(req: RequestHeader): Option[Handler] = {
    (req.method, req.path) match {

      case ("GET",  FakeGlobal.routeHelloTest)     => Some(applicationController.hello)
      case ("GET",  FakeGlobal.routeSignOut)       => Some(applicationController.signOut)
      case ("GET",  FakeGlobal.routeAuthenticate)  => Some(socialAuthController.authenticate("yetu"))
      case ("POST", FakeGlobal.routeApiLogout)    => Some(applicationController.apiLogout)
      case _ => None
    }
  }

}

object FakeGlobal {


  /**
   * An identity.
   */
  val identity = User(
    userUUID = UUID.randomUUID(),
    loginInfo = LoginInfo("provider", "user@user.com"),
    firstName = None,
    lastName = None,
    fullName = None,
    email = None,
    avatarURL = None
  )

  val oauth2Info = OAuth2Info("random_access_token")

  val routeHelloTest = "/helloTestRoute"
  val routeSignOut = "/signOut"
  val routeAuthenticate = "/authenticate/yetu"
  val routeApiLogout = "/api/logout"

  /**
   * A Silhouette fake environment.
   */
  implicit val env = FakeEnvironment[User, SessionAuthenticator](Seq(identity.loginInfo -> identity))



}