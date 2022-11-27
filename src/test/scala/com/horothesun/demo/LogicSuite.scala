package com.horothesun.demo

import cats.effect.IO
import LogicSuite.clockStub
import munit.CatsEffectSuite
import java.time.{LocalDateTime, Month}

class LogicSuite extends CatsEffectSuite {

  test("app logic returns correct local date-time JSON") {
    val fakeLocalDateTime = LocalDateTime.of(2022, Month.APRIL, 15, 13, 33, 0)
    Logic(clockStub(fakeLocalDateTime)).appLogic
      .assertEquals("{\"server_date_time\":\"2022-04-15T13:33:00\"}")
  }

}

object LogicSuite {

  def clockStub(localDateTime: LocalDateTime): Clock = new Clock {
    override def currentDateTime: IO[LocalDateTime] = IO.pure(localDateTime)
  }

}
