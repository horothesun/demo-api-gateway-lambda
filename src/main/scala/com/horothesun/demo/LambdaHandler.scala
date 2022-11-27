package com.horothesun.demo

import cats.implicits._
import cats.effect.{IO, Resource}
import cats.effect.unsafe.implicits.global
import com.google.gson._
import com.amazonaws.services.lambda.runtime._

class LambdaHandler extends RequestHandler[java.util.Map[String, String], String] {

  val gson: Gson = new GsonBuilder().setPrettyPrinting().create

  override def handleRequest(event: java.util.Map[String, String], context: Context): String =
    run(event, context).unsafeRunSync()

  def run(event: java.util.Map[String, String], context: Context): IO[String] = {
    def log(message: String): IO[Unit] = logLn(context.getLogger, message)
    for {
      _ <- log(s"ENVIRONMENT VARIABLES: ${gson.toJson(System.getenv)}")
      _ <- log(s"CONTEXT: ${gson.toJson(context)}")
      _ <- log(s"EVENT: ${gson.toJson(event)}")
      s <- dependencies.use { clock =>
        Logic(clock).appLogic
      }
    } yield s
  }

  def logLn(logger: LambdaLogger, message: String): IO[Unit] = IO(logger.log(s"$message\n"))

  def dependencies: Resource[IO, Clock] =
    Resource.pure[IO, Clock](Clock.create)

}
