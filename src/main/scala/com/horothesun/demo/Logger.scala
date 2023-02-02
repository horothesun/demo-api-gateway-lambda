package com.horothesun.demo

import cats.effect.IO
import com.amazonaws.services.lambda.runtime.LambdaLogger

trait Logger {
  def info(message: => String): IO[Unit]
  def warning(message: => String): IO[Unit]
  def error(message: => String): IO[Unit]
}

object Logger {

  def create(lambdaLogger: LambdaLogger): Logger = new Logger {

    override def info(message: => String): IO[Unit] =
      log(LogLevel.Info, message)

    override def warning(message: => String): IO[Unit] =
      log(LogLevel.Warning, message)

    override def error(message: => String): IO[Unit] =
      log(LogLevel.Error, message)

    private def log(logLevel: LogLevel, message: => String): IO[Unit] =
      IO(lambdaLogger.log(s"${logLevel.prefix}: $message\n"))

  }

  sealed trait LogLevel {
    import LogLevel._
    def prefix: String =
      this match {
        case Info    => "[INFO]"
        case Warning => "[WARNING]"
        case Error   => "[ERROR]"
      }
  }
  object LogLevel {
    case object Info    extends LogLevel
    case object Warning extends LogLevel
    case object Error   extends LogLevel
  }

}
