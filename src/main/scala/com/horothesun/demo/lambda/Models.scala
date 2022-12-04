package com.horothesun.demo.lambda

object Models {

  sealed trait BodyEncoding
  object BodyEncoding {
    case object Base64 extends BodyEncoding
    case object None   extends BodyEncoding
  }

}
