package com.horothesun.demo

import cats.effect.{IO, Resource}
import cats.effect.unsafe.implicits.global
import com.amazonaws.services.lambda.runtime._
import scala.jdk.CollectionConverters.MapHasAsScala

class LambdaHandler extends RequestHandler[java.util.Map[String, Object], String] {

  override def handleRequest(input: java.util.Map[String, Object], context: Context): String =
    run(input.asScala.toMap, context).unsafeRunSync()

  def run(input: Map[String, Object], context: Context): IO[String] = {
    def logLn(message: => String): IO[Unit] = IO(context.getLogger.log(s"$message\n"))
    for {
      env <- getEnvVars
      _   <- logLn(s"ENVIRONMENT VARIABLES: ${env.mkString("[\n  ", "\n  ", "\n]")}")
      _   <- logLn(s"CONTEXT: ${showContext(context)}")
      _ <- logLn(
        s"INPUT: ${input.map { case (k, v) => s"$k -> ${v.toString}" }.mkString("[\n  ", "\n  ", "\n]")}"
      )
      s <- dependencies.use { clock =>
        Logic(clock).appLogic
      }
    } yield s
  }

  def dependencies: Resource[IO, Clock] =
    Resource.pure[IO, Clock](Clock.create)

  def getEnvVars: IO[Map[String, String]] =
    IO(System.getenv.asScala.toMap)

  def showContext(c: Context): String =
    "[\n" +
      s"  request_id -> ${c.getAwsRequestId}\n" +
      s"  log_group_name -> ${c.getLogGroupName}\n" +
      s"  log_stream_name -> ${c.getLogStreamName}\n" +
      s"  function_name -> ${c.getFunctionName}\n" +
      s"  function_version -> ${c.getFunctionVersion}\n" +
      s"  invoked_function_arn -> ${c.getInvokedFunctionArn}\n" +
      s"  remaining_time_in_millis -> ${c.getRemainingTimeInMillis}\n" +
      s"  memory_limit_in_mb -> ${c.getMemoryLimitInMB}\n" +
      "]"

}
