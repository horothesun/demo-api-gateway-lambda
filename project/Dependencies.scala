import sbt._
import sbt.Keys.libraryDependencies

object Dependencies {

  object Version {
    val catsEffect       = "3.4.1"
    val betterMonadicFor = "0.3.1"
    val circe            = "0.14.3"
    val awsLambda        = "1.2.2"
    val gson             = "2.10"
    val logbackClassic   = "1.4.5"
    val munitCatsEffect  = "1.0.7"
  }

  lazy val project: Seq[ModuleID] = Seq(
    "org.typelevel" %% "cats-effect"        % Version.catsEffect,
    "org.typelevel" %% "cats-effect-kernel" % Version.catsEffect,
    "org.typelevel" %% "cats-effect-std"    % Version.catsEffect,
    compilerPlugin("com.olegpy" %% "better-monadic-for" % Version.betterMonadicFor),
    "io.circe"            %% "circe-core"           % Version.circe,
    "io.circe"            %% "circe-generic"        % Version.circe,
    "com.amazonaws"        % "aws-lambda-java-core" % Version.awsLambda,
    "com.google.code.gson" % "gson"                 % Version.gson
  )

  lazy val logs: Seq[ModuleID] = Seq(
    "ch.qos.logback" % "logback-classic" % Version.logbackClassic
  )

  lazy val test: Seq[ModuleID] = Seq(
    "org.typelevel" %% "munit-cats-effect-3" % Version.munitCatsEffect % Test
  )

  lazy val core = libraryDependencies ++= (project ++ logs ++ test)

}
