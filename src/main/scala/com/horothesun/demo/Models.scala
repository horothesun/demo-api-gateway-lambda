package com.horothesun.demo

import io.circe.Encoder
import io.circe.generic.semiauto._
import java.time.LocalDateTime

object Models {

  case class Input(value: String)

  sealed trait Output
  object Output {
    case class InputParsingError(message: String) extends Output {
      def obfuscated: String = toString
    }

    case class DateTimeBody(server_date_time: LocalDateTime) extends Output
    object DateTimeBody {
      implicit val encoder: Encoder[DateTimeBody] = deriveEncoder
    }
  }

}
