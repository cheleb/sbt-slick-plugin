sbtVersion in Global := "0.13.0-RC1"

scalaVersion in ThisBuild := "2.10.2"

organization := "net.orcades"

name := "sbt-slick-plugin"

version := "0.2-SNAPSHOT"

sbtPlugin := true

libraryDependencies += "com.typesafe" % "config" % "1.0.1"

libraryDependencies += "postgresql" % "postgresql" % "9.1-901.jdbc4"

//libraryDependencies += "com.typesafe.slick" %% "slick" % "1.0.1"

//libraryDependencies += "org.scala-sbt" % "sbt" % "0.13.0-M2" 

libraryDependencies += "net.orcades.db" %% "rdbms-metadata-extractor" % "0.1-SNAPSHOT"
