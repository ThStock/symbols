name := "symbols"

version := "1.0"

scalaVersion := "2.13.0"

// scalacOptions ++= Seq("-unchecked", "-feature", "-Xfatal-warnings") // "-deprecation"

javacOptions ++= Seq("-source", "11")

logLevel := Level.Warn

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"

libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"

libraryDependencies += "com.google.guava" % "guava" % "28.2-jre"

libraryDependencies += "org.apache.commons" % "commons-text" % "1.8"

libraryDependencies += "org.scala-lang.modules" %% "scala-parallel-collections" % "0.2.0"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.8" % "test"

libraryDependencies += "org.mockito" %% "mockito-scala" % "1.5.12" % "test"

libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % "test"

libraryDependencies += "com.github.javafaker" % "javafaker" % "1.0.2" % "test"

unmanagedSourceDirectories in Compile := (scalaSource in Compile).value :: Nil

skip in publish := true
