val scala3Version = "3.5.0"
val circeVersion = "0.14.1"

resolvers += "Akka library repository".at("https://repo.akka.io/maven")

enablePlugins(AkkaGrpcPlugin)
enablePlugins(FlywayPlugin)
flywayUrl := "jdbc:postgresql://localhost:15432/bike_fleet"
flywayUser := "postgres"
flywayPassword := "password"
flywayLocations := Seq("filesystem:src/main/resources/db")

lazy val root = project
  .in(file("."))
  .settings(
    name := "bike-fleet",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    libraryDependencies ++= Seq(
      "org.apache.kafka" % "kafka-clients" % "3.8.0",
      "org.typelevel" %% "cats-effect" % "3.5.4",
      "org.postgresql" % "postgresql" % "42.7.4",
      "org.scalameta" %% "munit" % "1.0.0" % Test
    ) ++ Seq(
      "io.circe" %% "circe-core",
      "io.circe" %% "circe-generic",
      "io.circe" %% "circe-parser"
    ).map(_ % circeVersion)
  )
