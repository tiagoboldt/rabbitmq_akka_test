import AssemblyKeys._

assemblySettings

organization := "pt.inescporto"

name := "scala-amqp"

version := "0.1-SNAPSHOT"

scalaVersion := "2.9.2"

libraryDependencies ++= Seq(
    "com.typesafe.akka" % "akka-actor" % "2.0.3",
    "com.rabbitmq" % "amqp-client"  % "2.8.7"
    )