package com.horothesun.demo.lambda

import cats.effect.unsafe.implicits.global
import cats.effect.{IO, Resource}
import com.amazonaws.services.lambda.runtime._
import com.amazonaws.services.lambda.runtime.events._
import com.horothesun.demo._
import com.horothesun.demo.lambda.Models._
//import io.circe.syntax._

import scala.jdk.CollectionConverters._

class Handler extends RequestHandler[APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse] {

  override def handleRequest(
      event: APIGatewayV2HTTPEvent,
      context: Context
  ): APIGatewayV2HTTPResponse =
    run(event, context).unsafeRunSync()

  def run(event: APIGatewayV2HTTPEvent, context: Context): IO[APIGatewayV2HTTPResponse] = {
    implicit val logger: LambdaLogger = context.getLogger

//    val inputBody = Input.getBody(event)
    val inputBody = Input.decodedBody(
      event.getBody,
      if (event.getIsBase64Encoded) BodyEncoding.Base64
      else BodyEncoding.None
    )
    for {
      env <- getEnvVars
      _   <- logLn(showEnvVars(env))
      _   <- logLn(showContext(context))
      _   <- logLn(s"EVENT: ${event.toString}")
      _   <- logLn(showInputBody(inputBody))
      result <- dependencies.use { clock =>
        Logic(clock).appLogic
      }
      //      output = s"{\"statusCode\":400,\"body\":${result.asJson.noSpaces}}"
      // LambdaOutput.fromBodyAndEncoding(result, BodyEncoding.None).asJson.noSpaces
      response = getResponse(statusCode = 400, result, isBase64Encoded = false)
      _ <- logLn(s"RESPONSE: $response")
    } yield response
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

//  def showInput(in: Map[String, Object]): String =
//    "INPUT: " + in
//      .map { case (k, v) => s"$k -> ${v.toString}" }
//      .mkString("[\n  ", "\n  ", "\n]")

  def showInputBody(body: Option[String]): String =
    body.fold("MISSING BODY: couldn't decode input body!")(b => s"BODY: $b")

  def getResponse(
      statusCode: Int,
      result: String,
      isBase64Encoded: Boolean
  ): APIGatewayV2HTTPResponse =
    APIGatewayV2HTTPResponse
      .builder()
      .withStatusCode(statusCode)
      .withBody(result)
      .withIsBase64Encoded(isBase64Encoded)
      .build()

}
