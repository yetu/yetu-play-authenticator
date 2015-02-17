package com.yetu.youtube.utils

import java.util.UUID
import com.google.inject.{AbstractModule, Guice}
import com.google.inject.util.Modules
import com.mohiva.play.silhouette.api.{LoginInfo, Environment}
import com.mohiva.play.silhouette.impl.authenticators.SessionAuthenticator
import com.mohiva.play.silhouette.test.FakeEnvironment
import com.yetu.play.authenticator.Global
import com.yetu.play.authenticator.controllers.{ApplicationController, SocialAuthController}
import com.yetu.play.authenticator.models.User
import net.codingwell.scalaguice.ScalaModule
import com.yetu.play.authenticator.utils.di.SilhouetteModule

import FakeGlobal._
import play.api.mvc.RequestHeader
import play.api.mvc.Handler

/**
 * Provides a fake global to override the Guice injector.
 */
class FakeGlobal extends Global {

  /**
   * Overrides the Guice injector.
   */
  override val injector = Guice.createInjector(Modules.`override`(new SilhouetteModule).`with`(new FakeModule))

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

      case ("GET", "/helloTestRoute") => Some(applicationController.hello)
      case ("GET", "/signOut") => Some(applicationController.signOut)
      case ("GET", "/authenticate/yetu") => Some(socialAuthController.authenticate("yetu"))
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

  /**
   * A Silhouette fake environment.
   */
  implicit val env = FakeEnvironment[User, SessionAuthenticator](Seq(identity.loginInfo -> identity))



}