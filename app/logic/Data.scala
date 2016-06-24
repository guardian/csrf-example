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

  def addEntry(entry: Entry, request: RequestHeader): Cookie = {
    val json = Json.toJson(request.entries :+ entry)
    Cookie(dataCookieName, Json.stringify(json).base64Encode)
  }

  def removeEntry(index: Int, request: RequestHeader): Cookie = {
    val data = request.entries
    val newData = data.take(index) ++ data.take(index + 1)
    val json = Json.toJson(newData)
    Cookie(dataCookieName, Json.stringify(json).base64Encode)
  }
}
