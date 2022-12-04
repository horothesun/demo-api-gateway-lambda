package com.horothesun.demo.lambda

import com.horothesun.demo.lambda.Models._
import io.circe.syntax._
import munit.FunSuite

class ModelsTest extends FunSuite {

  test("LambdaOutput encoding") {
    val obtainedEncoding = LambdaOutput(
      isBase64Encoded = false,
      StatusCode.OK,
      body = "{\"hello\":\"world\"}",
      headers = Map("content-type" -> "application/json"),
      cookies = List.empty
    ).asJson.noSpaces
    val expectedEncoding =
      """
        |{
        |  "isBase64Encoded": false,
        |  "statusCode": 200,
        |  "body": "{\"hello\":\"world\"}",
        |  "headers": { "content-type": "application/json" },
        |  "cookies": []
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
        StatusCode.OK,
        body,
        headers = Map("content-type" -> "application/json"),
        cookies = List.empty
      )
    )
  }

  test("LambdaOutput fromBodyAndEncoding with base64 encoded body") {
    val body = "{\"hello\":\"world\"}"
    assertEquals(
      LambdaOutput.fromBodyAndEncoding(body, BodyEncoding.Base64),
      LambdaOutput(
        isBase64Encoded = true,
        StatusCode.OK,
        body = "eyJoZWxsbyI6IndvcmxkIn0=",
        headers = Map("content-type" -> "application/json"),
        cookies = List.empty
      )
    )
  }

}
