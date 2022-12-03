package com.horothesun.demo

import cats.effect.{IO, Resource}
import cats.effect.unsafe.implicits.global
import com.amazonaws.services.lambda.runtime._
import scala.jdk.CollectionConverters.MapHasAsScala

class LambdaHandler extends RequestHandler[Object, String] {

  override def handleRequest(input: Object, context: Context): String =
    run(input, context).unsafeRunSync()

  def run(input: Object, context: Context): IO[String] = {
    def logLn(message: => String): IO[Unit] = IO(context.getLogger.log(s"$message\n"))
    for {
      evs <- getEnvVars
      _   <- logLn(s"ENVIRONMENT VARIABLES: ${evs.mkString("[\n  ", ",\n  ", "\n]")}")
      _   <- logLn(s"EVENT: ${input.toString}")
      s <- dependencies.use { clock =>
        Logic(clock).appLogic
      }
    } yield s
  }

  def dependencies: Resource[IO, Clock] =
    Resource.pure[IO, Clock](Clock.create)

  def getEnvVars: IO[Map[String, String]] =
    IO(System.getenv.asScala.toMap)

}
