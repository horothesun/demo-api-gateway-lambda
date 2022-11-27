package com.horothesun.demo

import cats.effect.{IO, IOApp, Resource}

object Main extends IOApp.Simple {

  override def run: IO[Unit] =
    dependencies.use { clock =>
      Logic(clock).appLogic
    }

  def dependencies: Resource[IO, Clock] =
    Resource.pure[IO, Clock](Clock.create)

}
