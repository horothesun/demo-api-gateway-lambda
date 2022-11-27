package com.horothesun.demo

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import java.time.LocalDateTime

case class DateTimeBody(server_date_time: LocalDateTime)

case class Logic(clock: Clock) {

  def appLogic: IO[String] =
    clock.currentDateTime.map(d => DateTimeBody(d).asJson.noSpaces)

}
