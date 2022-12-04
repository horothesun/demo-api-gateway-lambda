package com.horothesun.demo.lambda

import io.circe._
import io.circe.generic.semiauto._

object Models {

  sealed trait BodyEncoding
  object BodyEncoding {
    case object Base64 extends BodyEncoding
    case object None   extends BodyEncoding
  }

  sealed trait StatusCode
  object StatusCode {
    case object OK         extends StatusCode
    case object BadRequest extends StatusCode

    implicit val encoder: Encoder[StatusCode] =
      Encoder.encodeInt.contramap {
        case OK         => 200
        case BadRequest => 400
      }
  }

  /*
    API Gateway integration - Payload format 2.0
    https://docs.aws.amazon.com/apigateway/latest/developerguide/http-api-develop-integrations-lambda.html
   */
  case class LambdaOutput(
      isBase64Encoded: Boolean,
      statusCode: StatusCode,
      body: String,
      headers: Map[String, String],
      cookies: List[String]
  )
  object LambdaOutput {
    implicit val encoder: Encoder[LambdaOutput] = deriveEncoder

    def fromBodyAndEncoding(body: String, bodyEncoding: BodyEncoding): LambdaOutput =
      LambdaOutput(
        isBase64Encoded = bodyEncoding == BodyEncoding.Base64,
        StatusCode.BadRequest,
        body = bodyEncoding match {
          case BodyEncoding.Base64 => new String(java.util.Base64.getEncoder.encode(body.getBytes))
          case BodyEncoding.None   => body
        },
        headers = Map("Content-Type" -> "application/json"),
        cookies = List.empty
      )
  }

}
