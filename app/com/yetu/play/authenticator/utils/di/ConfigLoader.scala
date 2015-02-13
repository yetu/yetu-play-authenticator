package com.yetu.play.authenticator.utils.di

import com.typesafe.config.ConfigFactory

object ConfigLoader {

  private val config = ConfigFactory.load()

  object AuthServer {
    val profileUrl = config.getString("silhouette.yetu.profileURL")
  }

  val indexUrl = "/"


}