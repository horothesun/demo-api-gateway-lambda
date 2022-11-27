package com.horothesun.demo

import cats.effect.IO
import java.time.LocalDateTime

case class Logic(clock: Clock) {

  case class DateTimeBody(server_date_time: LocalDateTime)

  def appLogic: IO[Unit] = IO.unit

}
