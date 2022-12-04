package com.horothesun.demo.lambda

import com.horothesun.demo.lambda.Models._
import io.circe.syntax._
import munit.FunSuite

class ModelsTest extends FunSuite {

  test("LambdaOutput encoding") {
    val obtainedEncoding = LambdaOutput(
      isBase64Encoded = false,
      StatusCode.Ok,
      body = "{\"hello\":\"world\"}",
      headers = Map("content-type" -> "application/json")
    ).asJson.noSpaces
    val expectedEncoding =
      """
        |{
        |  "isBase64Encoded": false,
        |  "statusCode": 200,
        |  "body": "{\"hello\":\"world\"}",
        |  "headers": { "content-type": "application/json" }
        |}
        |""".stripMargin.replace("\n", "").replace(" ", "")
    assertEquals(obtainedEncoding, expectedEncoding)
  }

  test("LambdaOutput fromBodyAndEncoding with plain-text body") {
    val body = "{\"hello\":\"world\"}"
    assertEquals(
      LambdaOutput.fromBodyAndEncoding(body, BodyEncoding.None),
      LambdaOutput(
        isBase64Encoded = false,
        StatusCode.Ok,
        body,
        headers = Map("content-type" -> "application/json")
      )
    )
  }

  test("LambdaOutput fromBodyAndEncoding with base64 encoded body") {
    val body = "{\"hello\":\"world\"}"
    assertEquals(
      LambdaOutput.fromBodyAndEncoding(body, BodyEncoding.Base64),
      LambdaOutput(
        isBase64Encoded = true,
        StatusCode.Ok,
        body = "eyJoZWxsbyI6IndvcmxkIn0=",
        headers = Map("content-type" -> "application/json")
      )
    )
  }

}
