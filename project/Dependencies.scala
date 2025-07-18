import sbt.*
import sbt.Keys.libraryDependencies

object Dependencies {

  object Version {

    val catsEffect = "3.6.2"

    val betterMonadicFor = "0.3.1"

    val circe = "0.14.14"

    val awsLambdaCore = "1.3.0"

    val awsLambdaEvents = "3.16.1"

    val logbackClassic = "1.5.18"

    val munitScalaCheck = "1.1.0"

    val munitCatsEffect = "2.1.0"

  }

  lazy val project: Seq[ModuleID] = Seq(
    "org.typelevel" %% "cats-effect" % Version.catsEffect,
    compilerPlugin("com.olegpy" %% "better-monadic-for" % Version.betterMonadicFor),
    "io.circe"     %% "circe-core"             % Version.circe,
    "io.circe"     %% "circe-generic"          % Version.circe,
    "com.amazonaws" % "aws-lambda-java-core"   % Version.awsLambdaCore,
    "com.amazonaws" % "aws-lambda-java-events" % Version.awsLambdaEvents
  )

  lazy val logs: Seq[ModuleID] = Seq(
    "ch.qos.logback" % "logback-classic" % Version.logbackClassic
  )

  lazy val test: Seq[ModuleID] = Seq(
    "org.scalameta" %% "munit-scalacheck"  % Version.munitScalaCheck % Test,
    "org.typelevel" %% "munit-cats-effect" % Version.munitCatsEffect % Test
  )

  lazy val core = libraryDependencies ++= (project ++ logs ++ test)

}
