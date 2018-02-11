name := "solipsism"

organization := "nl.papendorp"

version := "0.1-SNAPSHOT"

scalaVersion := "2.12.4"

lazy val akkaVersion = "2.5.6"
lazy val scalafxVersion = "8.0.144-R12"

lazy val scalaTestVersion = "3.0.1"
lazy val scalaCheckVersion = "1.13.4"
lazy val cucumberVersion = "1.2.5"

libraryDependencies ++= Seq(
	"com.typesafe.akka" %% "akka-actor" % akkaVersion,
	"com.typesafe.akka" %% "akka-stream" % akkaVersion,

	"org.scalafx" %% "scalafx" % scalafxVersion,

	"org.scalatest" %% "scalatest" % scalaTestVersion % Test,
	"org.scalacheck" %% "scalacheck" % scalaCheckVersion % Test,

	"com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test,
	"com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion % Test,

	//http://scalamock.org/quick-start/
	"org.scalamock" %% "scalamock-scalatest-support" % "3.5.0" % Test,

	"info.cukes" %% "cucumber-scala" % cucumberVersion % Test
)
