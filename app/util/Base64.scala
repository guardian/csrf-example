package util

import java.nio.charset.StandardCharsets._
import java.util.{Base64 => Base64Codecs}


object Base64 {
  implicit class Base64String(str: String) {
    def base64Encode: String = {
      new String(Base64Codecs.getEncoder.encode(str.getBytes(UTF_8)))
    }

    def base64Decode: String = {
      new String(Base64Codecs.getDecoder.decode(str))
    }
  }
}
