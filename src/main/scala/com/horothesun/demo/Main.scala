package com.horothesun.demo

import cats.effect.{IO, Resource}

object Main {

  def run: IO[String] =
    dependencies.use { clock =>
      Logic(clock).appLogic
    }

  def dependencies: Resource[IO, Clock] =
    Resource.pure[IO, Clock](Clock.create)

}
