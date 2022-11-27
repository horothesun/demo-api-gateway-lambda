import sbt._
import sbt.Keys.libraryDependencies

object Dependencies {

  object Version {
    val catsEffect      = "3.4.1"
    val http4s          = "0.23.11"
    val circeGeneric    = "0.14.1"
    val logbackClassic  = "1.4.5"
    val munitCatsEffect = "1.0.7"
  }

  lazy val project: Seq[ModuleID] = Seq(
    "org.typelevel" %% "cats-effect"        % Version.catsEffect,
    "org.typelevel" %% "cats-effect-kernel" % Version.catsEffect,
    "org.typelevel" %% "cats-effect-std"    % Version.catsEffect
  )

  lazy val logs: Seq[ModuleID] = Seq(
    "ch.qos.logback" % "logback-classic" % Version.logbackClassic
  )

  lazy val test: Seq[ModuleID] = Seq(
    "org.typelevel" %% "munit-cats-effect-3" % Version.munitCatsEffect % Test
  )

  lazy val core = libraryDependencies ++= (project ++ logs ++ test)

}
