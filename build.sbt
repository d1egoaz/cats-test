import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.d1egoaz",
      scalaVersion := "2.11.8",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "cats-test",
    libraryDependencies ++= Seq(
      cats,
      scalaTest % Test
    )
  )
