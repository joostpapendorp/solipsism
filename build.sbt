name := "sandbox"

organization := "nl.papendorp"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.8"

lazy val akkaVersion = "2.4.7"
lazy val scalaTestVersion = "2.2.6"

libraryDependencies ++= Seq(
	"com.typesafe.akka" %% "akka-actor" % akkaVersion,
	"com.typesafe.akka" %% "akka-testkit" % akkaVersion,
	"org.scalatest" %% "scalatest" % scalaTestVersion % "test",
	"junit" % "junit" % "4.12" % "test",
	"com.novocode" % "junit-interface" % "0.11" % "test",

	// workaround for SBT bug
	"org.scala-lang" % "scala-reflect" % "2.11.8",
	"org.scala-lang.modules" %% "scala-xml" % "1.0.4"
)

testOptions += Tests.Argument( TestFrameworks.JUnit, "-v" )
