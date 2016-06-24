package controllers

import javax.inject._

import logic.Auth
import models.Entry
import play.api.mvc._
import logic.Data
import logic.Data.RichRequest

@Singleton
class HomeController @Inject() extends Controller {

  def index = Action {
    Ok(views.html.index())
  }

  def list = Action { implicit request =>
    ifLoggedIn { data =>
      Ok(views.html.list(data))
    }
  }

  def add = Action { implicit request =>
    ifLoggedIn { _ =>
      Ok(views.html.add())
    }
  }

  def handleAdd = Action { implicit request =>
    ifLoggedIn { data =>
      val title = request.body.asFormUrlEncoded.get("title").head
      val text = request.body.asFormUrlEncoded.get("text").head
      val newCookie = Data.addEntry(Entry(title, text), request)
      SeeOther("/list").withCookies(newCookie)
    }
  }

  def delete(index: Int) = Action { implicit request =>
    ifLoggedIn { data =>
      SeeOther("/list").withCookies(Data.removeEntry(index, request))
    }
  }

  def furtherReading = Action {
    Ok(views.html.furtherReading())
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
