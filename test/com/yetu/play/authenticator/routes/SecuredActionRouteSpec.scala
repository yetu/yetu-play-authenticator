package com.yetu.play.authenticator.routes

import com.yetu.play.authenticator.utils.{BaseSpec, FakeGlobal}
import play.api.test.FakeRequest
import play.api.test.Helpers._

class SecuredActionRouteSpec extends BaseSpec {

  s"non-authenticated GET request on ${FakeGlobal.routeHelloTest}" must {
    "return a 303 response" in {
      val Some(response) = route(FakeRequest(GET, FakeGlobal.routeHelloTest))
      status(response) mustEqual (SEE_OTHER)
    }
  }

  s"authenticated GET request on ${FakeGlobal.routeHelloTest}" must {
    "return a valid 200 response" in {
      val response = getRequestAuthenticated(FakeGlobal.routeHelloTest)
      status(response) mustEqual (OK)
      contentAsString(response) must include("hello")
    }
  }

  s"non-authenticated POST request on ${FakeGlobal.routeApiLogout}" must {
    "return a valid 204 response when access token is removed" in {
      val response = postRequestAuthenticated(s"${FakeGlobal.routeApiLogout}?access_token=${FakeGlobal.oauth2Info.accessToken}")
      status(response) mustEqual (NO_CONTENT)
    }

    "return a valid 404 response when access token does not exist" in {
      val response = postRequestAuthenticated(s"${FakeGlobal.routeApiLogout}?access_token=${FakeGlobal.oauth2Info.accessToken}")
      status(response) mustEqual (NOT_FOUND)
    }

    "return a valid 404 response when access token is not giving as url parameter" in {
      val response = postRequestAuthenticated(FakeGlobal.routeApiLogout)
      status(response) mustEqual (NOT_FOUND)
    }
  }

}
