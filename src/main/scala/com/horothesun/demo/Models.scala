package com.horothesun.demo

import java.time.LocalDateTime

object Models {

  case class Input(value: String)

  case class DateTimeBody(server_date_time: LocalDateTime)

}
