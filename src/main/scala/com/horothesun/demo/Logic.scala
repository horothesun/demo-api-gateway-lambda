package com.horothesun.demo

import cats.effect.IO
import Models.DateTimeBody
import io.circe.generic.auto._
import io.circe.syntax._

case class Logic(clock: Clock) {

  def appLogic: IO[String] =
    clock.currentDateTime.map(d => DateTimeBody(d).asJson.noSpaces)

}
