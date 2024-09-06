val scala3Version = "3.5.0"

lazy val root = project
  .in(file("."))
  .settings(
    name := "bike-fleet",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,
    resolvers += Resolver.bintrayRepo("cakesolutions", "maven"),


    libraryDependencies ++= Seq(
      "org.apache.kafka" % "kafka-clients" % "3.8.0",
      "org.typelevel" %% "cats-effect" % "3.5.4",
      "org.scalameta" %% "munit" % "1.0.0" % Test
    )
  )
