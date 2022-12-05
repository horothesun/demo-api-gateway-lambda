package com.horothesun.demo.lambda

import scala.util.Try

object Models {

  sealed trait BodyEncoding {
    import BodyEncoding._

    def isBase64Encoded: Boolean = this match {
      case Base64Encoding => true
      case NoEncoding     => false
    }

    def encode: String => String = this match {
      case Base64Encoding => base64Encode
      case NoEncoding     => identity[String]
    }

    def decode: String => Option[String] = this match {
      case Base64Encoding => base64Decode
      case NoEncoding     => Some[String]
    }

    def base64Encode(s: String): String =
      new String(java.util.Base64.getEncoder.encode(s.getBytes))

    def base64Decode(s: String): Option[String] =
      Try(new String(java.util.Base64.getDecoder.decode(s))).toOption
  }
  object BodyEncoding {
    case object Base64Encoding extends BodyEncoding
    case object NoEncoding     extends BodyEncoding
  }

  sealed trait StatusCode {
    import StatusCode._
    def toInt: Int = this match {
      case Ok         => 200
      case BadRequest => 400
    }
  }
  object StatusCode {
    case object Ok         extends StatusCode
    case object BadRequest extends StatusCode
  }

}
