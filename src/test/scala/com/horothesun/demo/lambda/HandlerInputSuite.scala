package com.horothesun.demo.lambda

import HandlerInput._
import Models.BodyEncoding._
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent
import com.horothesun.demo.Models.Input
import munit.FunSuite

class HandlerInputSuite extends FunSuite {

  test("getDecodedBody from base64 encoded body") {
    val e = APIGatewayV2HTTPEvent
      .builder()
      .withBody("YWJj")
      .withIsBase64Encoded(true)
      .build()
    assertEquals(getDecodedBody(e), Some(Input("abc")))
  }

  test("getBody with null body returns None") {
    val e = APIGatewayV2HTTPEvent.builder().withBody(null).build()
    assertEquals(getBody(e), None)
  }

  test("getBody with 'abc' body returns Some('abc')") {
    val e = APIGatewayV2HTTPEvent.builder().withBody("abc").build()
    assertEquals(getBody(e), Some("abc"))
  }

  test("getBodyEncoding with true isBase64Encoded value returns Base64Encoding") {
    val e = APIGatewayV2HTTPEvent.builder().withIsBase64Encoded(true).build()
    assertEquals(getBodyEncoding(e), Base64)
  }

  test("getBodyEncoding with false isBase64Encoded value returns NoEncoding") {
    val e = APIGatewayV2HTTPEvent.builder().withIsBase64Encoded(false).build()
    assertEquals(getBodyEncoding(e), NoEncoding)
  }

}
