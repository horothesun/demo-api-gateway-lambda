package com.horothesun.demo.lambda

import cats.effect.unsafe.implicits.global
import cats.effect.{IO, Resource}
import com.amazonaws.services.lambda.runtime._
import com.amazonaws.services.lambda.runtime.events._
import com.horothesun.demo._
import lambda.Models.BodyEncoding._
import lambda.Models.StatusCode
import lambda.Output._
import scala.jdk.CollectionConverters._
import Handler._
import Input._

class Handler extends RequestHandler[APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse] {

  override def handleRequest(
      event: APIGatewayV2HTTPEvent,
      context: Context
  ): APIGatewayV2HTTPResponse =
    run(event, context).unsafeRunSync()

  def run(event: APIGatewayV2HTTPEvent, context: Context): IO[APIGatewayV2HTTPResponse] = {
    implicit val logger: LambdaLogger = context.getLogger

    val decodedBody = getDecodedBody(event)
    for {
      env <- getEnvVars
      _   <- logLn(showEnvVars(env))
      _   <- logLn(showContext(context))
      _   <- logLn(showEvent(event))
      _   <- logLn(showDecodedBody(decodedBody))
      result <- dependencies.use { clock =>
        Logic(clock).appLogic
      }
      response = createResponse(StatusCode.Ok, result, Base64Encoding)
      _ <- logLn(showResponse(response))
    } yield response
  }

  def dependencies: Resource[IO, Clock] =
    Resource.pure[IO, Clock](Clock.create)

}

object Handler {

  def getEnvVars: IO[Map[String, String]] =
    IO(System.getenv.asScala.toMap)

  def logLn(s: => String)(implicit logger: LambdaLogger): IO[Unit] =
    IO(logger.log(s"$s\n"))

  def showEnvVars(env: Map[String, String]): String =
    "ENVIRONMENT VARIABLES: " + env.mkString("[\n  ", "\n  ", "\n]")

  def showContext(ctx: Context): String =
    "CONTEXT: [\n" +
      s"  request_id -> ${ctx.getAwsRequestId}\n" +
      s"  log_group_name -> ${ctx.getLogGroupName}\n" +
      s"  log_stream_name -> ${ctx.getLogStreamName}\n" +
      s"  function_name -> ${ctx.getFunctionName}\n" +
      s"  function_version -> ${ctx.getFunctionVersion}\n" +
      s"  invoked_function_arn -> ${ctx.getInvokedFunctionArn}\n" +
      s"  remaining_time_in_millis -> ${ctx.getRemainingTimeInMillis}\n" +
      s"  memory_limit_in_mb -> ${ctx.getMemoryLimitInMB}\n" +
      "]"

  def showEvent(event: APIGatewayV2HTTPEvent): String =
    s"EVENT: ${event.toString}"

  def showDecodedBody(body: Option[String]): String =
    body.fold("MISSING BODY: couldn't decode input body!")(b => s"BODY: $b")

  def showResponse(response: APIGatewayV2HTTPResponse): String =
    s"RESPONSE: $response"

}
