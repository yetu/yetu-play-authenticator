package com.yetu.play.authenticator.utils.di
import play.api.Play
import play.api.Play.current

object ConfigLoader {

  object AuthServer {
    val profileUrl = Play.configuration.getString("silhouette.yetu.profileURL").get
    val logoutURL = Play.configuration.getString("silhouette.yetu.logoutURL").get
  }

  val singleSignOut = Play.configuration.getBoolean("silhouette.yetu.singleSignOut")
  val onLogoutGoToIfNoSingleSignOut = Play.configuration.getString("silhouette.yetu.onLogoutGoToIfNoSingleSignOut").get
  val onLoginGoTo = Play.configuration.getString("silhouette.yetu.onLoginGoTo").get

  val onLogoutGoTo : String = singleSignOut.getOrElse(false) match {
    case true => AuthServer.logoutURL
    case false => onLogoutGoToIfNoSingleSignOut
  }





}