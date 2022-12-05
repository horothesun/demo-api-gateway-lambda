package com.horothesun.demo.lambda

import java.util.Base64._
import scala.util.Try

object Models {

  sealed trait BodyEncoding {
    import BodyEncoding._

    def encode: String => String = this match {
      case Base64     => s => new String(getEncoder.encode(s.getBytes))
      case NoEncoding => identity[String]
    }

    def decode: String => Option[String] = this match {
      case Base64     => s => Try(new String(getDecoder.decode(s))).toOption
      case NoEncoding => Some[String]
    }
  }
  object BodyEncoding {
    case object Base64     extends BodyEncoding
    case object NoEncoding extends BodyEncoding
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
