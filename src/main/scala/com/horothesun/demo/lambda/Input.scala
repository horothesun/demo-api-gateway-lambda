package com.horothesun.demo.lambda

import cats.implicits._
import Models._
import scala.util.Try

/*
  API Gateway integration - Payload format 2.0
  https://docs.aws.amazon.com/apigateway/latest/developerguide/http-api-develop-integrations-lambda.html
*/
object Input {

  def getBodyOpt(in: Map[String, Object]): Option[String] =
    (
      bodyFromInput(in),
      bodyEncodingFromInput(in)
    ).flatMapN(decodedBody)

  def bodyEncodingFromInput(in: Map[String, Object]): Option[BodyEncoding] =
    in.get("isBase64Encoded")
      .collect { case b: java.lang.Boolean => b }
      .map(b =>
        if (b) BodyEncoding.Base64
        else BodyEncoding.None
      )

  def bodyFromInput(in: Map[String, Object]): Option[String] =
    in.get("body").collect { case s: String => s }

  def decodedBody(body: String, bodyEncoding: BodyEncoding): Option[String] =
    bodyEncoding match {
      case BodyEncoding.Base64 => Try(new String(java.util.Base64.getDecoder.decode(body))).toOption
      case BodyEncoding.None   => Some(body)
    }

}
