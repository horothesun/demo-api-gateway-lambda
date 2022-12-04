package com.horothesun.demo.lambda

import com.horothesun.demo.lambda.Models.BodyEncoding
import munit.FunSuite

class InputTest extends FunSuite {

  test("getBody from base64 encoded body") {
    val in: Map[String, Object] = Map(
      "body"            -> "YWJj",
      "isBase64Encoded" -> java.lang.Boolean.valueOf(true)
    )
    assertEquals(Input.getBody(in), Some("abc"))
  }

  test("decodedBody from plain-text body") {
    assertEquals(Input.decodedBody("abc", BodyEncoding.None), Some("abc"))
  }

  test("decodedBody from base64 encoded body") {
    assertEquals(Input.decodedBody("YWJj", BodyEncoding.Base64), Some("abc"))
  }

  test("bodyFromInput with missing 'body' key returns None") {
    val in: Map[String, Object] = Map.empty
    assertEquals(Input.bodyFromInput(in), None)
  }

  test("bodyFromInput with 'body' key and non-String value returns None") {
    val in: Map[String, Object] = Map("body" -> Integer.valueOf(123))
    assertEquals(Input.bodyFromInput(in), None)
  }

  test("bodyFromInput with 'body' key and 'abc' value returns Some('abc')") {
    val in: Map[String, Object] = Map("body" -> "abc")
    assertEquals(Input.bodyFromInput(in), Some("abc"))
  }

  test("bodyEncodingFromInput with missing 'isBase64Encoded' key returns None") {
    val in: Map[String, Object] = Map.empty
    assertEquals(Input.bodyEncodingFromInput(in), None)
  }

  test("bodyEncodingFromInput with 'isBase64Encoded' key and non-Boolean value returns None") {
    val in: Map[String, Object] = Map("isBase64Encoded" -> Integer.valueOf(123))
    assertEquals(Input.bodyEncodingFromInput(in), None)
  }

  test("bodyEncodingFromInput with 'isBase64Encoded' key and true value returns Some(Base64)") {
    val in: Map[String, Object] = Map("isBase64Encoded" -> java.lang.Boolean.valueOf(true))
    assertEquals(Input.bodyEncodingFromInput(in), Some(BodyEncoding.Base64))
  }

  test("bodyEncodingFromInput with 'isBase64Encoded' key and false value returns Some(None)") {
    val in: Map[String, Object] = Map("isBase64Encoded" -> java.lang.Boolean.valueOf(false))
    assertEquals(Input.bodyEncodingFromInput(in), Some(BodyEncoding.None))
  }

}
