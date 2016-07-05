package controllers

import javax.inject._

import logic.Auth
import models.Entry
import play.api.mvc._
import logic.Data
import logic.Data.{RichRequest, RichEntryList}

@Singleton
class HomeController @Inject() extends Controller {

  def index = Action { implicit request =>
    Ok(views.html.index(Auth.identity))
  }

  def hack = Action { implicit request =>
    Ok(views.html.hack(Auth.identity))
  }

  def list = Action { implicit request =>
    ifLoggedIn { data =>
      Ok(views.html.list(data, Auth.identity))
    }
  }

  def add = Action { implicit request =>
    ifLoggedIn { _ =>
      Ok(views.html.add(Auth.identity))
    }
  }

  def handleAdd = Action { implicit request =>
    ifLoggedIn { data =>
      val title = request.body.asFormUrlEncoded.get("title").head
      val text = request.body.asFormUrlEncoded.get("text").head
      val newCookie = Data.addEntry(Entry(title, text), request.entries).asCookie
      SeeOther("/list").withCookies(newCookie)
    }
  }

  def delete(index: Int) = Action { implicit request =>
    ifLoggedIn { data =>
      val cookie = Data.removeEntry(index, request.entries).asCookie
      SeeOther("/list").withCookies(cookie)
    }
  }

  def furtherReading = Action { implicit request =>
    Ok(views.html.furtherReading(Auth.identity))
  }

  def account = Action { implicit request =>
    Ok(views.html.account(Auth.identity))
  }

  def login = Action { implicit request =>
    Ok(views.html.login(request.flash.get("message")))
  }

  def handleLogin = Action { implicit request =>
    val username = request.body.asFormUrlEncoded.get("username").head
    val loginCookie = Auth.loginCookie(username, "")
    SeeOther("/").withCookies(loginCookie)
  }

  def logout = Action { implicit request =>
    SeeOther("/").discardingCookies(Auth.discardloginCookie)
  }

  private def ifLoggedIn(block: (List[Entry]) => Result)(implicit request: RequestHeader): Result = {
    if (Auth.isLoggedIn(request)) {
      block(request.entries)
    } else {
      SeeOther("/login").flashing("message" -> "You'll need to log in to use the site")
    }
  }
}
