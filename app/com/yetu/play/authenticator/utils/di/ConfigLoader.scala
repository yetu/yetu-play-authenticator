package com.yetu.play.authenticator.utils.di

import com.typesafe.config.ConfigFactory
import play.api.Play
import play.api.Play.current
import com.yetu.typesafeconfigextentension.ConfigExtension

object ConfigLoader extends ConfigExtension {

  val config = ConfigFactory.load().substitutePropertyValues("application.environmentUrl")

  object AuthServer {
    val profileUrl = config.getString("silhouette.yetu.profileURL")
    val logoutURL = config.getString("silhouette.yetu.logoutURL")
  }

  val singleSignOut = Play.configuration.getBoolean("silhouette.yetu.singleSignOut")
  val onLogoutGoToIfNoSingleSignOut = Play.configuration.getString("silhouette.yetu.onLogoutGoToIfNoSingleSignOut").get
  val onLoginGoTo = Play.configuration.getString("silhouette.yetu.onLoginGoTo").get

  val onLogoutGoTo : String = singleSignOut.getOrElse(false) match {
    case true => AuthServer.logoutURL
    case false => onLogoutGoToIfNoSingleSignOut
  }
}