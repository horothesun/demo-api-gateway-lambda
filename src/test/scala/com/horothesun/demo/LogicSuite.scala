package com.horothesun.demo

import cats.effect.IO
import cats.implicits._
import LogicSuite.clockStub
import munit.CatsEffectSuite
import java.time.{LocalDateTime, Month}

class LogicSuite extends CatsEffectSuite {

  test("1 + 1 = 2") {
    assertEquals(1 + 1, 2)
  }

}

object LogicSuite {

  def clockStub(localDateTime: LocalDateTime): Clock = new Clock {
    override def currentDateTime: IO[LocalDateTime] = IO.pure(localDateTime)
  }

}
