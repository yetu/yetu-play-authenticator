package com.yetu.play.authenticator.models

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.impl.providers.SocialProfile

case class YetuSocialProfile(
                                loginInfo: LoginInfo,
                                userUUID: String,
                                firstName: Option[String] = None,
                                lastName: Option[String] = None,
                                fullName: Option[String] = None,
                                email: Option[String] = None,
                                avatarURL: Option[String] = None) extends SocialProfile
