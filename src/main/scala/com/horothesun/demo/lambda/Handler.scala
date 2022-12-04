package com.horothesun.demo.lambda

import cats.effect.unsafe.implicits.global
import cats.effect.{IO, Resource}
import com.amazonaws.services.lambda.runtime._
import com.horothesun.demo._
import com.horothesun.demo.lambda.Models._
import io.circe.syntax._

import scala.jdk.CollectionConverters._

class Handler extends RequestHandler[java.util.Map[String, Object], String] {

  override def handleRequest(input: java.util.Map[String, Object], context: Context): String =
    run(input.asScala.toMap, context).unsafeRunSync()

  def run(input: Map[String, Object], context: Context): IO[String] = {
    implicit val logger: LambdaLogger = context.getLogger

    val inputBody = Input.getBody(input)
    for {
      env <- getEnvVars
      _   <- logLn(showEnvVars(env))
      _   <- logLn(showContext(context))
      _   <- logLn(showInput(input))
      _   <- logLn(showInputBody(inputBody))
      result <- dependencies.use { clock =>
        Logic(clock).appLogic
      }
      output = s"{\"statusCode\":400,\"body\":${result.asJson.noSpaces}}"
      // LambdaOutput.fromBodyAndEncoding(result, BodyEncoding.None).asJson.noSpaces
      _ <- logLn(output)
    } yield output
  }

  def dependencies: Resource[IO, Clock] =
    Resource.pure[IO, Clock](Clock.create)

  def logLn(message: => String)(implicit logger: LambdaLogger): IO[Unit] =
    IO(logger.log(s"$message\n"))

  def getEnvVars: IO[Map[String, String]] =
    IO(System.getenv.asScala.toMap)

  def showEnvVars(env: Map[String, String]): String =
    "ENVIRONMENT VARIABLES: " + env.mkString("[\n  ", "\n  ", "\n]")

  def showContext(c: Context): String =
    "CONTEXT: [\n" +
      s"  request_id -> ${c.getAwsRequestId}\n" +
      s"  log_group_name -> ${c.getLogGroupName}\n" +
      s"  log_stream_name -> ${c.getLogStreamName}\n" +
      s"  function_name -> ${c.getFunctionName}\n" +
      s"  function_version -> ${c.getFunctionVersion}\n" +
      s"  invoked_function_arn -> ${c.getInvokedFunctionArn}\n" +
      s"  remaining_time_in_millis -> ${c.getRemainingTimeInMillis}\n" +
      s"  memory_limit_in_mb -> ${c.getMemoryLimitInMB}\n" +
      "]"

  def showInput(in: Map[String, Object]): String =
    "INPUT: " + in
      .map { case (k, v) => s"$k -> ${v.toString}" }
      .mkString("[\n  ", "\n  ", "\n]")

  def showInputBody(body: Option[String]): String =
    body.fold("MISSING BODY: couldn't decode input body!")(b => s"BODY: $b")

}
