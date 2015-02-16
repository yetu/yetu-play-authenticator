package com.yetu.play.authenticator
package models.daos

import java.util.UUID

import com.mohiva.play.silhouette.api.LoginInfo

import scala.collection.mutable
import scala.concurrent.Future

import com.yetu.play.authenticator.models.User
import com.yetu.play.authenticator.models.daos.UserDAOImpl._

/**
 * Give access to the user object.
 */
class UserDAOImpl extends UserDAO {

  /**
   * Finds a user by its login info.
   *
   * @param loginInfo The login info of the user to find.
   * @return The found user or None if no user for the given login info could be found.
   */
  def find(loginInfo: LoginInfo) = Future.successful(
    users.find { case (id, user) => user.loginInfo == loginInfo }.map(_._2)
  )

  /**
   * Finds a user by its user ID.
   *
   * @param userUUID The ID of the user to find.
   * @return The found user or None if no user for the given ID could be found.
   */
  def find(userUUID: String) = Future.successful(users.get(userUUID))

  /**
   * Saves a user.
   *
   * @param user The user to save.
   * @return The saved user.
   */
  def save(user: User) = {
    users += (user.userUUID -> user)
    Future.successful(user)
  }
}

/**
 * The companion object.
 */
object UserDAOImpl {

  /**
   * The list of users.
   */
  val users: mutable.HashMap[String, User] = mutable.HashMap()
}
