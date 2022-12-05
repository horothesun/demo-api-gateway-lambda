package com.horothesun.demo.lambda

import Models.StatusCode
import Models.BodyEncoding._
import munit.FunSuite

class OutputTest extends FunSuite {

  test("getResponse with plain-text body") {
    val body     = "{\"hello\":\"world\"}"
    val response = Output.getResponse(StatusCode.Ok, body, NoEncoding)
    assertEquals(response.getStatusCode, 200)
    assertEquals(response.getBody, body)
    assertEquals(response.getIsBase64Encoded, false)
  }

  test("getResponse with base64 encoded body") {
    val body     = "{\"hello\":\"world\"}"
    val response = Output.getResponse(StatusCode.Ok, body, Base64Encoding)
    assertEquals(response.getStatusCode, 200)
    assertEquals(response.getBody, "eyJoZWxsbyI6IndvcmxkIn0=")
    assertEquals(response.getIsBase64Encoded, true)
  }

}
