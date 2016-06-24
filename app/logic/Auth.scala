package logic

import java.nio.charset.StandardCharsets.UTF_8

import play.api.mvc.Cookie
import play.api.mvc._

object Auth {
  import util.Base64._

  private val loginCookieName = "login"

  /**
    * Just an example, login always succeeds here!
    */
  def loginCookie(username: String, password: String): Cookie = {
    Cookie(loginCookieName, username.base64Encode)
  }

  def isLoggedIn(request: RequestHeader): Boolean = {
    request.cookies.exists(_.name == loginCookieName)
  }

  def discardloginCookie: DiscardingCookie = {
    DiscardingCookie(loginCookieName)
  }
}
