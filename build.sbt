scalaVersion in ThisBuild := "2.10.2"

name := "sbt-slick-plugin"

sbtPlugin := true

libraryDependencies += "postgresql" % "postgresql" % "9.1-901.jdbc4"

libraryDependencies += "com.typesafe.slick" %% "slick" % "1.0.1"

libraryDependencies += "org.scala-sbt" % "sbt" % "0.13.0-M2" 

libraryDependencies += "net.orcades.db" %% "rdbms-metadata-extractor" % "0.1-SNAPSHOT"
