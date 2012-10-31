organization := "default-9ec0b8"

name := "scala-amqp"

version := "0.1-SNAPSHOT"

scalaVersion := "2.9.1"

libraryDependencies ++= Seq(
    "com.typesafe.akka" % "akka-actor" % "2.0.3",
    "com.rabbitmq" % "amqp-client"  % "2.8.7"
    )