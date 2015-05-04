package com.yetu.play.authenticator.actors

import akka.actor.Actor.Receive
import akka.actor.{Props, ActorLogging, Actor}
import com.yetu.notification.client.messages.MsgDeliver
import play.api.libs.json.Json


// We might implement logout stuff with actor communication
class EventsActor extends Actor with ActorLogging  {
  def receive: Receive = {
    case MsgDeliver(msg) => Json.parse(msg) match {
      case
    }
  }
}

object EventsActor {
  def props() = Props(new EventsActor)



}
