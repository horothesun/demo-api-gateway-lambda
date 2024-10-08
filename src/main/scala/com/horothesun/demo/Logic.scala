package com.horothesun.demo

import Logic._
import Models._
import Models.Output._
import cats.effect.{IO, Resource}
import cats.effect.implicits._

case class Logic(log: Logger) {

  def appLogic(input: Input): IO[Output] =
    getClients.use(clock => getOutput(clock)(input))

}

object Logic {

  def getOutput(clock: Clock)(input: Input): IO[Output] =
    clock.currentDateTime.map(DateTimeBody.apply)

  def getClients: Resource[IO, Clock] =
    IO(Clock.create).toResource

}
