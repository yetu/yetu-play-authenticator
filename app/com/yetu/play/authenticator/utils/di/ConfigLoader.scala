package com.yetu.play.authenticator.utils.di
import play.api.Play
import play.api.Play.current

object ConfigLoader {


  lazy val environmentUrl = Play.configuration.getString("application.environmentUrl").get // "-dev.yetu.me"
  lazy val authorizationUrlPrefix = Play.configuration.getString("silhouette.yetu.authBaseUrl").get // https://auth
  lazy val authorizationBaseUrl = authorizationUrlPrefix + environmentUrl // https://auth-dev.yetu.me

  object AuthServer {
    val profileUrl = authorizationBaseUrl + Play.configuration.getString("silhouette.yetu.profileURL").get
    val logoutURL = authorizationBaseUrl + Play.configuration.getString("silhouette.yetu.logoutURL").get
  }

  val singleSignOut = Play.configuration.getBoolean("silhouette.yetu.singleSignOut")
  val onLogoutGoToIfNoSingleSignOut = Play.configuration.getString("silhouette.yetu.onLogoutGoToIfNoSingleSignOut").get
  val onLoginGoTo = Play.configuration.getString("silhouette.yetu.onLoginGoTo").get

  val onLogoutGoTo : String = singleSignOut.getOrElse(false) match {
    case true => AuthServer.logoutURL
    case false => onLogoutGoToIfNoSingleSignOut
  }






}