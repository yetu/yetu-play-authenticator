package com.yetu.play.authenticator
package models.daos

import java.util.concurrent.ConcurrentHashMap

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.impl.daos.DelegableAuthInfoDAO
import com.mohiva.play.silhouette.impl.providers.OAuth2Info
import com.yetu.play.authenticator.models.daos.OAuth2InfoDAO._

import scala.collection.JavaConverters._
import scala.collection._
import scala.concurrent.Future
import scala.language.postfixOps

/**
 * The DAO to store the OAuth2 information.
 */
class OAuth2InfoDAO extends DelegableAuthInfoDAO[OAuth2Info] {

  /**
   * Saves the OAuth2 info.
   *
   * @param loginInfo The login info for which the auth info should be saved.
   * @param authInfo The OAuth2 info to save.
   * @return The saved OAuth2 info or None if the OAuth2 info couldn't be saved.
   */
  def save(loginInfo: LoginInfo, authInfo: OAuth2Info): Future[OAuth2Info] = {
    data += (loginInfo -> authInfo)
    Future.successful(authInfo)
  }

  /**
   * Finds the OAuth2 info which is linked with the specified login info.
   *
   * @param loginInfo The linked login info.
   * @return The retrieved OAuth2 info or None if no OAuth2 info could be retrieved for the given login info.
   */
  def find(loginInfo: LoginInfo): Future[Option[OAuth2Info]] = {
    Future.successful(data.get(loginInfo))
  }


  /**
   * Removes the user from the given data store
   * @param loginInfo
   * @return
   */
  def remove(loginInfo: LoginInfo): Future[Unit] = {
    data -= loginInfo
    Future.successful(Unit)
  }


  def findByAccessToken(accessToken: String): Future[Option[LoginInfo]] = {
    Future.successful(data.find(x => x._2.accessToken == accessToken).map(_._1))
  }

}

/**
 * The companion object.
 */
object OAuth2InfoDAO {

  /**
   * The data store for the OAuth2 info.
   */
  var data: concurrent.Map[LoginInfo, OAuth2Info] = new ConcurrentHashMap[LoginInfo, OAuth2Info] asScala

}
