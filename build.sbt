name := "stsource"
organization := "tech.navicore"
fork := true
parallelExecution in test := false

val akkaVersion = "2.8.5"
val scala212 = "2.12.7"
val scala211 = "2.11.12"

inThisBuild(List(
  organization := "tech.navicore",
  homepage := Some(url("https://github.com/navicore/stsource")),
  licenses := List("MIT" -> url("https://github.com/navicore/stsource/blob/master/LICENSE")),
  developers := List(
    Developer(
      "navicore",
      "Ed Sweeney",
      "ed@onextent.com",
      url("https://navicore.tech")
    )
  )
))

libraryDependencies ++=
  Seq(

    "org.antlr" % "stringtemplate" % "4.0.2",

    "com.typesafe.scala-logging" %% "scala-logging" % "3.9.0",

    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-stream" % akkaVersion,

    "org.scalatest" %% "scalatest" % "3.2.16" % "test"
  )

dependencyOverrides ++= Seq(
  "com.typesafe.akka" %% "akka-actor"  % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion
)

assemblyJarName in assembly := "stsource.jar"

assemblyMergeStrategy in assembly := {
  case PathList("reference.conf") => MergeStrategy.concat
  case x if x.endsWith("io.netty.versions.properties") => MergeStrategy.first
  case PathList("META-INF", _ @ _*) => MergeStrategy.discard
  case _ => MergeStrategy.first
}

