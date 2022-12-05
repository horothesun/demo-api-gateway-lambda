package com.horothesun.demo.lambda

import munit.ScalaCheckSuite
import org.scalacheck.Gen
import org.scalacheck.Prop._
import Models.BodyEncoding._

class ModelsTest extends ScalaCheckSuite {

  property("NoEncoding.decode == Some[String]") {
    forAll(Gen.asciiPrintableStr) { s =>
      assertEquals(NoEncoding.decode(s), Some(s))
    }
  }

  property("Base64Encoding.encode and-then decode == NoEncoding.decode") {
    forAll(Gen.asciiPrintableStr) { s =>
      assertEquals(Base64Encoding.decode(Base64Encoding.encode(s)), NoEncoding.decode(s))
    }
  }

  test("Base64Encoding.decode with base64 text") {
    assertEquals(Base64Encoding.decode("YWJj"), Some("abc"))
  }

  test("Base64Encoding.decode with non-base64 text") {
    assertEquals(Base64Encoding.decode("!@£$%^&*"), None)
  }

  test("Base64Encoding.encode") {
    assertEquals(Base64Encoding.encode("abc"), "YWJj")
  }

}
