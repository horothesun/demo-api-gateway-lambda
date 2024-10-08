package com.horothesun.demo.lambda

import Models._
import Models.BodyEncoding._
import com.amazonaws.services.lambda.runtime.events._
import com.horothesun.demo.Models.Input

object HandlerInput {

  def getDecodedBody(event: APIGatewayV2HTTPEvent): Option[Input] =
    getBody(event)
      .flatMap(getBodyEncoding(event).decode)
      .map(Input)

  def getBody(event: APIGatewayV2HTTPEvent): Option[String] =
    Option(event.getBody)

  def getBodyEncoding(event: APIGatewayV2HTTPEvent): BodyEncoding =
    if (event.getIsBase64Encoded) Base64
    else NoEncoding

}
