package com.horothesun.demo.lambda

import munit.ScalaCheckSuite
import org.scalacheck.Gen
import org.scalacheck.Prop._
import Models.BodyEncoding._

class ModelsTest extends ScalaCheckSuite {

  property("BodyEncoding.NoEncoding.decode == Some[String]") {
    forAll(Gen.alphaNumStr) { s =>
      assertEquals(NoEncoding.decode(s), Some(s))
    }
  }

  test("BodyEncoding.Base64Encoding decode") {
    assertEquals(Base64Encoding.decode("YWJj"), Some("abc"))
  }

  test("BodyEncoding.base64Decode with base64 text") {
    assertEquals(Base64Encoding.base64Decode("YWJj"), Some("abc"))
  }

  test("BodyEncoding.base64Decode with non-base64 text") {
    assertEquals(Base64Encoding.base64Decode("!@£$%^&*"), None)
  }

  test("BodyEncoding.base64Encode") {
    assertEquals(Base64Encoding.base64Encode("abc"), "YWJj")
  }

}
