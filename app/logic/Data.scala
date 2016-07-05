package logic

import models.Entry
import play.api.libs.json.Json
import play.api.mvc.{Cookie, RequestHeader}

object Data {
  import util.Base64._

  private val dataCookieName = "data"

  implicit class RichRequest(request: RequestHeader) {
    def entries: List[Entry] = {
      request.cookies.find(_.name == dataCookieName).fold(List[Entry]()){ cookie =>
        Json.parse(cookie.value.base64Decode).as[List[Entry]]
      }
    }
  }

  implicit class RichEntryList(data: List[Entry]) {
    def asCookie = {
      val json = Json.toJson(data)
      Cookie(dataCookieName, Json.stringify(json).base64Encode)
    }
  }

  def addEntry(entry: Entry, data: List[Entry]): List[Entry] = {
    data :+ entry
  }

  def removeEntry(index: Int, data: List[Entry]): List[Entry] = {
    data.take(index) ++ data.drop(index + 1)
  }
}
