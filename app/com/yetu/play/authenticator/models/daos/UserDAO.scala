package com.yetu.play.authenticator.models.daos

import java.util.UUID

import com.mohiva.play.silhouette.api.LoginInfo
import com.yetu.play.authenticator.models.User

import scala.concurrent.Future

/**
 * Give access to the user object.
 */
trait UserDAO {

  /**
   * Finds a user by its login info.
   *
   * @param loginInfo The login info of the user to find.
   * @return The found user or None if no user for the given login info could be found.
   */
  def find(loginInfo: LoginInfo): Future[Option[User]]

  /**
   * Finds a user by its user ID.
   *
   * @param userUUID The ID of the user to find.
   * @return The found user or None if no user for the given ID could be found.
   */
  def find(userUUID: UUID): Future[Option[User]]

  /**
   * Removes the user from the given data store
   * @param userUUID
   * @return
   */
  def remove(userUUID: UUID): Future[Unit]

  /**
   * Saves a user.
   *
   * @param user The user to save.
   * @return The saved user.
   */
  def save(user: User): Future[User]
}
