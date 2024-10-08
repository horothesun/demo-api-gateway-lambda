package com.horothesun.demo.lambda

import Handler._
import HandlerInput._
import cats.effect.IO
import cats.effect.unsafe.implicits.global
import com.amazonaws.services.lambda.runtime._
import com.amazonaws.services.lambda.runtime.events._
import com.horothesun.demo._
import com.horothesun.demo.Models.Input
import lambda.HandlerOutput._
import lambda.Models._
import lambda.Models.BodyEncoding._
import scala.jdk.CollectionConverters._

class Handler extends RequestHandler[APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse] {

  override def handleRequest(
      event: APIGatewayV2HTTPEvent,
      context: Context
  ): APIGatewayV2HTTPResponse =
    run(event, context).unsafeRunSync()

  def run(event: APIGatewayV2HTTPEvent, context: Context): IO[APIGatewayV2HTTPResponse] = {
    val log         = Logger.create(context.getLogger)
    val decodedBody = getDecodedBody(event)
    for {
      env      <- getEnvVars
      _        <- log.info(showEnvVars(env))
      _        <- log.info(showContext(context))
      _        <- log.info(showEvent(event))
      _        <- log.info(showDecodedBody(decodedBody))
      response <- getResponse(log, decodedBody)
      _        <- log.info(showResponse(response))
    } yield response
  }

}

object Handler {

  val responseEncoding: BodyEncoding = Base64

  lazy val decodeInputBodyErrorResponse: APIGatewayV2HTTPResponse =
    createBadRequestResponse(
      BadRequestError("Lambda handler: Couldn't decode input body!"),
      responseEncoding
    )

  def getResponse(log: Logger, decodedBody: Option[Input]): IO[APIGatewayV2HTTPResponse] =
    decodedBody
      .fold(IO.pure(decodeInputBodyErrorResponse))(in =>
        Logic(log).appLogic(in).map(createResponse(_, responseEncoding))
      )

  def getEnvVars: IO[Map[String, String]] =
    IO(System.getenv.asScala.toMap)

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

  def showDecodedBody(body: Option[Input]): String =
    body.fold("MISSING BODY: couldn't decode input body!")(b => s"BODY: ${b.value}")

  def showResponse(response: APIGatewayV2HTTPResponse): String =
    s"RESPONSE: $response"

}
