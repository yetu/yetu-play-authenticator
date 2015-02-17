package com.yetu.play.authenticator.routes

import com.yetu.youtube.utils.BaseSpec
import play.api.test.FakeRequest
import play.api.test.Helpers._

class SecuredActionRouteSpec extends BaseSpec {

  val helloUrl = "/helloTestRoute"

  s"non-authenticated GET request on $helloUrl" must {
    "return a 303 response" in {

      val Some(response) = route(FakeRequest(GET, helloUrl))
      status(response) mustEqual (SEE_OTHER)
    }

  }

  s"authenticated GET request on $helloUrl" must {
    "return a valid 200 response" in {

      val response = getRequestAuthenticated(helloUrl)
      status(response) mustEqual (OK)
      contentAsString(response) must include("hello")
    }

  }

}
