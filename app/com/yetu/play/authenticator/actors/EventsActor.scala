package com.yetu.play.authenticator.actors

import java.util.UUID

import akka.actor.{Actor, ActorLogging, Props}
import com.yetu.notification.client.messages.MsgDeliver
import com.yetu.play.authenticator.messages.{UsedDeletedMsg, InvalidMessage, LogoutEventMsg, EventNotMatchedMsg}
import com.yetu.play.authenticator.models.daos.UserDAO
import play.api.data.validation.ValidationError
import play.api.libs.json.{Json, _}

import scala.util.Try


// We might implement logout stuff with actor communication
class EventsActor(userDAO: UserDAO) extends Actor with ActorLogging {

  def receive: Receive = {
    case MsgDeliver(rawMessage) =>
      Try(Json.parse(rawMessage)) map { json =>
        json.\("event") match {
          case JsString(LogoutEventMsg.event) =>
            checkMessage[LogoutEventMsg](json, classOf[LogoutEventMsg], LogoutEventMsg.logout_event_msg)
          case JsUndefined() =>
            sender ! EventNotMatchedMsg
            log.warning(s"Received unexpected message type")
          case e =>
            log.warning(s"Received unexpected message type")
        }
      } recover {
        case e =>
          log.error(s"Failed wot parse event message: $rawMessage - error $e")
          sender ! InvalidMessage(e)
      }
    case LogoutEventMsg(userId) =>
      userDAO.remove(UUID.fromString(userId))
      log.info(s"user with $userId was removed from store")
      sender ! UsedDeletedMsg
  }

  def checkMessage[T](json: JsValue, clazz: Class[T], format: Format[T]) = {
    implicit val f = format
    Json.fromJson(json) match {
      case JsSuccess(logoutMessage, path) =>
        log.info(s"Got logout message $logoutMessage")
        self ! logoutMessage
      case JsError(e: Seq[(JsPath, Seq[ValidationError])]) =>
        log.error(s"Failed wot parse event message: $e - error $e")
        sender ! EventNotMatchedMsg
    }
  }


}

object EventsActor {
  def props(userDAO: UserDAO) = Props(new EventsActor(userDAO))
}
