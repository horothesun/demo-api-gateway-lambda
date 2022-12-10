package com.horothesun.demo

import cats.effect.IO
import Models._
import io.circe.generic.auto._
import io.circe.syntax._

case class Logic(clock: Clock) {

  def appLogic(input: Input): IO[String] =
    clock.currentDateTime.map(d => DateTimeBody(d).asJson.noSpaces)

}
