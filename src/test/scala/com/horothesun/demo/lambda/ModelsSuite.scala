package com.horothesun.demo.lambda

import munit.ScalaCheckSuite
import org.scalacheck.Gen
import org.scalacheck.Prop._
import Models.BodyEncoding._

class ModelsSuite extends ScalaCheckSuite {

  property("NoEncoding.decode == Some[String]") {
    forAll(Gen.asciiPrintableStr) { s =>
      assertEquals(NoEncoding.decode(s), Some(s))
    }
  }

  property("Base64.encode and-then decode == NoEncoding.decode") {
    forAll(Gen.asciiPrintableStr) { s =>
      assertEquals(Base64.decode(Base64.encode(s)), NoEncoding.decode(s))
    }
  }

  test("Base64.decode with base64 text") {
    assertEquals(Base64.decode("YWJj"), Some("abc"))
  }

  test("Base64.decode with non-base64 text") {
    assertEquals(Base64.decode("!@£$%^&*"), None)
  }

  test("Base64.encode") {
    assertEquals(Base64.encode("abc"), "YWJj")
  }

}
