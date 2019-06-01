name := """TACS-2019C1"""
organization := "UTN-FRBA"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.12.8"

libraryDependencies += guice


// Test Database
libraryDependencies += "com.h2database" % "h2" % "1.4.194"

// Testing libraries for dealing with CompletionStage...
libraryDependencies += "org.assertj" % "assertj-core" % "3.6.2" % Test
libraryDependencies += "org.awaitility" % "awaitility" % "2.0.0" % Test


// My framewords
libraryDependencies += "org.mindrot" % "jbcrypt" % "0.4"
libraryDependencies += "com.auth0" % "java-jwt" % "2.2.1"
  // MongoDb Conecction
libraryDependencies += "org.mongodb" % "mongo-java-driver" % "3.10.2"
libraryDependencies += ws
