package com.horothesun.demo

import cats.effect.unsafe.implicits.global
import com.google.gson._
import com.amazonaws.services.lambda.runtime._

class LambdaHandler() extends RequestHandler[java.util.Map[String, String], String] {

  val gson: Gson = new GsonBuilder().setPrettyPrinting().create

  override def handleRequest(event: java.util.Map[String, String], context: Context): String = {
    val logger = context.getLogger

    logger.log(s"ENVIRONMENT VARIABLES: ${gson.toJson(System.getenv)}\n")
    logger.log(s"CONTEXT: ${gson.toJson(context)}\n")

    logger.log(s"EVENT: ${gson.toJson(event)}\n")

    Main.run.unsafeRunSync()
  }

}
