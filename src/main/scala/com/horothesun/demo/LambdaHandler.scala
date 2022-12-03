package com.horothesun.demo

import cats.effect.{IO, Resource}
import cats.effect.unsafe.implicits.global
//import com.google.gson._
import com.amazonaws.services.lambda.runtime._

import scala.jdk.CollectionConverters.MapHasAsScala

class LambdaHandler extends RequestHandler[java.util.Map[String, String], String] {

//  val gson: Gson = new GsonBuilder().setPrettyPrinting().create

  override def handleRequest(event: java.util.Map[String, String], context: Context): String =
    run(event, context).unsafeRunSync()

  def run(event: java.util.Map[String, String], context: Context): IO[String] = {
    def log(message: => String): IO[Unit] = logLn(context.getLogger, message)
    for {
      _       <- log("Function START!!! 🔥🔥🔥")
      envVars <- getEnvVars
      _       <- log(s"ENVIRONMENT VARIABLES: ${envVars.toString}")
      _       <- log(s"CONTEXT: ${context.toString}")
      _       <- log(s"EVENT: ${event.asScala.toMap.toString}")
      s <- dependencies.use { clock =>
        Logic(clock).appLogic
      }
    } yield s
  }

  def logLn(logger: LambdaLogger, message: => String): IO[Unit] =
    IO(logger.log(s"$message\n"))

  def dependencies: Resource[IO, Clock] =
    Resource.pure[IO, Clock](Clock.create)

  def getEnvVars: IO[Map[String, String]] =
    IO(System.getenv.asScala.toMap)

}
