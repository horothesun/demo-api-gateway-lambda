package com.horothesun.demo

import cats.effect.implicits._
import cats.effect.{IO, Resource}
import Models._
import Models.Output._
import Logic._

case class Logic(log: Logger) {

  def appLogic(input: Input): IO[Output] =
    getClients.use(clock => getOutput(clock)(input))

}

object Logic {

  def getOutput(clock: ClockClient)(input: Input): IO[Output] =
    clock.currentDateTime.map(DateTimeBody)

  def getClients: Resource[IO, ClockClient] =
    IO(ClockClient.create).toResource

}
