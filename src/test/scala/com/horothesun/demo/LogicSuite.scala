package com.horothesun.demo

import LogicSuite._
import Models.Input
import Models.Output.DateTimeBody
import cats.effect.IO
import java.time.{LocalDateTime, Month}
import munit._
import org.scalacheck.Gen
import org.scalacheck.Prop._

class LogicSuite extends CatsEffectSuite with ScalaCheckSuite {

  property("getOutput returns correct local date-time for all inputs") {
    forAll(inputGen) { in =>
      val localDateTime = LocalDateTime.of(2022, Month.APRIL, 15, 13, 33, 0)
      Logic
        .getOutput(clockStub(localDateTime))(in)
        .assertEquals(DateTimeBody(localDateTime))
        .unsafeRunSync()
    }
  }

}

object LogicSuite {

  val inputGen: Gen[Input] = Gen.asciiStr.map(Input)

  def clockStub(localDateTime: LocalDateTime): Clock = new Clock {
    override def currentDateTime: IO[LocalDateTime] = IO.pure(localDateTime)
  }

}
