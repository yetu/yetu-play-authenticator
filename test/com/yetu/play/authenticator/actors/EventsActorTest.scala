package com.yetu.play.authenticator.actors

import java.util.UUID

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.testkit.TestActorRef
import akka.util.Timeout
import com.yetu.notification.client.messages.MsgDeliver
import com.yetu.play.authenticator.messages.{UsedDeletedMsg, EventNotMatchedMsg}
import com.yetu.play.authenticator.models.User
import com.yetu.play.authenticator.models.daos.UserDAOImpl
import com.yetu.play.authenticator.utils.{BaseSpec, FakeGlobal}
import play.api.libs.concurrent.Akka
import play.api.libs.json.{JsString, Json}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.language.postfixOps


class EventsActorTest extends BaseSpec {

  implicit var system: ActorSystem = Akka.system

  implicit val timeout = Timeout(5 second)

  val dao: UserDAOImpl = app.global.asInstanceOf[FakeGlobal].userDao

  val testUser = User(loginInfo = null, userUUID = UUID.randomUUID(),
    firstName = None, lastName = None,
    fullName = None, email = None, avatarURL = None)

  val testJsonExistingUser = Json.stringify(Json.obj(
    "event" -> JsString("logout"),
    "userId" -> JsString(testUser.userUUID.toString)
  ))

  val testJsonNonExistingUser = Json.stringify(Json.obj(
    "event" -> JsString("logout"),
    "userId" -> JsString(UUID.randomUUID().toString)
  ))

  val testJsonNotMatchedEvent = Json.stringify(Json.obj(
    "event" -> JsString("logout"),
    "userId" -> JsString(testUser.userUUID.toString)
  ))

   before {
    dao.save(testUser)
  }

  after {
    UserDAOImpl.users.clear()
  }

  "Events actor " must {

    "remove user info from userDao in case of valid message" in {
      whenReady(dao.find(testUser.userUUID)) { res =>
        res mustBe Some(testUser)
        val ref = TestActorRef(EventsActor.props(dao))
        ref ? MsgDeliver(testJsonExistingUser) map { res =>
          res mustBe UsedDeletedMsg
          whenReady(dao.find(testUser.userUUID)) { res =>
            res mustBe None
          }
        }
      }
    }

    "do not modify UserDAO in case of non existing user removal request" in {
      whenReady(dao.find(testUser.userUUID)) { res =>
        res mustBe Some(testUser)
      }
      val ref = TestActorRef(EventsActor.props(dao))
      ref ! MsgDeliver(testJsonNonExistingUser)
      whenReady(dao.find(testUser.userUUID)) { res =>
        res mustBe Some(testUser)
      }
    }

    "respond with NotMatchedEvent message in case if received event is a valid json but not supported" in {
      (TestActorRef(EventsActor.props(dao)) ? MsgDeliver(testJsonNotMatchedEvent)) map {
        case EventNotMatchedMsg => 2 mustBe 2
        case _ => fail()
      }
    }

    "respond with Invalid Event message in case of received invalid json" in {
      val dao: UserDAOImpl = app.global.asInstanceOf[FakeGlobal].userDao
      val ref = TestActorRef(EventsActor.props(dao))
      ref ! MsgDeliver("wrong json")
    }
  }


}
