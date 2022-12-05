package com.horothesun.demo.lambda

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent
import Models.BodyEncoding._
import munit.FunSuite

class InputTest extends FunSuite {

  test("getBody from base64 encoded body") {
    val e = APIGatewayV2HTTPEvent
      .builder()
      .withBody("YWJj")
      .withIsBase64Encoded(true)
      .build()
    assertEquals(Input.getBody(e), Some("abc"))
  }

  test("bodyFromEvent with null body returns None") {
    val e = APIGatewayV2HTTPEvent.builder().withBody(null).build()
    assertEquals(Input.bodyFromEvent(e), None)
  }

  test("bodyFromEvent with 'abc' body returns Some('abc')") {
    val e = APIGatewayV2HTTPEvent.builder().withBody("abc").build()
    assertEquals(Input.bodyFromEvent(e), Some("abc"))
  }

  test("bodyEncodingFromEvent with true isBase64Encoded value returns Base64") {
    val e = APIGatewayV2HTTPEvent.builder().withIsBase64Encoded(true).build()
    assertEquals(Input.bodyEncodingFromEvent(e), Base64Encoding)
  }

  test("bodyEncodingFromEvent with false isBase64Encoded value returns None") {
    val e = APIGatewayV2HTTPEvent.builder().withIsBase64Encoded(false).build()
    assertEquals(Input.bodyEncodingFromEvent(e), NoEncoding)
  }

}
