package com.horothesun.demo.lambda

import com.amazonaws.services.lambda.runtime.events._
import com.horothesun.demo.Models.Output
import com.horothesun.demo.Models.Output._
import io.circe.generic.auto._
import io.circe.syntax._
import Models._
import Models.BodyEncoding._

object HandlerOutput {

  def createResponse(output: Output, bodyEncoding: BodyEncoding): APIGatewayV2HTTPResponse =
    output match {
      case dtb @ DateTimeBody(_) =>
        createResponse(StatusCode.Ok, dtb.asJson.noSpaces, bodyEncoding)
    }

  def createResponse(
      statusCode: StatusCode,
      body: String,
      bodyEncoding: BodyEncoding
  ): APIGatewayV2HTTPResponse =
    APIGatewayV2HTTPResponse
      .builder()
      .withBody(bodyEncoding.encode(body))
      .withIsBase64Encoded(bodyEncoding == Base64)
      .withStatusCode(statusCode.toInt)
      .build()

}
