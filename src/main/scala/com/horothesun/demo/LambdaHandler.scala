package com.horothesun.demo

import cats.effect.{IO, Resource}
import cats.effect.unsafe.implicits.global
import cats.implicits._
import com.amazonaws.services.lambda.runtime._
import scala.jdk.CollectionConverters._
import scala.util.Try

class LambdaHandler extends RequestHandler[java.util.Map[String, Object], String] {

  override def handleRequest(input: java.util.Map[String, Object], context: Context): String =
    run(input.asScala.toMap, context).unsafeRunSync()

  def run(input: Map[String, Object], context: Context): IO[String] = {
    def logLn(message: => String): IO[Unit] = IO(context.getLogger.log(s"$message\n"))
    for {
      env  <- getEnvVars
      _    <- logLn(showEnvVars(env))
      _    <- logLn(showContext(context))
      _    <- logLn(showInput(input))
      body <- getBody(input)
      _    <- logLn(s"BODY: $body")
      s <- dependencies.use { clock =>
        Logic(clock).appLogic
      }
    } yield s
  }

  def dependencies: Resource[IO, Clock] =
    Resource.pure[IO, Clock](Clock.create)

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

  def getBody(in: Map[String, Object]): IO[String] =
    IO.fromOption(
      (
        bodyFromInput(in),
        isBase64EncodedFromInput(in)
      ).flatMapN(decodedBody)
    )(new Throwable("Couldn't decode input body!"))

  def isBase64EncodedFromInput(in: Map[String, Object]): Option[Boolean] =
    in.get("isBase64Encoded")
      .collect {
        case b: java.lang.Boolean => b
        case s: String =>
          s match {
            case "true"  => true
            case "false" => false
          }
      }

  def bodyFromInput(in: Map[String, Object]): Option[String] =
    in.get("body").collect { case s: String => s }

  def decodedBody(body: String, isBase64Encoded: Boolean): Option[String] =
    if (isBase64Encoded) Try(new String(java.util.Base64.getDecoder.decode(body))).toOption
    else Some(body)

}
