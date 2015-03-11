package com.yetu.play.authenticator
package models.daos

import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

import com.mohiva.play.silhouette.api.LoginInfo
import com.yetu.play.authenticator.models.User
import com.yetu.play.authenticator.models.daos.UserDAOImpl._

import scala.collection.JavaConverters._
import scala.collection.concurrent
import scala.concurrent.Future
import scala.language.postfixOps

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
    users.find { case (id, user) => user.loginInfo == loginInfo}.map(_._2)
  )

  /**
   * Finds a user by its user ID.
   *
   * @param userUUID The ID of the user to find.
   * @return The found user or None if no user for the given ID could be found.
   */
  def find(userUUID: UUID) = Future.successful(users.get(userUUID))

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

  /**
   * Removes the user from the given data store
   * @param userUUID
   * @return
   */
  override def remove(userUUID: UUID): Future[Unit] = {
    users -= userUUID
    Future.successful(Unit)
  }
}

/**
 * The companion object.
 */
object UserDAOImpl {

  /**
   * The list of users.
   */
  var users: concurrent.Map[UUID, User] = new ConcurrentHashMap[UUID, User] asScala
}
