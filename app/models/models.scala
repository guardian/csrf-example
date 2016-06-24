package models

import play.api.libs.json.Json


case class Entry(title: String, text: String)
object Entry {
  implicit val format = Json.format[Entry]
}
