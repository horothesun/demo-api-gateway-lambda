package com.horothesun.demo.lambda

import com.amazonaws.services.lambda.runtime.events._
import Models._

object Output {

  def getResponse(
      statusCode: StatusCode,
      body: String,
      bodyEncoding: BodyEncoding
  ): APIGatewayV2HTTPResponse =
    APIGatewayV2HTTPResponse
      .builder()
      .withBody(bodyEncoding.encode(body))
      .withIsBase64Encoded(bodyEncoding.isBase64Encoded)
      .withStatusCode(statusCode.toInt)
      .build()

}
