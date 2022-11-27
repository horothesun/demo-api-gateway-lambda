package com.horothesun.demo

import cats.implicits._
import cats.effect.{IO, Resource}
import cats.effect.unsafe.implicits.global
import com.google.gson._
import com.amazonaws.services.lambda.runtime._

final class LambdaHandler extends RequestHandler[java.util.Map[String, String], String] {

  val gson: Gson = new GsonBuilder().setPrettyPrinting().create

  override def handleRequest(event: java.util.Map[String, String], context: Context): String =
    run(event, context).unsafeRunSync()

  def run(event: java.util.Map[String, String], context: Context): IO[String] =
    IO(
      List(
        s"ENVIRONMENT VARIABLES: ${gson.toJson(System.getenv)}",
        s"CONTEXT: ${gson.toJson(context)}",
        s"EVENT: ${gson.toJson(event)}"
      )
    )
      .flatMap { ms =>
        val logger = context.getLogger
        ms.traverse_(m => logLn(logger, m))
      }
      .flatMap(_ =>
        dependencies.use { clock =>
          Logic(clock).appLogic
        }
      )

  def dependencies: Resource[IO, Clock] =
    Resource.pure[IO, Clock](Clock.create)

  def logLn(logger: LambdaLogger, message: String): IO[Unit] =
    IO(logger.log(s"$message\n"))

}
