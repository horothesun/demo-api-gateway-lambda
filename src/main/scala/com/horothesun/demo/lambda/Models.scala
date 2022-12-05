package com.horothesun.demo.lambda

import java.util.Base64
import scala.util.Try

object Models {

  sealed trait BodyEncoding {
    import BodyEncoding._

    def encode: String => String = this match {
      case Base64Encoding => s => new String(Base64.getEncoder.encode(s.getBytes))
      case NoEncoding     => identity[String]
    }

    def decode: String => Option[String] = this match {
      case Base64Encoding => s => Try(new String(Base64.getDecoder.decode(s))).toOption
      case NoEncoding     => Some[String]
    }

    def isBase64Encoded: Boolean = this match {
      case Base64Encoding => true
      case NoEncoding     => false
    }
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
