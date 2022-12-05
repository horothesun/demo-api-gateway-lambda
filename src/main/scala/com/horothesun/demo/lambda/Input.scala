package com.horothesun.demo.lambda

import com.amazonaws.services.lambda.runtime.events._
import Models._
import Models.BodyEncoding._

object Input {

  def getBody(event: APIGatewayV2HTTPEvent): Option[String] =
    bodyFromEvent(event)
      .flatMap(body => bodyEncodingFromEvent(event).decode(body))

  def bodyFromEvent(event: APIGatewayV2HTTPEvent): Option[String] =
    Option(event.getBody)

  def bodyEncodingFromEvent(event: APIGatewayV2HTTPEvent): BodyEncoding =
    if (event.getIsBase64Encoded) Base64Encoding else NoEncoding

}
