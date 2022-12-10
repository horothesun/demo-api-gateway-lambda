package com.horothesun.demo

import cats.effect.IO
import java.time.{LocalDateTime, Month}
import munit._
import org.scalacheck.Gen
import org.scalacheck.Prop._
import LogicSuite._
import Models.Input

class LogicSuite extends CatsEffectSuite with ScalaCheckSuite {

  property("app logic returns correct local date-time JSON string for all inputs") {
    forAll(inputGen) { in =>
      val fakeLocalDateTime = LocalDateTime.of(2022, Month.APRIL, 15, 13, 33, 0)
      Logic(clockStub(fakeLocalDateTime))
        .appLogic(in)
        .assertEquals("{\"server_date_time\":\"2022-04-15T13:33:00\"}")
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
