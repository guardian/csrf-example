package util

import org.scalatest.{FreeSpec, Matchers}
import Base64._
import play.core.netty.utils.ServerCookieEncoder


class Base64StringTest extends FreeSpec with Matchers {
  val json = """{"foo":"bar","list":["a","b","b"]}"""

  "decode -> encode -> decode leaves string unchanged" in {
    val encodedJson = json.base64Encode
    encodedJson.base64Encode.base64Decode shouldEqual encodedJson
  }

  "json data is cookie-safe when encoded" in {
    noException should be thrownBy ServerCookieEncoder.STRICT.encode("cookieName", json.base64Encode)
  }
}
