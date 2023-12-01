import sbt.*
import sbt.Keys.libraryDependencies

object Dependencies {

  object Version {
    val catsEffect       = "3.5.2"
    val betterMonadicFor = "0.3.1"
    val circe            = "0.14.6"
    val awsLambdaCore    = "1.2.3"
    val awsLambdaEvents  = "3.11.3"
    val logbackClassic   = "1.4.14"
    val munitScalaCheck  = "0.7.29"
    val munitCatsEffect  = "1.0.7"
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
    "org.scalameta" %% "munit-scalacheck"    % Version.munitScalaCheck % Test,
    "org.typelevel" %% "munit-cats-effect-3" % Version.munitCatsEffect % Test
  )

  lazy val core = libraryDependencies ++= (project ++ logs ++ test)

}
