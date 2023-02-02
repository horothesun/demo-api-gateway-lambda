package com.horothesun.demo

import cats.effect.IO
import java.time._

trait ClockClient {
  def currentDateTime: IO[LocalDateTime]
}

object ClockClient {

  def create: ClockClient = new ClockClient {
    override def currentDateTime: IO[LocalDateTime] =
      cats.effect
        .Clock[IO]
        .realTimeInstant
        .map(instant => LocalDateTime.ofInstant(instant, ZoneId.systemDefault()))
  }

}
