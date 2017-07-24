name := "ScalaPlay"

version := "1.0"

lazy val `scalaplay` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq( jdbc , cache , ws   , specs2 % Test )

// https://mvnrepository.com/artifact/org.scalastyle/scalastyle_2.10
libraryDependencies += "org.scalastyle" % "scalastyle_2.10" % "0.9.0"


unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )  

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
