name := "solipsism"

organization := "nl.papendorp"

version := "0.1-SNAPSHOT"

scalaVersion := "2.12.4"

lazy val akkaVersion = "2.5.6"
lazy val scalaTestVersion = "3.0.1"

libraryDependencies ++= Seq(
	"com.typesafe.akka" %% "akka-actor" % akkaVersion,
	"com.typesafe.akka" %% "akka-stream" % akkaVersion,

	"org.scalatest" %% "scalatest" % scalaTestVersion % Test,

	"com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test,
	"com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion % Test,

	//http://scalamock.org/quick-start/
	"org.scalamock" %% "scalamock-scalatest-support" % "3.5.0" % Test,

	"info.cukes" %% "cucumber-scala" % "1.2.5" % Test,
	"info.cukes" % "cucumber-junit" % "1.2.5" % Test
)

enablePlugins( CucumberPlugin )

CucumberPlugin.glue := "com/waioeka/sbt/"