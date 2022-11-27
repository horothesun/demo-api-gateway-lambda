import sbtassembly.MergeStrategy

ThisBuild / organization     := "com.horothesun"
ThisBuild / organizationName := "horothesun"
ThisBuild / scalaVersion     := "2.13.10"

val demoApiGatewayLambdaName = "demo-api-gateway-lambda"

lazy val root = project
  .in(file("."))
  .settings(name := demoApiGatewayLambdaName)
  .configs(IntegrationTest)
  .settings(Defaults.itSettings)
  .settings(Dependencies.core)
  .settings(
    assembly / test := Def
      .sequential(Test / test, IntegrationTest / test)
      .value,
    assembly / assemblyMergeStrategy := customMergeStrategy,
    assembly / assemblyJarName       := s"$demoApiGatewayLambdaName.jar"
  )

val customMergeStrategy: String => MergeStrategy = {
  case r if r.endsWith(".conf")            => MergeStrategy.concat
  case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.discard
  // https://stackoverflow.com/questions/46287789/running-an-uber-jar-from-sbt-assembly-results-in-error-could-not-find-or-load-m
  case _ => MergeStrategy.first
}

IntegrationTest / parallelExecution := false
