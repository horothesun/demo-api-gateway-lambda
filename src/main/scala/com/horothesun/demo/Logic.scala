package com.horothesun.demo

import cats.effect.IO
import io.circe._
import io.circe.syntax._
import java.time.LocalDateTime

case class DateTimeBody(server_date_time: LocalDateTime)
object DateTimeBody {
  implicit val encoder: Encoder[DateTimeBody] =
    Encoder[LocalDateTime].contramap(_.server_date_time)
}

case class Logic(clock: Clock) {

  def appLogic: IO[String] =
    clock.currentDateTime.map(d => DateTimeBody(d).asJson.noSpaces)

}
