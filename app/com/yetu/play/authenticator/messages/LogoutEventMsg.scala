package com.yetu.play.authenticator.messages

import play.api.data.validation.ValidationError
import play.api.libs.json.{JsPath, Json}

case class LogoutEventMsg(userId: String)

object LogoutEventMsg {

  val event = "logout"

  implicit val logout_event_msg = Json.format[LogoutEventMsg]
}


case object UsedDeletedMsg

case object EventNotMatchedMsg

case class InvalidMessage(e: Throwable)
