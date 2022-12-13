package com.horothesun.demo

import cats.effect.IO
import Models._
import Models.Output._

case class Logic(clock: Clock) {

  def appLogic(input: Input): IO[Output] =
    clock.currentDateTime.map(DateTimeBody)

}
